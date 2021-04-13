package com.github.cuteluobo.service.Impl;

import cn.pomit.mybatis.ProxyHandlerFactory;
import com.github.cuteluobo.Pojo.*;
import com.github.cuteluobo.service.ExpandRollService;
import com.github.cuteluobo.enums.YysRoll;
import com.github.cuteluobo.mapper.YysUnitMapper;
import com.github.cuteluobo.model.YysUnit;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author CuteLuoBo
 * @date 2021-04-07
 */
public class YysRollServiceImpl implements ExpandRollService {

    public final static YysRollServiceImpl INSTANCE = new YysRollServiceImpl();
    /**默认UP倍率*/
    public final float UP_RATE = 2.5f;
    /**SSR抽出SP皮肤概率*/
    public final float SSR_SKIN_RATE = 0.1f;
    /**抽出高阶式神概率（SSR+SP）*/
    private final float NORMAL_HIGH_LEVEL_RATE = YysRoll.SSR.getRollProb()+YysRoll.SP.getRollProb();
    private YysUnitMapper yysUnitMapper;
    private Map<String, Map<Integer,RollUnit>> rollUnitMap = new HashMap<>();

    private YysRollServiceImpl(){
       yysUnitMapper = ProxyHandlerFactory.getMapper(YysUnitMapper.class);
       //填充卡池式神数据
        //TODO 配置Mybatis日志打印并排除无查询数据问题
        List<YysUnit> yysUnitList = yysUnitMapper.selectList(true);
        if (yysUnitList != null) {
            for (YysUnit yysUnit :
                    yysUnitList) {
                Map<Integer,RollUnit> levelRollUnitMap = rollUnitMap.get(yysUnit.getLevel());
                //获取已有阶级式神列表，为null时进行初始化
                if (levelRollUnitMap == null) {
                    levelRollUnitMap = new HashMap<>();
                    rollUnitMap.put(yysUnit.getLevel(), levelRollUnitMap);
                }
                levelRollUnitMap.put(yysUnit.getUnitId(), new RollUnit(yysUnit));
            }
//            List<RollUnit> rollUnitList = yysUnitList.stream().map(RollUnit::new).collect(Collectors.toList());
//            for (RollUnit rollUnit : rollUnitList) {
//
//                Map<Integer,RollUnit> levelRollUnitMap = rollUnitMap.get(rollUnit.getLevel());
//                if (levelRollUnitMap == null) {
//                    levelRollUnitMap = new HashMap<>();
//                    rollUnitMap.put(rollUnit.getLevel(), levelRollUnitMap);
//                }
//                //对引用对象操作应该可以存进map里？
//                levelRollUnitMap.put(rollUnit.getId(), rollUnit);
//            }
        }
    }


    /**
     * 抽取文字结果
     *
     * @param rollNum  抽取次数
     * @param up       是否启用概率up
     * @param upRate   up倍率
     * @param upNum    up次数
     * @param winProb 高阶抽取概率，为null时使用默认概率
     * @return 抽取结果
     */
    @Override
    public RollResultData rollText(Integer rollNum, Boolean up, Float upRate, Integer upNum, Float winProb) {
        //初始返回数据对象
        List<RollResultUnit> rollResultUnitList = new ArrayList<>();

        RollResultData rollResultData = new YysRollResultData();
        rollResultData.setRollNum(rollNum);
        //未定义up倍数时手动添加
        if (upRate == null) {
            upRate = this.UP_RATE;
        }
        if (upNum == null) {
            upNum = up?3:0;
        }else {
            upNum = up?upNum:0;
        }
        for (int i = 1; i <= rollNum; i++) {
            String rollLevel = rollLevel(up, upRate);
            RollUnit rollUnit = startRollUnit(rollUnitMap.get(rollLevel));
            if (rollUnit != null) {
                RollResultUnit rollResultUnit = new RollResultUnit (rollUnit);
                //为SSR阶时抽取皮肤
                if (YysRoll.SSR.getLevel().equals(rollResultUnit.getLevel()) && rollUnit.getSpecialName() != null) {
                    rollResultUnit.setSpecial(rollSsrSkin());
                }
                rollResultUnit.setSequence(i);
                rollResultUnitList.add(rollResultUnit);
                if (YysRoll.SSR.getLevel().equals(rollResultUnit.getLevel()) || YysRoll.SP.getLevel().equals(rollResultUnit.getLevel())) {
                    rollResultUnit.setUp(up);
                    //出货时减去Up次数
                    up = upNum-- > 0;
                }
            }
        }
        rollResultData.setRollUnitList(rollResultUnitList);
        return rollResultData;
    }

