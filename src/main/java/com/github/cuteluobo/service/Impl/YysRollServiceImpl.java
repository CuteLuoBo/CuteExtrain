package com.github.cuteluobo.service.Impl;

import cn.pomit.mybatis.ProxyHandlerFactory;
import com.github.cuteluobo.pojo.*;
import com.github.cuteluobo.service.ExpandRollService;
import com.github.cuteluobo.enums.YysRoll;
import com.github.cuteluobo.mapper.YysUnitMapper;
import com.github.cuteluobo.model.YysUnit;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.*;

/**
 * 阴阳师抽卡实现类
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
        List<YysUnit> yysUnitList = yysUnitMapper.selectListByCanRoll(true);
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
        }
    }




    /**
     * 抽取对象等级
     * @param up     是否启用概率up
     * @param upRate Up时提升的倍数
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
                float overRate = 1.0f - YysRoll.SR.getRollProb();
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
     * 根据指定胜率抽取等级
     * @param winRate  胜率
     * @return 抽取等级
     */
    private String rollLevel(float winRate) {
        //分解SR概率，剩余
        float lowRate = 1.0f - winRate;
        float srRate = lowRate < YysRoll.SR.getRollProb() ? lowRate : YysRoll.SR.getRollProb();

        //抽取
        Random random = new Random();
        float rollNum = random.nextFloat();
        //出货
        if (rollNum < winRate) {
            //对SSR：SP以4:1比例分配
            return random.nextInt(5) < 4 ? YysRoll.SSR.getLevel() : YysRoll.SP.getLevel();
        }
        //抽出SR
        else if (rollNum < winRate + srRate) {
            return YysRoll.SR.getLevel();
        }
        //剩下为R
        else {
            return YysRoll.R.getLevel();
        }
    }

    /**
     * 从卡池内随机抽取
     * 可参考 https://bbs.nga.cn/read.php?tid=8477978 #4楼
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
     * 抽取指定对象
     * @param rollUnit 指定概率UP式神对象
     * @param fullBuff   是否启用全图鉴加成
     * @return 抽取数据
     */
    @Override
    public RollResultData rollTextForSpecifyUnit(@NotNull RollUnit rollUnit,@NotNull Boolean fullBuff) {
        //初始返回数据对象
        RollResultData rollResultData = new YysRollResultData();
        //出货结果集
        List<RollResultUnit> winResultUnitList = new ArrayList<>(20);
        rollResultData.setWinResultUnitList(winResultUnitList);
        List<RollResultUnit> rollResultUnitList = new ArrayList<>();
        LinkedHashMap<Integer, String> tipMap = new LinkedHashMap<>();
        //初始化指定抽取概率
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
        Map<Integer,RollUnit> levelUnitMap =  new HashMap<>(10);
        levelUnitMap.putAll(rollUnitMap.get(upUnitLevel));
        if (YysRoll.SSR.getLevel().equals(upUnitLevel) || YysRoll.SP.getLevel().equals(upUnitLevel)) {
            //指定SSR/SP时在对应阶级卡池中移除指定概率UP式神，后续进行额外抽取
            levelUnitMap.remove(rollUnit.getId());
            /*SSR概率初始化：
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
                    stepUpProb.put(400, new BigDecimal("0.03"));
                    stepUpProb.put(450, new BigDecimal("0.05"));
                }
                noFullLastMaxProb = new BigDecimal("0.3");
            }
            /*SP概率初始化：
             * 全图鉴：初始10%持续提升并在500次召唤后提升至100%
             * 非全图鉴：初始的3%持续提升并在500次召唤后提升至15%
             */
            else {
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
        }
        //在定向模拟时被捣蛋时，设置第一发SSR/SP转为N
        else if (YysRoll.N.getLevel().equals(upUnitLevel)){
            rollProb = new BigDecimal(1);
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

        //抽卡开始
        while (true) {
            RollResultUnit rollResultUnit;
            String rollLevel = rollLevel(up, UP_RATE);
            //抽卡次数达到时50的倍数，概率提升
            if (rollNum % stepUpNum == 0) {
                BigDecimal tempProb = stepUpProb.get(rollNum);
                if (tempProb != null) {
                    upProb = tempProb;
                }
                if (rollNum <= normalMaxProbNum) {
                    rollProb = rollProb.add(upProb).min(maxProb);
                    String probString = rollProb.movePointRight(2).toString();
                    tipMap.put(rollNum, "---"+rollNum+"抽，定向UP↑:" + probString+ "%---");
                }
                //非全图抽到700抽时，进一步提升概率
                if (!fullBuff && rollNum == maxRollNum) {
                    rollProb = noFullLastMaxProb;
                    String probString = rollProb.movePointRight(2).toString();
                    tipMap.put(rollNum,"---"+rollNum+"抽，定向UP↑:"+probString+ "%---");
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
                    //设置定向抽取概率
                    rollResultUnit.setUnitRollRate(rollProb);
                    rollResultUnit.setLevelRollRate(new BigDecimal(YysRoll.valueOf(rollUnit.getLevel()).getRollProbString()).multiply(up?upProb:BigDecimal.ONE));
                    rollResultUnitList.add(rollResultUnit);
                    //高阶添加到出货结果集
                    winResultUnitList.add(rollResultUnit);
                    //为SSR时抽取皮肤
                    if (YysRoll.SSR.getLevel().equals(rollResultUnit.getLevel()) && rollUnit.getSpecialName() != null) {
                        rollResultUnit.setSpecial(rollSsrSkin());
                    }
                    break;
                }else {
                    rollResultUnit = new RollResultUnit(startRollUnit(rollUnit.getLevel().equals(rollLevel)?levelUnitMap:rollUnitMap.get(rollLevel)));
                    rollResultUnit.setUp(up);
                }
                //高阶添加到出货结果集
                winResultUnitList.add(rollResultUnit);
                //设置定向抽取概率
                rollResultUnit.setUnitRollRate(rollProb);
                rollResultUnit.setLevelRollRate(new BigDecimal(YysRoll.valueOf(rollUnit.getLevel()).getRollProbString()).multiply(up?upProb:BigDecimal.ONE));
                //为SSR时抽取皮肤
                if (YysRoll.SSR.getLevel().equals(rollResultUnit.getLevel()) && rollUnit.getSpecialName() != null) {
                    rollResultUnit.setSpecial(rollSsrSkin());
                }
            }else {
                rollResultUnit = new RollResultUnit (startRollUnit(rollUnitMap.get(rollLevel)));
            }
            //储存结果
            if (rollResultUnit != null) {
                rollResultUnit.setSequence(rollNum);
                rollResultUnitList.add(rollResultUnit);

                //判断是否已抽出指定结果(SR/R)
                if (rollResultUnit.getId().equals(rollUnit.getId())) {
                    //全输出结果太长，这里通过tip进行额外显示
                    tipMap.put(rollNum,rollResultUnit.toString());
                    break;
                }
            }
            rollNum++;
            //保底时
            if (fullBuff&&rollNum==maxRollNum){
                rollResultUnit = new RollResultUnit(rollUnit);
                rollResultUnit.setSequence(rollNum);
                rollResultUnit.setUp(up);
                rollResultUnitList.add(rollResultUnit);
                //添加到出货结果集
                winResultUnitList.add(rollResultUnit);
                break;
            }
        }
        //输出结果
        rollResultData.setRollResultUnitList(rollResultUnitList);
        rollResultData.setRollNum(rollNum);
        rollResultData.setTipMap(tipMap);
        return rollResultData;
    }

    /**
     * 抽取指定式神 （普通抽卡）
     *
     * @param rollUnit 指定抽卡对象
     * @param up       是否启用UP加成
     * @return 抽取结果
     */
    @Override
    public RollResultData rollTextForAssignUnit(RollUnit rollUnit, Boolean up) {
        //传回的对象为空或阶级为N卡时，返回空结果
        if (rollUnit == null || YysRoll.N.getLevel().equals(rollUnit.getLevel())) {
            return new YysRollResultData();
        }
        //初始返回数据对象
        List<RollResultUnit> rollResultUnitList = new ArrayList<>();
        RollResultData rollResultData = new YysRollResultData();
        //出货结果集
        List<RollResultUnit> winResultUnitList = new ArrayList<>(20);
        rollResultData.setWinResultUnitList(winResultUnitList);
        //统计抽取次数
        int rollNum = 1;
        //未定义up倍数时手动添加
        float upRate = this.UP_RATE;
        int upNum = up?3:0;
        while (true) {
            //抽取阶级
            String rollLevel = rollLevel(up, upRate);
            //抽取式神
            RollUnit nowRollUnit = startRollUnit(rollUnitMap.get(rollLevel));
            if (nowRollUnit != null) {
                RollResultUnit rollResultUnit = new RollResultUnit (nowRollUnit);
                //为SSR阶时抽取皮肤
                if (YysRoll.SSR.getLevel().equals(rollResultUnit.getLevel()) && rollUnit.getSpecialName() != null) {
                    rollResultUnit.setSpecial(rollSsrSkin());
                }
                //出货时减去Up次数，2021-11-19修正，默认三次UP时出现实际UP4次的BUG
                if (YysRoll.SSR.getLevel().equals(rollResultUnit.getLevel()) || YysRoll.SP.getLevel().equals(rollResultUnit.getLevel())) {
                    rollResultUnit.setUp(up);
                    up = --upNum > 0;
                    //设置阶级抽取概率
                    rollResultUnit.setLevelRollRate(new BigDecimal(YysRoll.valueOf(rollUnit.getLevel()).getRollProbString()).multiply(up? BigDecimal.valueOf(upRate) :BigDecimal.ONE));
                    //添加到出货结果集
                    winResultUnitList.add(rollResultUnit);
                }
                //设置抽取次数次数
                rollResultUnit.setSequence(rollNum);
                rollResultUnitList.add(rollResultUnit);
                //抽出指定式神时，结束抽取
                if (nowRollUnit.getId().equals(rollUnit.getId())) {
                    break;
                }
            }
            rollNum++;
        }
        rollResultData.setRollNum(rollNum);
        rollResultData.setRollResultUnitList(rollResultUnitList);
        return rollResultData;
    }

    public Map<String, Map<Integer, RollUnit>> getRollUnitMap() {
        return rollUnitMap;
    }

    /**
     * 抽取结果
     *
     * @param rollNum  抽取次数
     * @param up       是否启用概率up
     * @param upRate   up倍率
     * @param upNum    up次数
     * @param winProb 高阶抽取概率，为null时使用默认概率
     * @return 抽取结果
     */
    private RollResultData rollResult(Integer rollNum, Boolean up, Integer upNum, Float upRate, Float winProb) {
        //初始返回数据对象
        RollResultData rollResultData = new YysRollResultData();
        rollResultData.setRollNum(rollNum);
        //总抽取结果集
        List<RollResultUnit> rollResultUnitList = new ArrayList<>(rollNum);
        rollResultData.setRollResultUnitList(rollResultUnitList);
        //出货结果集
        List<RollResultUnit> winResultUnitList = new ArrayList<>(rollNum / 50);
        rollResultData.setWinResultUnitList(winResultUnitList);
        //验证并初始化数值
        if (upRate == null) {
            upRate = this.UP_RATE;
        }
        if (up) {
            upNum = upNum == null ? 3 : upNum;
        }else{
            upNum = 0;
        }
        if (winProb == null) {
            winProb = NORMAL_HIGH_LEVEL_RATE;
        }
        //实际抽取概率
        float rollProb = up ? winProb * upRate : winProb;
        for (int i = 1; i <= rollNum; i++) {
            //抽取等级
            String rollLevel = rollLevel(rollProb);
            //抽取出货对象
            RollUnit rollUnit = startRollUnit(rollUnitMap.get(rollLevel));
            //对结果进行处理
            if (rollUnit != null) {
                RollResultUnit rollResultUnit = new RollResultUnit (rollUnit);
                //为SSR阶时抽取皮肤
                if (YysRoll.SSR.getLevel().equals(rollResultUnit.getLevel()) && rollUnit.getSpecialName() != null) {
                    rollResultUnit.setSpecial(rollSsrSkin());
                }
                rollResultUnit.setSequence(i);
                rollResultUnitList.add(rollResultUnit);
                //对成功出货的对象进行处理
                if (YysRoll.SSR.getLevel().equals(rollResultUnit.getLevel()) || YysRoll.SP.getLevel().equals(rollResultUnit.getLevel())) {
                    //设置up标志
                    rollResultUnit.setUp(up);
                    //储存出货对象
                    winResultUnitList.add(rollResultUnit);
                    //UP标记和抽取概率
                    up = --upNum > 0;
                    rollProb = up ? rollProb : winProb;
                }
            }
        }

        return rollResultData;
    }

    /**
     * 普通抽取-可定出货概率
     *
     * @param rollNum 抽取次数
     * @param winProb 高阶抽取概率，为null时使用默认概率
     * @return 抽取结果
     */
    @Override
    public RollResultData roll(@NotNull Integer rollNum, Float winProb) {
        return rollResult(rollNum, Boolean.FALSE, 0, 0f, null);
    }

    /**
     * 抽取结果-启用特定概率提升
     *
     * @param rollNum 抽取次数
     * @param up      是否启用概率up
     * @param upRate  up倍率
     * @param upNum   up
     * @return 抽取结果
     */
    @Override
    public RollResultData rollUp(@NotNull Integer rollNum, @NotNull Boolean up, Integer upNum, Float upRate) {
        return rollResult(rollNum, up, upNum, upRate, null);
    }
}
