package com.github.cuteluobo.pojo;

import cn.pomit.mybatis.ProxyHandlerFactory;
import com.github.cuteluobo.enums.YysRoll;
import com.github.cuteluobo.mapper.YysUnitMapper;
import com.github.cuteluobo.model.YysUnit;

import java.lang.reflect.Field;
import java.util.*;

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
        List<RollResultUnit> rollResultUnitList = getRollResultUnitList();
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
            int rollResultUnitNumber = rollResultUnitList.size();
            int rollNumberTemp = rollResultUnitNumber / 10;
            if (!showAllLevel) {
                for (int i = 0; i < rollResultUnitList.size(); i++) {
                    //输出提示语,2021-11-19修改，前置提示语，避免先出结果后才显示提示的问题
                    String tipText = tempTipMap.get(i+1);
                    if (tipText != null) {
                        sb.append(tipText).append("\n");
                    }
                    //抽卡式神结果输出
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
            //抽卡成就结果输出，定向抽卡时累计抽取过多会导致刷屏
//            sb.append(checkRollAchievementString(rollResultUnitList));
        }
        return sb.toString();
    }

    /**生成抽卡成就相关
     * 记录成就出现抽次，（并显示详细式神）
     * 否极泰来-10次抽卡全为R
     * 接二连三-10次抽卡有至少3个SR
     * 五福临门-10次抽卡有至少5个SR
     * 福慧双至-10次抽卡有至少2个SSR
     * 眷顾：10次抽卡至少有2/3/4个相同式神
     * 非酋-初级/中级/高级/阴阳师/大阴阳师 200/300/400/500/600次抽卡没有SSR式神
     * */
    private String checkRollAchievementString(List<RollResultUnit> rollResultUnitList) {
        YysUnitMapper yysUnitMapper = ProxyHandlerFactory.getMapper(YysUnitMapper.class);
        if (rollResultUnitList == null || rollResultUnitList.size() == 0) {
            return "";
        }
        int listNumber = rollResultUnitList.size();
        //将抽取结果拆分为若干十连
        int tenRollNumber = listNumber / 10;
        //有余数时+1
        if (listNumber % 10 > 0) {
            tenRollNumber++;
        }
        //非酋统计相关，最后出货抽数
        int unluckyLastRollNumber = 0;
        //容量为10的缓存数组
        RollResultUnit[] rollTempList = new RollResultUnit[10];
        //抽出式神ID缓存
        long[] unitIdArray;
        String[] unitLevelArray;
        Set<Integer> rollIdSet;
        Set<String> rollLevelSet;
        Map<Integer, Integer> repeatUnitIdMap;
        Map<String, Integer> repeatUnitLevelMap;
        StringBuffer sb = new StringBuffer("抽卡成就：\n");
        for (int i = 0; i < tenRollNumber; i++) {
            List<RollResultUnit> tempList = rollResultUnitList.subList(i * 10, Math.min((i + 1) * 10, listNumber));
            //排查重复抽取ID
            rollIdSet = new HashSet<>(tempList.size());
            repeatUnitIdMap = new HashMap<>(tempList.size());
            //排查重复抽取阶级
            rollLevelSet = new HashSet<>(tempList.size());
            repeatUnitLevelMap = new HashMap<>(YysRoll.values().length);
            for (int j = 0; j < tempList.size(); j++) {
                RollResultUnit rollResultUnit = tempList.get(j);
                int rollUnitId = rollResultUnit.getId();
                String rollUnitLevel = rollResultUnit.getLevel();
                //放入抽取式神ID,可正常放入set时不重复，否则重复
                boolean repeatId =  !rollIdSet.add(rollUnitId);
                //重复时累加抽取次数，因重复，十连抽内抽取次数从2开始计算
                if (repeatId) {
                    Integer totalRollNumber = repeatUnitIdMap.get(rollUnitId);
                    if (totalRollNumber == null) {
                        totalRollNumber = 1;
                    }
                    totalRollNumber++;
                    repeatUnitIdMap.put(rollUnitId,totalRollNumber);
                }
                //放入抽取式神阶级,可正常放入set时不重复，否则重复
                boolean repeatLevel = !rollLevelSet.add(rollUnitLevel);
                //重复时累加抽取次数，因重复，十连抽内抽取次数从2开始计算
                //TODO 使用额外对象储存SSR的抽卡次数
                if (repeatLevel) {
                    Integer totalRollNumber = repeatUnitLevelMap.get(rollUnitLevel);
                    if (totalRollNumber == null) {
                        totalRollNumber = 1;
                    }
                    totalRollNumber++;
                    repeatUnitLevelMap.put(rollUnitLevel,totalRollNumber);
                }
                //当前检索抽取次数
                int nowRollNumber = i * 10 + j + 1;
                //当抽出SSR，或直到计算完成仍未抽出SSR时，计算非酋成就
                if (YysRoll.SSR.getLevel().equals(rollResultUnit.getLevel()) || (listNumber == nowRollNumber && unluckyLastRollNumber == 0)) {
                    //未出货的百抽次数
                    int unluckyHundredNumber = (nowRollNumber - unluckyLastRollNumber) / 100;
                    //SSR出货间隔大于等于两百抽=非酋
                    if (unluckyHundredNumber >= 2) {
                        sb.append("非酋-");
                        switch (unluckyHundredNumber) {
                            case 6:
                                sb.append("大阴阳师");
                                break;
                            case 5:
                                sb.append("阴阳师");
                                break;
                            case 4:
                                sb.append("高级");
                                break;
                            case 3:
                                sb.append("中级");
                                break;
                            default:
                                sb.append("初级");
                                break;
                        }
                        sb.append(" 于 ").append(unluckyLastRollNumber).append(" - ").append(nowRollNumber).append(" ，共").append(nowRollNumber - unluckyLastRollNumber).append("抽\n");
                    }
                    //赋值非酋起始抽卡数
                    unluckyLastRollNumber = nowRollNumber;
                }
            }
            //十连内重复2个以上式神时
            if (repeatUnitIdMap.size() > 0) {
                for (Map.Entry<Integer, Integer> entry :
                        repeatUnitIdMap.entrySet()) {
                    YysUnit unit = yysUnitMapper.selectOneByUnitId(entry.getKey());
                    //高抽取会导致R/SR结果刷屏，增加SSR/SP阶判定
                    if (unit != null && (YysRoll.SSR.equals(unit.getLevel()) || YysRoll.SP.equals(unit.getLevel()))) {
                        //示例：眷顾-重复式神： 名称 x数量
                        sb.append("眷顾-重复式神： ").append(unit.getName()).append(" x").append(entry.getValue()).append("\n");
                    }
                }
            }
            if (repeatUnitLevelMap.size() > 0) {
                for (Map.Entry<String, Integer> entry :
                        repeatUnitLevelMap.entrySet()) {
                    //SSR
                    if (YysRoll.SSR.getLevel().equals(entry.getKey())) {
                        sb.append("福慧双至-十抽内 ").append(entry.getValue()).append(" 个SSR\n");
                    }
                    //R
//                    if (YysRoll.R.getLevel().equals(entry.getKey()) && entry.getValue() == 10) {
//                        //示例：眷顾-重复式神： 名称 x数量
//                        sb.append("否极泰来-10次抽卡全为R");
//                    }
                }
            }
            //思考：因为抽取基数大，导致成就可能多次出现，考虑只保留SSR成就，或者使用单独对象储存相关的成就和抽取的式神列表，最后进行统一输出？
        }
        return sb.toString();
    }
}