    /**内部抽卡类
     * @param up     是否启用概率up
     * @param upRate up时概率
     * @return*/
    private String rollLevel(Boolean up, Float upRate){
        Random random = new Random();
        //随机数
        Float randomNum = random.nextFloat();
        //用于判断随机数落值的缓存
        Float temp = 0.0f;
        //提取默认概率
        Map<String, Float> levelRollProbMap = new HashMap<>(YysRoll.values().length);
        for (YysRoll y: YysRoll.values()) {
            levelRollProbMap.put(y.getLevel(), y.getRollProb());
        }
        //UP时提升概率
        if (up) {
            float ssrRate = levelRollProbMap.get(YysRoll.SSR.getLevel()) * upRate;
            float spRate = levelRollProbMap.get(YysRoll.SP.getLevel()) * upRate;
            float rRate = levelRollProbMap.get(YysRoll.R.getLevel());
            //当概率提升超过R阶概率时，进行概率比例分配，否则减少出现R阶概率
            if (ssrRate + spRate > YysRoll.R.getRollProb()) {
                rRate = 0.0f;
                //剩余可用概率
                float overRate = 1.0F - YysRoll.SR.getRollProb();
                ssrRate = overRate * (YysRoll.SSR.getRollProb()%NORMAL_HIGH_LEVEL_RATE);
                spRate = overRate * (YysRoll.SP.getRollProb()%NORMAL_HIGH_LEVEL_RATE);
            }else{
                rRate -= (ssrRate + spRate) - NORMAL_HIGH_LEVEL_RATE;
            }
            levelRollProbMap.put(YysRoll.SSR.getLevel(), ssrRate);
            levelRollProbMap.put(YysRoll.SP.getLevel(), spRate);
            levelRollProbMap.put(YysRoll.R.getLevel(), rRate);
        }
        //实际抽卡
        for (Map.Entry<String, Float> entry :
                levelRollProbMap.entrySet()) {
            temp += entry.getValue();
            if (randomNum <= temp) {
                return entry.getKey();
            }
        }
        //保底？
        return YysRoll.SP.getLevel();
    }

    /**
     * 随机抽取可参考 https://bbs.nga.cn/read.php?tid=8477978 #4楼
     * @param levelRollUnitMap 卡池
     * @return 抽取结果
     */
    private RollUnit startRollUnit(Map<Integer,RollUnit> levelRollUnitMap){
        if (levelRollUnitMap != null) {
            Random random = new Random();
            int rollUnitTemp = random.nextInt(levelRollUnitMap.size());
            //map中储存的是ID与式神对应关系，需要转成取List/Array对应Index对象
            return (RollUnit) levelRollUnitMap.values().toArray()[rollUnitTemp];
        }
        return null;
    }

    /**
     * 抽取SSR皮肤(10%)
     * @return 是否抽中
     */
    private Boolean rollSsrSkin() {
        return Math.random() < SSR_SKIN_RATE;
    }

    /**
     * 抽取图片结果
     *
     * @param rollNum  抽取次数
     * @param up       是否启用概率up
     * @param upRate   up倍率
     * @param upNum    up
     * @param winProb 高阶抽取概率，为null时使用默认概率
     * @return 抽取结果
     */
    @Override
    public RollImgResult rollImg(Integer rollNum, Boolean up, Float upRate, Integer upNum, Float winProb) {
        return null;
    }


