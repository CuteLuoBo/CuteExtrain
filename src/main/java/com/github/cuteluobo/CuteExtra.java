package com.github.cuteluobo;

import cn.pomit.mybatis.ProxyHandlerFactory;
import cn.pomit.mybatis.configuration.MybatisConfiguration;
import com.github.cuteluobo.command.InvitedCommand;
import com.github.cuteluobo.command.RollCommand;
import com.github.cuteluobo.command.YysUnitInfoCommand;
import com.github.cuteluobo.enums.DatabaseTable;
import com.github.cuteluobo.mapper.CommandLimitMapper;
import com.github.cuteluobo.mapper.SystemMapper;
import com.github.cuteluobo.mapper.YysUnitMapper;
import com.github.cuteluobo.repository.GlobalConfig;
import com.github.cuteluobo.repository.InvitedEventRepository;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.console.command.CommandManager;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.MemberCommandSender;
import net.mamoe.mirai.console.command.UserCommandSender;
import net.mamoe.mirai.console.permission.*;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.Listener;
import net.mamoe.mirai.event.events.*;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.utils.LoggerAdapters;
import net.mamoe.mirai.utils.MiraiLogger;
import org.apache.ibatis.session.Configuration;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import net.mamoe.mirai.console.permission.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 参考文档：https://github.com/Karlatemp/mirai-console-example-plugin/blob/master/src/main/kotlin/org/example/my/plugin/MyPluginMain.kt
 * https://github.com/mamoe/mirai-console/blob/master/docs/Commands.md
 * https://github.com/mamoe/mirai-console/blob/master/docs/PluginData.md
 * https://github.com/mamoe/mirai-console/blob/master/docs/Permissions.md
 * 主类
 * @author Cute_LuoBo
 */
public final class CuteExtra extends JavaPlugin {

    private MiraiLogger logger  = getLogger();
    public static final CuteExtra INSTANCE = new CuteExtra();
    public static final String PLUGIN_NAME = "cute-extra 模拟抽卡插件";
    public static final String PLUGIN_ID = "com.github.cuteluobo.cute-extra";
    public static final String PLUGIN_VERSION = "0.5.1";
    public static final String DATABASE_FILE_NAME = "database.sqlite";
    /**
     * 基础权限
     */
    private Permission basePermission ;
    /**
     * 管理权限
     */
    private Permission adminPermission ;


    /**初始化*/
    private CuteExtra() {
        super(new JvmPluginDescriptionBuilder(
                PLUGIN_ID,
                PLUGIN_VERSION)
                .author("Cute_LuoBo")
                .info("模拟抽卡插件")
                .build());
    }



    public void onLoad() throws SQLException {

    }

    @Override
    public void onEnable() {
        try {
            initDatasource();
            permissionExecute();
            commandReg();
            logger.info(PLUGIN_NAME+" v"+PLUGIN_VERSION+"加载完成");
        } catch (PermissionRegistryConflictException | SQLException | IOException e) {
            logger.error(PLUGIN_NAME+" v"+PLUGIN_VERSION+"加载错误，请查看打印信息");
            e.printStackTrace();
        }
//        https://github.com/mamoe/mirai/blob/dev/docs/Events.md#%E5%BF%AB%E9%80%9F%E6%8C%87%E5%AF%BC
//        //群邀请监听
        Listener botInvitedJoinGroupRequestEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(BotInvitedJoinGroupRequestEvent.class, event -> {
            MessageChain chain = new MessageChainBuilder()
                    .append("群加入邀请事件消息 ID: ").append(String.valueOf(event.getEventId())).append("\n")
                    .append("邀请人：").append(event.getInvitorNick()).append("(").append(String.valueOf(event.getInvitorId())).append(")\n")
                    .append("对应群：").append(event.getGroupName()).append("(").append(String.valueOf(event.getGroupId())).append(")\n")
                    .append("输入/invited 或/i group/g <群号> <ture/false> 来处理事件\n")
                    .append("例:/i g 1234567 ture -拒绝1234567群邀请\n")
                    .build();
            if (event.getBot().getFriend(GlobalConfig.ADMIN_ID) != null) {
                event.getBot().getFriend(GlobalConfig.ADMIN_ID).sendMessage(chain);
            } else {
                logger.warning("机器人没有管理员"+GlobalConfig.ADMIN_ID+"好友，无法发送通知");
            }
            //缓存事件
            InvitedEventRepository.INSTANCE.getGroupInvitedJoinEventMap().put(event.getGroupId(),event);
        });
        //好友申请监听
        Listener botInvitedFriendsRequestEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(NewFriendRequestEvent.class, event -> {
            MessageChain chain = new MessageChainBuilder()
                    .append("好友添加事件消息 ID: ").append(String.valueOf(event.getEventId())).append("\n")
                    .append("添加人：").append(event.getFromNick()).append("(").append(String.valueOf(event.getFromId())).append(")\n")
                    .append("来自群：").append(event.getFromGroup()==null?"无":event.getFromGroup().getName()).append("(").append(String.valueOf(event.getFromGroupId())).append(")\n")
                    .append("好友申请消息：").append(event.getMessage()).append("\n")
                    .append("默认同意\n")
                    .build();
            if (event.getBot().getFriend(GlobalConfig.ADMIN_ID) != null) {
                event.getBot().getFriend(GlobalConfig.ADMIN_ID).sendMessage(chain);
            } else {
                logger.warning("机器人没有管理员"+GlobalConfig.ADMIN_ID+"好友，无法发送通知");
            }
            event.accept();
            //非默认通过时，缓存事件进行处理
//            InvitedEventRepository.INSTANCE.getFriendRequestEventMap().put(event.getFromId(),event);
        });
    }

