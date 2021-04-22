package com.github.cuteluobo;

import cn.pomit.mybatis.ProxyHandlerFactory;
import cn.pomit.mybatis.configuration.MybatisConfiguration;
import cn.pomit.mybatis.configuration.MybatisProperties;
import com.github.cuteluobo.command.RollCommand;
import com.github.cuteluobo.enums.DatabaseTable;
import com.github.cuteluobo.mapper.SystemMapper;
import kotlin.coroutines.CoroutineContext;
import kotlinx.coroutines.Job;
import net.mamoe.mirai.console.command.Command;
import net.mamoe.mirai.console.permission.PermissionId;
import net.mamoe.mirai.console.permission.PermissionRegistryConflictException;
import net.mamoe.mirai.console.permission.PermissionService;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.message.MessageReceipt;
import net.mamoe.mirai.message.data.*;
import net.mamoe.mirai.utils.MiraiLogger;
import org.apache.ibatis.jdbc.SqlRunner;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.File;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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


    /**初始化*/
    private CuteExtra() {
        super(new JvmPluginDescriptionBuilder(
                "SimulatedExtraction",
                "0.1.0")
                .author("Cute_LuoBo-635419450")
                .info("模拟抽卡插件")
                .build());
    }



    public void onLoad() throws SQLException {
        initDatasource();
    }

    @Override
    public void onEnable() {
        try {
            permissionExecute();
        } catch (PermissionRegistryConflictException e) {
            e.printStackTrace();
        }
    }

    /**初始化权限*/
    private void permissionExecute() throws PermissionRegistryConflictException {
//        PermissionService.getInstance().register(new PermissionId("CuteExtra","normal"),"插件默认权限",null);
    }

    private void commandReg(){
        //TODO 解决指令注册问题
        RollCommand rollCommand = new RollCommand();
    }

    private void initDatasource() throws SQLException {
        try {
            logger.info("开始加载数据库配置");
            Properties properties= new Properties();
            properties.put("mybatis.mapper.scan", "com.github.cuteluobo.mapper");
            properties.put("mybatis.datasource.type", "POOLED");
            properties.put("mybatis.datasource.driver", "org.sqlite.JDBC");
            //TODO 增加从配置文件读取并使用mysql数据库配置方式
            properties.put("mybatis.datasource.url", "jdbc:sqlite:database.sqlite");
            properties.put("mybatis.datasource.username", "");
            properties.put("mybatis.datasource.password", "");
            properties.setProperty("mapUnderscoreToCamelCase", "true");
            properties.put("mybatis.logImpl","STDOUT_LOGGING");
            MybatisConfiguration.initConfiguration(properties);
            MybatisConfiguration.getSqlSessionFactory().getConfiguration().setMapUnderscoreToCamelCase(true);
            SystemMapper systemMapper = ProxyHandlerFactory.getMapper(SystemMapper.class);
            Boolean noUseSchema = true;
            for (DatabaseTable d :
                    DatabaseTable.values()) {
                noUseSchema &= systemMapper.existTable(d.getTableName());
            }
            //TODO 初始化数据库需要DEBUG查看问题
            //有表名不存在时
            if (!noUseSchema) {
                logger.info("数据表不存在，进行初始化");
                String sql = getResource("schema.sql");
                if (sql == null) {
                    logger.error("数据表初始化失败，无法读取内置SQL");
                }
                SqlRunner sqlRunner = new SqlRunner(MybatisConfiguration.getSqlSessionFactory().openSession().getConnection());
                sqlRunner.run(sql);
                logger.info("初始化数据表完成");
            }
        } catch (SQLException e) {
            logger.error("初始化数据库配置失败，请检查配置");
            throw e;
        }
        logger.info("加载数据库完成");
    }

}          