package com.github.cuteluobo.util;

import com.github.cuteluobo.enums.TriggerType;
import com.github.cuteluobo.model.CommandLimit;
import com.github.cuteluobo.pojo.CommandExecTemp;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandLimitUtilsTest {
    private static CommandLimitUtils commandLimitUtils;

    @BeforeAll
    public static void setUp() {
        commandLimitUtils = CommandLimitUtils.getInstance();
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
    void commandVerify() {
        //初始化测试变量
        long userId = 1;
        long groupId = 10000;
        String command = "test";
        TriggerType triggerType = TriggerType.IGNORE_TEN_MINUTE;

        //初始化一个600秒内只能执行2次的指令限制
        CommandLimit commandLimit = new CommandLimit();
        commandLimit.setCycleNum(2);
        commandLimit.setCycleSecond(600);
        commandLimit.setState(triggerType.getValue());

        CommandExecTemp commandExecTemp = commandLimitUtils.commandVerify(userId, groupId, command, commandLimit);
        //未添加执行记录时，不会有结果
        assertNull(commandExecTemp);
        //执行一次
        commandLimitUtils.addCommandRecord(userId, groupId, command);
        commandExecTemp = commandLimitUtils.commandVerify(userId, groupId, command, commandLimit);
        //有结果但不会触发条件
        assertNotNull(commandExecTemp);
        assertNull(commandExecTemp.getTrigger());

        //执行第二次
        commandLimitUtils.addCommandRecord(userId, groupId, command);
        //在第三次执行之前验证会超限
        commandExecTemp = commandLimitUtils.commandVerify(userId, groupId, command, commandLimit);
        //有结果并触发条件
        assertNotNull(commandExecTemp);
        assertEquals(commandExecTemp.getTrigger(), triggerType);
    }
}