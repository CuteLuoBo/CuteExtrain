package com.github.cuteluobo.util;

import com.github.cuteluobo.enums.YysRoll;
import com.github.cuteluobo.pojo.RollResultData;
import com.github.cuteluobo.pojo.RollResultUnit;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author CuteLuoBo
 * @date 2022/6/19 12:44
 */
public class YysRollUtils {
    /**
     * 统计抽出的各阶数量
     * @param rollResultUnitList 需要验证的数组
     * @return 结果Map
     */
    public static Map<YysRoll, Integer> checkLevelNum(List<RollResultUnit> rollResultUnitList) {
        Iterator<RollResultUnit> iterator = rollResultUnitList.iterator();
        int sp = 0, ssr = 0, sr = 0, r = 0,n = 0;
        while (iterator.hasNext()) {
            RollResultUnit rollResultUnit = iterator.next();
            switch (YysRoll.valueOf(rollResultUnit.getLevel())) {
                case R:
                    r++;break;
                case SR:
                    sr++;break;
                case SSR:
                    ssr++;break;
                case SP:
                    sp++;break;
                default:
                    n++;
            }
        }
        Map<YysRoll, Integer> yysRollTotalMap = new HashMap<>(YysRoll.values().length);
        yysRollTotalMap.put(YysRoll.N, n);
        yysRollTotalMap.put(YysRoll.R, r);
        yysRollTotalMap.put(YysRoll.SR, sr);
        yysRollTotalMap.put(YysRoll.SSR, ssr);
        yysRollTotalMap.put(YysRoll.SP, sp);
        return yysRollTotalMap;
    }

    /**
     * 统计抽出的各阶概率
     * @param rollResultUnitList 需要验证的数组
     * @return 结果Map
     */
    public static Map<YysRoll, BigDecimal> checkLevelRate(List<RollResultUnit> rollResultUnitList) {
        //生成结果
        Map<YysRoll, Integer> checkRollMap = checkLevelNum(rollResultUnitList);

        //初始化变量
        BigDecimal total = BigDecimal.valueOf(rollResultUnitList.size());

        return checkRollMap.entrySet().stream()
                .map(entry ->
                        new AbstractMap.SimpleEntry<>(
                                entry.getKey(),
                                BigDecimal
                                        .valueOf(entry.getValue())
                                        .divide(total, 4, RoundingMode.CEILING)
                        ))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static RollResultData generateTestData(int spNum, int ssrNum, int srNum, int rNum, int nNum) {
        RollResultData rollResultData = new RollResultData();
        int totalNum = spNum + ssrNum + srNum + rNum + nNum;
        //填充测试数组
        ArrayList<RollResultUnit> rollResultUnits = new ArrayList<>(totalNum);
        rollResultUnits.addAll(Stream.generate(() ->  {
            RollResultUnit temp = new RollResultUnit(200,1L, YysRoll.SP.getLevel(), "test");
            temp.setOfficialId(200L);
            return temp;}).limit(spNum).collect(Collectors.toList()));
        rollResultUnits.addAll(Stream.generate(() -> {
            RollResultUnit temp = new RollResultUnit(200,1L, YysRoll.SSR.getLevel(), "test");
            temp.setOfficialId(200L);
            return temp;}).limit(ssrNum).collect(Collectors.toList()));
        rollResultUnits.addAll(Stream.generate(() -> {
            RollResultUnit temp = new RollResultUnit(200,1L, YysRoll.SR.getLevel(), "test");
            temp.setOfficialId(200L);
            return temp;}).limit(srNum).collect(Collectors.toList()));
        rollResultUnits.addAll(Stream.generate(() -> {
            RollResultUnit temp = new RollResultUnit(200,1L, YysRoll.R.getLevel(), "test");
            temp.setOfficialId(200L);
            return temp;}).limit(rNum).collect(Collectors.toList()));
        rollResultUnits.addAll(Stream.generate(() -> {
            RollResultUnit temp = new RollResultUnit(200,1L, YysRoll.N.getLevel(), "test");
            temp.setOfficialId(200L);
            return temp;}).limit(nNum).collect(Collectors.toList()));
        rollResultData.setRollResultUnitList(rollResultUnits);

        ArrayList<RollResultUnit> winResultUnits = new ArrayList<>(spNum + ssrNum);
        winResultUnits.addAll(Stream.generate(() -> {
            RollResultUnit temp = new RollResultUnit(200,1L, YysRoll.SP.getLevel(), "test");
            temp.setOfficialId(200L);
            return temp;}).limit(spNum).collect(Collectors.toList()));
        winResultUnits.addAll(Stream.generate(() -> {
            RollResultUnit temp = new RollResultUnit(200,1L, YysRoll.SSR.getLevel(), "test");
            temp.setOfficialId(200L);
            return temp;}).limit(ssrNum).collect(Collectors.toList()));
        rollResultData.setRollNum(totalNum);
        rollResultData.setWinResultUnitList(winResultUnits);
        return rollResultData;
    }

}