    /**
     * 抽取指定对象
     * @param rollUnit 指定概率UP式神对象
     * @param fullBuff   是否启用全图鉴加成
     * @return 抽取数据
     */
    @Override
    public RollResultData rollTextForSpecifyUnit(@NotNull RollUnit rollUnit,@NotNull Boolean fullBuff) {
        //初始返回数据对象
        RollResultData rollResultData = new YysRollResultData();
        List<RollResultUnit> rollResultUnitList = new ArrayList<>();
        Map<Integer, String> tipMap = new HashMap<>();
        //当前指定抽取概率
        BigDecimal rollProb = new BigDecimal(0);
        //最大概率
        BigDecimal maxProb = new BigDecimal(0);
        //非全图到达700抽后的最终概率
        BigDecimal noFullLastMaxProb = new BigDecimal(0);
        //阶梯提升数量
        int stepUpNum = 50;
        //阶梯提升概率
        Map<Integer,BigDecimal> stepUpProb = new LinkedHashMap<>();
        //700抽前最大概率抽卡数
        int normalMaxProbNum = 500;
        //非全图保底（必出）抽卡数
        int maxRollNum = 700;
        String upUnitLevel = rollUnit.getLevel();
        /*SSR概率初始化：
            TODO SSR阶梯提升相关概率未探明
         * 全图鉴：初始15%持续提升并在500次召唤后提升至100%
         * 非全图鉴：初始的4%持续提升并在500次召唤后提升至20%
         */
        if (YysRoll.SSR.getLevel().equals(upUnitLevel)) {
            if (fullBuff) {
                rollProb = new BigDecimal("0.15");
                maxProb = new BigDecimal("1.0");
                stepUpProb.put(0, new BigDecimal("0.05"));
                stepUpProb.put(300, new BigDecimal("0.1"));
                stepUpProb.put(400,new BigDecimal("0.2"));
            }else{
                rollProb = new BigDecimal("0.04");
                maxProb = new BigDecimal("0.2");
                stepUpProb.put(0, new BigDecimal("0.01"));
                stepUpProb.put(150, new BigDecimal("0.02"));
            }
            noFullLastMaxProb = new BigDecimal("0.3");
        }
        /*SP概率初始化：
         * 全图鉴：初始10%持续提升并在500次召唤后提升至100%
         * 非全图鉴：初始的3%持续提升并在500次召唤后提升至15%
         */
        else if (YysRoll.SP.getLevel().equals(upUnitLevel)) {
            if (fullBuff) {
                rollProb = new BigDecimal("0.10");
                maxProb = new BigDecimal("1.0");
                stepUpProb.put(0, new BigDecimal("0.05"));
                stepUpProb.put(300, new BigDecimal("0.1"));
                stepUpProb.put(400, new BigDecimal("0.2"));
            }else{
                rollProb = new BigDecimal("0.03");
                maxProb = new BigDecimal("0.15");
                stepUpProb.put(0, new BigDecimal("0.01"));
                stepUpProb.put(150, new BigDecimal("0.02"));
                stepUpProb.put(300, new BigDecimal("0.01"));
            }
            noFullLastMaxProb = new BigDecimal("0.25");
        }
        BigDecimal upProb ;
        if (stepUpProb.get(0) != null) {
            upProb = stepUpProb.get(0);
        }else {
            upProb = new BigDecimal(0);
        }
        //抽卡次数
        int rollNum = 1;
        //概率up
        boolean up = true;
        int upNum = 3;
        //在对应阶级卡池中移除指定概率UP式神，后续进行额外抽取
        Map<Integer,RollUnit> levelUnitMap =  new HashMap<>(10);
        levelUnitMap.putAll(rollUnitMap.get(upUnitLevel));
        levelUnitMap.remove(rollUnit.getId());
        //抽卡开始
        while (true) {
            RollResultUnit rollResultUnit = null;
            String rollLevel = rollLevel(up, UP_RATE);
            //抽卡次数达到时50的倍数，概率提升
            if (rollNum % stepUpNum == 0) {
                BigDecimal tempProb = stepUpProb.get(rollNum);
                if (tempProb != null) {
                    upProb = tempProb;
                }
                if (rollNum <= normalMaxProbNum) {
                    rollProb = rollProb.add(upProb).min(maxProb);
                    tipMap.put(rollNum, "指定UP概率提升至：" + rollProb.scaleByPowerOfTen(2).toString()+ "%");
                }
                //非全图抽到700抽时，进一步提升概率
                if (!fullBuff && rollNum == maxRollNum) {
                    rollProb = noFullLastMaxProb;
                    tipMap.put(rollNum, "指定UP概率提升至："+rollProb.scaleByPowerOfTen(2).toString()+"%");
                }
            }
            //抽出高阶式神时
            if (YysRoll.SSR.getLevel().equals(rollLevel) ||  YysRoll.SP.getLevel().equals(rollLevel)){
                //出货时减去Up次数
                up = upNum-- > 0;
                //先进行指定式神概率UP判定
                if (Math.random() <= rollProb.doubleValue()) {
                    rollResultUnit = new RollResultUnit(rollUnit);
                    rollResultUnit.setSequence(rollNum);
                    rollResultUnit.setUp(up);
                    rollResultUnitList.add(rollResultUnit);
                    //为SSR时抽取皮肤
                    if (YysRoll.SSR.getLevel().equals(rollResultUnit.getLevel()) && rollUnit.getSpecialName() != null) {
                        rollResultUnit.setSpecial(rollSsrSkin());
                    }
                    break;
                }else {
                    rollResultUnit = new RollResultUnit(startRollUnit(levelUnitMap));
                    rollResultUnit.setUp(up);
                }
                //为SSR时抽取皮肤
                if (YysRoll.SSR.getLevel().equals(rollResultUnit.getLevel()) && rollUnit.getSpecialName() != null) {
                    rollResultUnit.setSpecial(rollSsrSkin());
                }
            }else {
                rollResultUnit = new RollResultUnit (startRollUnit(rollUnitMap.get(rollLevel)));
            }
            if (rollResultUnit != null) {
                rollResultUnit.setSequence(rollNum);
                rollResultUnitList.add(rollResultUnit);
            }
            rollNum++;
            //保底时
            if (fullBuff&&rollNum==maxRollNum){
                rollResultUnit = new RollResultUnit(rollUnit);
                rollResultUnit.setSequence(rollNum);
                rollResultUnit.setUp(up);
                rollResultUnitList.add(rollResultUnit);
                break;
            }
        }
        //输出结果
        rollResultData.setRollUnitList(rollResultUnitList);
        rollResultData.setRollNum(rollNum);
        rollResultData.setTipMap(tipMap);
        return rollResultData;
    }

    public Map<String, Map<Integer, RollUnit>> getRollUnitMap() {
        return rollUnitMap;
    }
}
