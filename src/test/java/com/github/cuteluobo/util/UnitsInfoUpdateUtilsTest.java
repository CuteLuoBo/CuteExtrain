package com.github.cuteluobo.util;

import cn.pomit.mybatis.configuration.MybatisConfiguration;
import com.github.cuteluobo.mapper.CommandLimitMapper;
import com.github.cuteluobo.mapper.SystemMapper;
import com.github.cuteluobo.mapper.YysUnitMapper;
import com.github.cuteluobo.model.YysUnit;
import org.apache.ibatis.session.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;

import static org.junit.jupiter.api.Assertions.*;

class UnitsInfoUpdateUtilsTest {

    @BeforeAll
    public static void load() {
        File databaseFile = new File("J:\\IDEA work-space\\CuteExtrain\\src\\test\\resources\\database.sqlite");
        //TODO 增加从配置文件读取并使用mysql数据库配置方式
        Properties properties = new Properties();
        properties.put("mybatis.mapper.scan", "com.github.cuteluobo.mapper");
//        error properties.put("mybatis.mapper.scan", "com.github.cuteluobo.model");
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
        Class[] classes = {CommandLimitMapper.class, SystemMapper.class, YysUnitMapper.class};
        for (Class c : classes) {
            if (!configuration.getMapperRegistry().hasMapper(c)) {
                configuration.addMapper(c);
            }
        }
    }

    @Test
    void appendUnitsListTask() throws Exception {
        Callable<List<YysUnit>> task = UnitsInfoUpdateUtils.appendUnitsListTask();
        List<YysUnit> newUnitList = task.call();
        int size = newUnitList.size();
        System.out.println("本次更新式神数量: "+size);
        if (size <= 10) {
            for (int i = 0; i < size; i++) {
                YysUnit unit = newUnitList.get(i);
                System.out.println(unit.getUnitId() + "-" + unit.getName());
            }
        }
    }

    @Test
    void updateUnitImageTask() {
        //TODO 完成下载测试
    }
}