package com.github.cuteluobo;

import cn.pomit.mybatis.ProxyHandlerFactory;
import cn.pomit.mybatis.configuration.MybatisConfiguration;
import com.github.cuteluobo.command.RollCommand;
import com.github.cuteluobo.enums.DatabaseTable;
import com.github.cuteluobo.mapper.CommandLimitMapper;
import com.github.cuteluobo.mapper.SystemMapper;
import com.github.cuteluobo.mapper.YysUnitMapper;
import net.mamoe.mirai.console.command.CommandManager;
import net.mamoe.mirai.console.permission.Permission;
import net.mamoe.mirai.console.permission.PermissionId;
import net.mamoe.mirai.console.permission.PermissionRegistryConflictException;
import net.mamoe.mirai.console.permission.PermissionService;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.utils.MiraiLogger;
import org.apache.ibatis.session.Configuration;

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
    public static final String PLUGIN_VERSION = "0.3.4";
    public static final String DATABASE_FILE_NAME = "database.sqlite";
    public static Permission basePermission ;


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
    }

    /**初始化权限*/
    private void permissionExecute() throws PermissionRegistryConflictException {
        basePermission = PermissionService.getInstance().register(new PermissionId(PLUGIN_ID,"base"),"插件默认权限", Permission.getRootPermission());
    }

    private void commandReg() throws PermissionRegistryConflictException {
        RollCommand rollCommand = new RollCommand();
        CommandManager.INSTANCE.registerCommand(rollCommand, false);
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
    }

}