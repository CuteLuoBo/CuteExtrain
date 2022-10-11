package com.github.cuteluobo.util;

import cn.pomit.mybatis.configuration.MybatisConfiguration;
import com.github.cuteluobo.enums.TriggerType;
import com.github.cuteluobo.model.CommandLimit;
import com.github.cuteluobo.pojo.CommandExecTemp;
import com.github.cuteluobo.repository.CommandLimitRepository;
import org.apache.ibatis.session.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class CommandLimitUtilsTest {
    private static CommandLimitUtils commandLimitUtils;

    /**
     * 初始化数据库配置文件
     */
    private static void setUpDataBase() {
        Properties properties = new Properties();
        properties.put("mybatis.mapper.scan", "com.github.cuteluobo.mapper");
        properties.put("mybatis.datasource.type", "POOLED");
        properties.put("mybatis.datasource.driver", "org.sqlite.JDBC");
        URL url = CommandLimitUtilsTest.class.getClassLoader().getResource("database.sqlite");
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
        commandLimitUtils = CommandLimitUtils.getInstance();
        System.out.println("========= CommandLimitUtilsTest =========");
    }

    /**
     * 因为是单例，所以每个测试方法执行前，都需要清除缓存列表
     */
    @BeforeEach
    public void clearData() {
        commandLimitUtils.clearAllRecord();
    }

    @Test
    void getCommandRecord() {
        CommandExecTemp commandExecTemp = commandLimitUtils.getCommandRecord(1L, 1L, "test");
        assertNull(commandExecTemp);
    }

    @Test
    void testGetCommandRecord() {
        CommandExecTemp commandExecTemp = commandLimitUtils.getCommandRecord(1L,"test");
        assertNull(commandExecTemp);
    }

    /**
     * 用户群内限制
     */
    @Test
    void addCommandRecord() {
        //增加记录-第1次
        CommandExecTemp commandExecTemp = commandLimitUtils.addCommandRecord(1L, 1L, "test");
        assertNotNull(commandExecTemp);
        //获取记录-第1次
        CommandExecTemp commandExecTempGet = commandLimitUtils.getCommandRecord(1L, 1L, "test");
        assertNotNull(commandExecTempGet);
        assertEquals(commandExecTempGet.getNumber(),1);

        //增加记录-第2次
        commandExecTemp = commandLimitUtils.addCommandRecord(1L, 1L, "test");
        assertNotNull(commandExecTemp);
        //获取记录-第2次
        commandExecTempGet = commandLimitUtils.getCommandRecord(1L, 1L, "test");
        assertNotNull(commandExecTempGet);
        assertEquals(commandExecTempGet.getNumber(),2);
    }




    /**
     * 用户全局限制
     */
    @Test
    void testAddCommandRecord() {
        //增加记录-第1次
        CommandExecTemp commandExecTemp = commandLimitUtils.addCommandRecord( 1L, "test");
        assertNotNull(commandExecTemp);
        //获取记录-第1次
        CommandExecTemp commandExecTempGet = commandLimitUtils.getCommandRecord( 1L, "test");
        assertNotNull(commandExecTempGet);
        assertEquals(commandExecTempGet.getNumber(),1);

        //增加记录-第2次
        commandExecTemp = commandLimitUtils.addCommandRecord( 1L, "test");
        assertNotNull(commandExecTemp);
        //获取记录-第2次
        commandExecTempGet = commandLimitUtils.getCommandRecord( 1L, "test");
        assertNotNull(commandExecTempGet);
        assertEquals(commandExecTempGet.getNumber(),2);
    }

    /**
     * 清除指令记录
     */
    @Test
    void clearCommandRecord() {
        CommandExecTemp commandExecTemp = commandLimitUtils.addCommandRecord( 1L, "test");
        assertNotNull(commandExecTemp);
        commandLimitUtils.clearCommandRecord(1L,1L,"test");
        CommandExecTemp commandExecTempGet = commandLimitUtils.getCommandRecord( 1L,1L, "test");
        assertNull(commandExecTempGet);
    }

    /**
     * 指令校验
     */
    @Test
    @DisplayName("指定用户指定群限制")
    void commandVerify1() throws InterruptedException {
        modelTest(123456L, 10000L, 5,2, "test", TriggerType.IGNORE_TEN_MINUTE);
    }

    @Test
    @DisplayName("指定用户限制")
    void commandVerify2() throws InterruptedException {
        modelTest(123456L, 0L, 5,2, "test", TriggerType.IGNORE_TEN_MINUTE);
    }

    @Test
    @DisplayName("指定用户限制")
    void commandVerify3() throws InterruptedException {
        modelTest(123456L, null, 5,2, "test", TriggerType.IGNORE_TEN_MINUTE);
    }
    @Test
    @DisplayName("指定群限制")
    void commandVerify4() throws InterruptedException {
        modelTest(0L, 10000L, 5,2, "test", TriggerType.IGNORE_TEN_MINUTE);
    }
    @Test
    @DisplayName("全局限制")
    void commandVerify5() throws InterruptedException {
        modelTest(0L, 0L, 5,2, "test", TriggerType.IGNORE_TEN_MINUTE);
    }
    @Test
    @DisplayName("全局私聊限制")
    void commandVerify6() throws InterruptedException {
        modelTest(0L, null, 5,2, "test", TriggerType.IGNORE_TEN_MINUTE);
    }

    @Test
    @DisplayName("指定全局指令限制")
    void commandVerify7() throws InterruptedException {
        modelTest(1L, null, 5,2, "*", TriggerType.IGNORE_TEN_MINUTE);
        modelTest(null, 10000L, 5,2, "*", TriggerType.IGNORE_TEN_MINUTE);
        modelTest(null, null, 5,2, "*", TriggerType.IGNORE_TEN_MINUTE);
    }

    private void modelTest(Long limitUserId,Long limitGroupId,int cycleSecond,int limitNum,String limitCommand,TriggerType triggerType) throws InterruptedException {
        //初始化指令限制
        CommandLimit commandLimit = new CommandLimit();
        commandLimit.setUserId(limitUserId);
        commandLimit.setGroupId(limitGroupId);
        commandLimit.setPrimary(limitCommand);
        commandLimit.setCycleNum(limitNum);
        commandLimit.setCycleSecond(cycleSecond);
        commandLimit.setState(triggerType.getValue());
        //设置到储存库中
        Map<String, CommandLimit> tempMap = new HashMap<>();
        tempMap.put(CommandLimitRepository.createKey(limitGroupId,limitUserId,limitCommand), commandLimit);
        CommandLimitRepository.getInstance().setTempMap(tempMap);

        CommandExecTemp commandExecTemp;

        long startTime = System.currentTimeMillis() / 1000;
        Random random = new Random();

        //初始化测试用户
        long testUserId = (limitUserId == null || limitUserId == 0) ? Math.abs(random.nextLong()) : limitUserId;

        long testGroupId = (limitGroupId == null || limitGroupId == 0) ? Math.abs(random.nextLong()) : limitGroupId;
        boolean assignGroupAllUserLimit = limitGroupId != null && limitGroupId != 0 && (limitUserId == null || limitUserId == 0);
        boolean assignUserOnAllLimit = (limitGroupId == null || limitGroupId == 0) && (limitUserId != null && limitUserId != 0);
        boolean allUserLimit = (limitGroupId == null || limitGroupId == 0) && (limitUserId == null || limitUserId == 0);

        //指定用户在指定群
        System.out.println(commandLimit);
        overLimitTest(testUserId, testGroupId, limitCommand,limitNum,true, triggerType);
        //指定用户在其他群
        System.out.println(commandLimit);
        overLimitTest(testUserId, Math.abs(random.nextLong()), limitCommand,limitNum,allUserLimit || assignUserOnAllLimit, triggerType);
        //指定用户在私聊
        System.out.println(commandLimit);
        overLimitTest(testUserId, null, limitCommand,limitNum,allUserLimit || assignUserOnAllLimit, triggerType);

        //判断是否有全指令限制
        boolean allCommandLimit = "*".equals(limitCommand);
        //指定用户在其他群使用其他指令
        System.out.println(commandLimit);
        overLimitTest(testUserId, Math.abs(random.nextLong()), "other", limitNum, (assignUserOnAllLimit | allUserLimit) && allCommandLimit, triggerType);
        //指定用户在私聊使用其他指令
        System.out.println(commandLimit);
        overLimitTest(testUserId, null, "other",limitNum, (assignUserOnAllLimit | allUserLimit) && allCommandLimit, triggerType);

        //其他用户在指定群
        System.out.println(commandLimit);
        overLimitTest(Math.abs(random.nextLong()), limitGroupId, limitCommand,limitNum,assignGroupAllUserLimit || allUserLimit , triggerType);

        //其他用户在其他群
        System.out.println(commandLimit);
        overLimitTest(Math.abs(random.nextLong()), Math.abs(random.nextLong()), limitCommand,limitNum, allUserLimit, triggerType);

        //其他用户在私聊
        System.out.println(commandLimit);
        overLimitTest(Math.abs(random.nextLong()), null, limitCommand,limitNum, allUserLimit, triggerType);


        //等待超时时间后尝试
        long endTime = System.currentTimeMillis() / 1000;
        long testSecond = endTime - startTime;
        if (testSecond <= cycleSecond) {
            long waitSecond = cycleSecond - testSecond +1;
            System.out.println("新周期验证，将等待"+waitSecond+"秒后继续执行");
            Thread.sleep(waitSecond * 1000L);
        }
        commandExecTemp = commandLimitUtils.commandTryExec(limitUserId, limitGroupId, limitCommand);
        //不应该触发
        assertNull(commandExecTemp);
    }

    /**
     * 超限测试，用于方便的循环测试
     * @param userId 用户ID
     * @param groupId      群ID
     * @param command      执行指令
     * @param hasLimit     是否应当有限制
     * @param triggerType  限制类型
     */
    private void overLimitTest(Long userId, Long groupId, String command,int limitNum,boolean hasLimit,TriggerType triggerType) {
        CommandExecTemp commandExecTemp;

        for (int i = 1; i <= limitNum; i++) {
            commandLimitUtils.commandTryExec(userId, groupId, command);
            //应当通过
            assertEquals(1,commandLimitUtils.commandExecInterceptResult(userId, groupId, command),
                    "tryNum:"+i+"\n"+"error:"+userId+'-'+groupId+'-'+command
            );
            //模拟执行正常执行完成后累加一次
            commandExecTemp = commandLimitUtils.addCommandRecord(userId, groupId, command);
            assertEquals(i,commandExecTemp.getNumber());
        }
        if (hasLimit) {
            //第一次到达限制
            assertEquals(0, commandLimitUtils.commandExecInterceptResult(userId, groupId, command));

            //超限直接拒绝
            assertEquals(-1, commandLimitUtils.commandExecInterceptResult(userId, groupId, command));
        }
        //不应有限制
        else {
            //不应有限制
            assertEquals(1, commandLimitUtils.commandExecInterceptResult(userId, groupId, command));

            //不应有限制
            assertEquals(1, commandLimitUtils.commandExecInterceptResult(userId, groupId, command));
        }

    }
}