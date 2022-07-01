package com.github.cuteluobo.util;

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
}