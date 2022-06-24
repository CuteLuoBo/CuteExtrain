package com.github.cuteluobo.service.Impl;


import cn.pomit.mybatis.configuration.MybatisConfiguration;
import com.github.cuteluobo.CuteExtra;
import com.github.cuteluobo.enums.YysRoll;
import com.github.cuteluobo.mapper.YysUnitMapper;
import com.github.cuteluobo.pojo.RollResultData;
import com.github.cuteluobo.pojo.RollResultUnit;
import com.github.cuteluobo.pojo.RollUnit;
import com.github.cuteluobo.util.YysRollUtils;
import org.apache.ibatis.session.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 阴阳师抽卡测试类
 */
class YysRollServiceImplTest {

    private static YysRollServiceImpl yysRollService;

    /**
     * 初始化数据库配置文件
     */
    private static void setUpDataBase() {
        Properties properties = new Properties();
        properties.put("mybatis.mapper.scan", "com.github.cuteluobo.mapper");
        properties.put("mybatis.datasource.type", "POOLED");
        properties.put("mybatis.datasource.driver", "org.sqlite.JDBC");
        URL url = YysRollServiceImplTest.class.getClassLoader().getResource("database.sqlite");
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
        yysRollService = YysRollServiceImpl.INSTANCE;
        System.out.println("========= YysRollServiceImplTest =========");
    }

    /**
     * 普通抽卡
     */
    @Test
    public void normalRoll() {
        int rollNum = 1000;
        System.out.println("------- normalRoll x "+rollNum+" ------");

        RollResultData rollResultData = yysRollService.roll(rollNum, null);
        //验证生成
        assertNotNull(rollResultData);
        //验证生成数量
        assertEquals(rollResultData.getRollNum(), rollNum);
        //验证结果数量正常
        List<RollResultUnit> rollResultUnitList = rollResultData.getRollResultUnitList();
        assertEquals(rollResultUnitList.size(), rollNum);
        //验证结果没有up
        assertEquals(rollResultUnitList.stream().filter(unit -> Boolean.TRUE.equals(unit.getUp())).count(),0);
        //验证概率-未找到相对较好的验证方法，略过
//        Map<YysRoll, Integer> checkRollMap = YysRollUtils.checkLevelNum(rollResultUnitList);
//        assertTrue(checkRollMap.get(YysRoll.N) <= Math.round(YysRoll.N.getRollProb() * rollNum) * 2);
//        assertTrue(checkRollMap.get(YysRoll.R) <= Math.round(YysRoll.R.getRollProb() * rollNum) * 2);
//        assertTrue(checkRollMap.get(YysRoll.SR) <= Math.round(YysRoll.SR.getRollProb() * rollNum) * 2);
//        assertTrue(checkRollMap.get(YysRoll.SSR) <= Math.round(YysRoll.SSR.getRollProb() * rollNum) * 2);
//        assertTrue(checkRollMap.get(YysRoll.SP) <= Math.round(YysRoll.SP.getRollProb() * rollNum) * 2);
    }

    /**
     * 普通Up抽卡
     */
    @Test
    public void normalUpRoll() {
        int rollNum = 100;
        int upNum = 3;
        float upRate = 2.5f;
        System.out.println("------- normalUpRoll x "+rollNum+" ------");

        RollResultData rollResultData = yysRollService.rollUp(rollNum, Boolean.TRUE, upNum, upRate);
        //验证生成
        assertNotNull(rollResultData);
        //验证生成数量
        assertEquals(rollResultData.getRollNum(), rollNum);
        //验证结果数量正常
        List<RollResultUnit> rollResultUnitList = rollResultData.getRollResultUnitList();
        assertEquals(rollResultUnitList.size(), rollNum);
        //验证up数量正常
//        rollResultData.getWinResultUnitList().forEach(System.out::println);
        assertTrue(rollResultUnitList.stream().filter(unit -> Boolean.TRUE.equals(unit.getUp())).count() <= upNum);
        assertTrue(rollResultData.getWinResultUnitList().stream().filter(unit -> Boolean.TRUE.equals(unit.getUp())).count() <= upNum);
    }

    /**
     * 活动定向式神抽卡
     */
    @Test
    public void specifyRoll() {
        RollUnit testUnit = new RollUnit(0, 200L, YysRoll.SP.getLevel(), "test");
        System.out.println("------- specifyRoll x "+testUnit.getLevel()+'-'+testUnit.getName()+" ------");
        //全图鉴
        RollResultData rollResultDataByFull = yysRollService.rollTextForSpecifyUnit(testUnit, true);
        //验证生成
        assertNotNull(rollResultDataByFull);
        //验证必定出货
        List<RollResultUnit> winList = rollResultDataByFull.getWinResultUnitList();
        assertNotNull(winList);
        assertFalse(winList.isEmpty());
        //最后一个出货的必定为指定式神
        assertEquals(testUnit.getId(), winList.get(winList.size() - 1).getId());

        //非全图鉴
        RollResultData rollResultData = yysRollService.rollTextForSpecifyUnit(testUnit, false);
        //验证生成
        assertNotNull(rollResultData);
        //验证必定出货
        List<RollResultUnit> winList2 = rollResultData.getWinResultUnitList();
        assertNotNull(winList2);
        assertFalse(winList2.isEmpty());
        //最后一个出货的必定为指定式神
        assertEquals(testUnit.getId(), winList2.get(winList2.size() - 1).getId());
    }

    /**
     * 普通定向式神抽卡
     */
    @Test
    public void assignRoll() {
        RollUnit testUnit = (RollUnit) YysRollServiceImpl.INSTANCE.getRollUnitMap().get(YysRoll.SSR.getLevel()).values().toArray()[0];
        System.out.println("------- assignRoll x "+testUnit.getLevel()+'-'+testUnit.getName()+" ------");
        //UP概率抽取
        RollResultData rollResultDataByUp = yysRollService.rollTextForAssignUnit(testUnit, true);
        //验证生成
        assertNotNull(rollResultDataByUp);
        //验证必定出货
        List<RollResultUnit> winList = rollResultDataByUp.getWinResultUnitList();
        assertNotNull(winList);
        assertFalse(winList.isEmpty());
        //最后一个出货的必定为指定式神
        assertEquals(testUnit.getId(), winList.get(winList.size() - 1).getId());

        //无UP抽取
        RollResultData rollResultData = yysRollService.rollTextForAssignUnit(testUnit, false);
        //验证生成
        assertNotNull(rollResultData);
        //验证必定出货
        List<RollResultUnit> winList2 = rollResultData.getWinResultUnitList();
        assertNotNull(winList2);
        assertFalse(winList2.isEmpty());
        //最后一个出货的必定为指定式神
        assertEquals(testUnit.getId(), winList2.get(winList2.size() - 1).getId());

    }


}