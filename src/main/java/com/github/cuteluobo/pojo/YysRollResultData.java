package com.github.cuteluobo.pojo;

import com.github.cuteluobo.enums.YysRoll;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author CuteLuoBo
 * @date 2021-04-13
 */
public class YysRollResultData extends RollResultData {

    /**
     * 打印抽取结果
     * @param showAllLevel 是否显示所有抽卡结果(低阶仅统计数量)
     * @param showAllTip 是否显示所有提示
     * @return
     */
    @Override
    public String printResultText(boolean showAllLevel, boolean showAllTip){
        final StringBuffer sb = new StringBuffer();
        List<RollResultUnit> rollResultUnitList = getRollUnitList();
        //创建map副本
        Map<Integer,String> tempTipMap = new LinkedHashMap<>(getTipMap());
        //不显示全部提示时，获取最后一个提示
        if (!showAllTip && tempTipMap.size() > 0) {
            //使用反射获取最后一个提示，清空原提示数组后保留最后一个（JDK9后有反射警告，换成另一种方法）
//            try {
//                Field tail = tempTipMap.getClass().getDeclaredField("tail");
//                tail.setAccessible(true);
//                Object tempEntry = tail.get(tempTipMap);
//                if (tempEntry instanceof Map.Entry<?,?>) {
//                    Map.Entry<?, ?> entry = (Map.Entry<?, ?>) tempEntry;
//                    if (entry.getKey() instanceof Integer && entry.getValue() instanceof String) {
//                        tempTipMap.clear();
//                        tempTipMap.put((Integer) entry.getKey(), (String) entry.getValue());
//                    }
//                }
//            } catch (IllegalAccessException | NoSuchFieldException ignored) {
//
//            }
            //转为数组后读取最后一个，参考https://stackoverflow.com/questions/50161624/accesing-a-method-in-the-last-object-in-a-linkedhashmap
            //参考2：https://stackoverflow.com/questions/1936462/java-linkedhashmap-get-first-or-last-entry
            Integer key = tempTipMap.keySet().toArray(new Integer[0])[tempTipMap.size() - 1];
            String value = tempTipMap.get(key);
            tempTipMap.clear();
            tempTipMap.put(key,value);
        }
        if (rollResultUnitList==null || rollResultUnitList.size()==0){
            sb.append("无事发生");
        }else {
            int rLevelNum = 0;
            int srLevelNum = 0;
            if (!showAllLevel) {
                for (int i = 0; i < rollResultUnitList.size(); i++) {
                    RollResultUnit rollResultUnit = rollResultUnitList.get(i);
                    String level = rollResultUnit.getLevel();
                    //从概率高的阶级进行判断
                    if (YysRoll.R.getLevel().equals(level)) {
                        rLevelNum++;
                    }else if (YysRoll.SR.getLevel().equals(level)) {
                        srLevelNum++;
                    }else {
                        sb.append(rollResultUnit).append("\n");
                    }
                    //输出提示语
                    String tipText = tempTipMap.get(i+1);
                    if (tipText != null) {
                        sb.append(tipText).append("\n");
                    }
                }
                sb.append("\n");
                sb.append(YysRoll.SR.getLevel()).append("阶数量：").append(srLevelNum).append("\n");
                sb.append(YysRoll.R.getLevel()).append("阶数量：").append(rLevelNum).append("\n");
            }else {
                for (RollResultUnit rollResultUnit : rollResultUnitList) {
                    sb.append(rollResultUnit).append("\n");
                    String tipText = tempTipMap.get(rollResultUnit.getSequence());
                    if (tipText != null) {
                        sb.append(tipText).append("\n");
                    }
                }
            }
        }
        return sb.toString();
    }
}
