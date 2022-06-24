package com.github.cuteluobo.util;

import com.github.cuteluobo.enums.YysRoll;
import com.github.cuteluobo.pojo.RollResultUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 阴阳师抽卡工具类测试
 */
class YysRollUtilsTest {

    private List<RollResultUnit> testUnitList;
    //测试变量
    private int spNum = 2;
    private int ssrNum = 8;
    private int srNum = 30;
    private int rNum = 60;
    private int nNum = 0;
    private int totalNum = spNum + ssrNum + srNum + rNum + nNum;
    private BigDecimal totalNumBigDecimal = BigDecimal.valueOf(totalNum);
    private BigDecimal spRate = BigDecimal.valueOf(spNum).divide(totalNumBigDecimal, 4, RoundingMode.CEILING);
    private BigDecimal ssrRate = BigDecimal.valueOf(ssrNum).divide(totalNumBigDecimal, 4, RoundingMode.CEILING);
    private BigDecimal srRate = BigDecimal.valueOf(srNum).divide(totalNumBigDecimal, 4, RoundingMode.CEILING);
    private BigDecimal rRate = BigDecimal.valueOf(rNum).divide(totalNumBigDecimal, 4, RoundingMode.CEILING);
    private BigDecimal nRate = BigDecimal.valueOf(nNum).divide(totalNumBigDecimal, 4, RoundingMode.CEILING);

    @BeforeEach
    public void setUp() {
        System.out.println("====== YysRollUtilsTest ======");
        //填充测试数组
        testUnitList = new ArrayList<>(totalNum);
        testUnitList.addAll(Stream.generate(() -> new RollResultUnit(0,1L,YysRoll.SP.getLevel(),"test")).limit(spNum).collect(Collectors.toList()));
        testUnitList.addAll(Stream.generate(() -> new RollResultUnit(0,1L,YysRoll.SSR.getLevel(),"test")).limit(ssrNum).collect(Collectors.toList()));
        testUnitList.addAll(Stream.generate(() -> new RollResultUnit(0,1L,YysRoll.SR.getLevel(),"test")).limit(srNum).collect(Collectors.toList()));
        testUnitList.addAll(Stream.generate(() -> new RollResultUnit(0,1L,YysRoll.R.getLevel(),"test")).limit(rNum).collect(Collectors.toList()));
        testUnitList.addAll(Stream.generate(() -> new RollResultUnit(0,1L,YysRoll.N.getLevel(),"test")).limit(nNum).collect(Collectors.toList()));
    }

    /**
     * 验证统计方法
     */
    @Test
    public void checkLevelNum() {
        System.out.println("----- checkLevelNum start -----");
        Map<YysRoll, Integer> yysRollIntegerMap = YysRollUtils.checkLevelNum(testUnitList);
        assertEquals(yysRollIntegerMap.get(YysRoll.SP), spNum);
        assertEquals(yysRollIntegerMap.get(YysRoll.SSR), ssrNum);
        assertEquals(yysRollIntegerMap.get(YysRoll.SR), srNum);
        assertEquals(yysRollIntegerMap.get(YysRoll.R), rNum);
        assertEquals(yysRollIntegerMap.get(YysRoll.N), nNum);
        System.out.println("验证统计数值通过");
        System.out.println("----- checkLevelNum end -----");
    }

    /**
     * 验证统计方法
     */
    @Test
    public void checkLevelRate() {
        System.out.println("----- checkLevelRate start -----");
        Map<YysRoll, BigDecimal> yysRollBigDecimalMap = YysRollUtils.checkLevelRate(testUnitList);
        assertEquals(spRate.compareTo(yysRollBigDecimalMap.get(YysRoll.SP)), 0);
        assertEquals(ssrRate.compareTo(yysRollBigDecimalMap.get(YysRoll.SSR)), 0);
        assertEquals(srRate.compareTo(yysRollBigDecimalMap.get(YysRoll.SR)), 0);
        assertEquals(rRate.compareTo(yysRollBigDecimalMap.get(YysRoll.R)), 0);
        assertEquals(nRate.compareTo(yysRollBigDecimalMap.get(YysRoll.N)), 0);
        System.out.println("验证统计概率通过");
        System.out.println("----- checkLevelRate end -----");
    }
}