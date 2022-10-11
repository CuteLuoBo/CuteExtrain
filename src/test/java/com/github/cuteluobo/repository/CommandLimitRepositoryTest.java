package com.github.cuteluobo.repository;

import cn.pomit.mybatis.configuration.MybatisConfiguration;
import com.github.cuteluobo.enums.TriggerType;
import com.github.cuteluobo.model.CommandLimit;
import org.apache.ibatis.session.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 指令限制类
 */
class CommandLimitRepositoryTest {
    private static CommandLimitRepository commandLimitRepository;

    /**
     * 初始化数据库配置文件
     */
    private static void setUpDataBase() {
        Properties properties = new Properties();
        properties.put("mybatis.mapper.scan", "com.github.cuteluobo.mapper");
        properties.put("mybatis.datasource.type", "POOLED");
        properties.put("mybatis.datasource.driver", "org.sqlite.JDBC");
        URL url = CommandLimitRepositoryTest.class.getClassLoader().getResource("database.sqlite");
        properties.put("mybatis.datasource.url", "jdbc:sqlite:" + url);
        properties.put("mybatis.datasource.username", "");
        properties.put("mybatis.datasource.password", "");
        properties.setProperty("mapUnderscoreToCamelCase", "true");
        properties.put("mybatis.logImpl", "STDOUT_LOGGING");
        MybatisConfiguration.initConfiguration(properties);
        Configuration configuration = MybatisConfiguration.getSqlSessionFactory().getConfiguration();
        configuration.setMapUnderscoreToCamelCase(true);
    }

    @BeforeAll
    static void setUp() {
        setUpDataBase();
        commandLimitRepository = CommandLimitRepository.getInstance();
        System.out.println("========= CommandLimitRepositoryTest =========");
    }

    @Test()
    void createKey() {
        String key = CommandLimitRepository.createKey(10000l, 123456l, "");
        assertNotNull(key);
    }

    @Test
    void getCommandLimit() {
        Map<String, CommandLimit> tempMap = new HashMap<>();
        long groupId = 10000L;
        long userId = 0L;
        String primary = "test";
        CommandLimit commandLimit = new CommandLimit();
        commandLimit.setUserId(userId);
        commandLimit.setGroupId(groupId);
        commandLimit.setPrimary(primary);
        commandLimit.setCycleNum(1);
        commandLimit.setCycleSecond(20);
        commandLimit.setState(TriggerType.IGNORE_TEN_MINUTE.getValue());
        tempMap.put(CommandLimitRepository.createKey(groupId,userId,primary), commandLimit);
        commandLimitRepository.setTempMap(tempMap);
        CommandLimit getLimit = commandLimitRepository.getCommandLimit(10000l, 123456l, "");
        assertEquals(getLimit, commandLimit);
    }
}