    /**初始化权限*/
    private void permissionExecute() throws PermissionRegistryConflictException {
        basePermission = PermissionService.getInstance().register(new PermissionId(PLUGIN_ID,"base"),"插件默认权限", this.getParentPermission());
        adminPermission = PermissionService.getInstance().register(new PermissionId(PLUGIN_ID,"admin"),"插件管理员权限", this.getParentPermission());
        //赋予所有用户默认权限
        PermissionService.permit(AbstractPermitteeId.AnyUser.INSTANCE, basePermission.getId());
    }

    private void commandReg() throws PermissionRegistryConflictException {
        CommandManager.INSTANCE.registerCommand(new RollCommand(), false);
        CommandManager.INSTANCE.registerCommand(new InvitedCommand(), false);
        CommandManager.INSTANCE.registerCommand(new YysUnitInfoCommand(), false);
    }

    private void initDatasource() throws SQLException, IOException {
        logger.info("开始加载数据库配置");
        File pluginDataFolder = this.getDataFolder();
        File databaseFile = new File(pluginDataFolder.getPath() + "/" + DATABASE_FILE_NAME);
        //不存在数据库文件时进行复制
        if (!databaseFile.exists()) {
            logger.info("检测到数据库不存在，读取内置数据库进行复制");
            try {
                InputStream resourceStream = getResourceAsStream(DATABASE_FILE_NAME);
                Path target = Paths.get(databaseFile.getPath());
                Files.copy(resourceStream, target);
                logger.info("复制数据库完成");
            } catch (IOException e) {
                logger.error("复制数据库失败");
                throw e;
            }
        }
        //TODO 增加从配置文件读取并使用mysql数据库配置方式
        Properties properties = new Properties();
        properties.put("mybatis.mapper.scan", "com.github.cuteluobo.mapper");
        properties.put("mybatis.datasource.type", "POOLED");
        properties.put("mybatis.datasource.driver", "org.sqlite.JDBC");
        properties.put("mybatis.datasource.url", "jdbc:sqlite:" + databaseFile.getPath());
        properties.put("mybatis.datasource.username", "");
        properties.put("mybatis.datasource.password", "");
        properties.setProperty("mapUnderscoreToCamelCase", "true");
        properties.put("mybatis.logImpl", "STDOUT_LOGGING");
        MybatisConfiguration.initConfiguration(properties);
        Configuration configuration = MybatisConfiguration.getSqlSessionFactory().getConfiguration();
        configuration.setMapUnderscoreToCamelCase(true);
        //以插件执行时通过包名导入无效，尝试手动指定class
        Class[] classes = {CommandLimitMapper.class,SystemMapper.class,YysUnitMapper.class};
        for (Class c : classes) {
            if (!configuration.getMapperRegistry().hasMapper(c)) {
                configuration.addMapper(c);
            }
        }

//            MybatisConfiguration.getSqlSessionFactory().getConfiguration().addMappers("com.github.cuteluobo.mapper");
//            SystemMapper systemMapper = ProxyHandlerFactory.getMapper(SystemMapper.class);
//            Boolean noUseSchema = true;
//            for (DatabaseTable d :
//                    DatabaseTable.values()) {
//                noUseSchema &= systemMapper.existTable(d.getTableName());
//            }
//            //有表名不存在时
//            if (!noUseSchema) {
//                logger.info("数据表不存在，进行初始化");
//                String sql = getResource("schema.sql");
//                if (sql == null) {
//                    logger.error("数据表初始化失败，无法读取内置SQL");
//                }
//                SqlRunner sqlRunner = new SqlRunner(MybatisConfiguration.getSqlSessionFactory().openSession().getConnection());
//                 sqlRunner.run(sql);
//                sqlRunner.closeConnection();
//                logger.info("初始化数据表完成");
//            }
        logger.info("加载数据库完成");
//        LoggerAdapters.useLog4j2();
    }

    public Permission getBasePermission() {
        return basePermission;
    }

    public void setBasePermission(Permission basePermission) {
        this.basePermission = basePermission;
    }

    public Permission getAdminPermission() {
        return adminPermission;
    }

    public void setAdminPermission(Permission adminPermission) {
        this.adminPermission = adminPermission;
    }
}