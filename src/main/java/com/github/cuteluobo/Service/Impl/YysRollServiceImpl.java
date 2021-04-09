package com.github.cuteluobo.Service.Impl;

import cn.pomit.mybatis.ProxyHandlerFactory;
import com.github.cuteluobo.Pojo.RollImgResult;
import com.github.cuteluobo.Pojo.RollResultData;
import com.github.cuteluobo.Pojo.RollResultUnit;
import com.github.cuteluobo.Pojo.RollUnit;
import com.github.cuteluobo.Service.ExpandRollService;
import com.github.cuteluobo.Service.RollService;
import com.github.cuteluobo.enums.YysRoll;
import mapper.YysUnitMapper;
import model.YysUnit;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

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
        List<YysUnit> yysUnitList = yysUnitMapper.selectList(true);
        if (yysUnitList != null) {
            List<RollUnit> rollUnitList = yysUnitList.stream().map(RollUnit::new).collect(Collectors.toList());
            for (RollUnit rollUnit : rollUnitList) {
                //获取已有阶级式神列表，为null时进行初始化
                Map<Integer,RollUnit> levelRollUnitMap = rollUnitMap.get(rollUnit.getLevel());
                if (levelRollUnitMap == null) {
                    levelRollUnitMap = new HashMap<>();
                    rollUnitMap.put(rollUnit.getLevel(), levelRollUnitMap);
                }
                //对引用对象操作应该可以存进map里？
                levelRollUnitMap.put(rollUnit.getId(), rollUnit);
            }
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

        RollResultData rollResultData = new RollResultData();
        rollResultData.setRollNum(rollNum);
        //未定义up倍数时手动添加
        if (upRate == null) {
            upRate = this.UP_RATE;
        }
        if (!up) {
            upNum = 0;
        }
        for (int i = 1; i <= rollNum; i++) {
            //出货时减去Up次数
            up = upNum-- > 0;
            String rollLevel = rollLevel(up, upRate);
            RollUnit rollUnit = rollUnit(rollUnitMap.get(rollLevel));
            RollResultUnit rollResultUnit = (RollResultUnit) rollUnit;
            //为SSR阶时抽取皮肤
            if (YysRoll.SSR.getLevel().equals(rollResultUnit.getLevel()) && rollUnit.getSpecialName() != null) {
                rollResultUnit.setSpecial(rollSsrSkin());
            }
            rollResultUnit.setUp(up);
            rollResultUnit.setSequence(i);
            rollResultUnitList.add(rollResultUnit);
        }
        rollResultData.setRollUnitList(rollResultUnitList);
        return null;
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
    private RollUnit rollUnit(Map<Integer,RollUnit> levelRollUnitMap){
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
        List<RollResultUnit> rollResultUnitList = new ArrayList<>();
        RollResultData rollResultData = new RollResultData();
        //当前指定抽取概率
        float rollProb = 0.0f;
        //最大概率
        float maxProb = 0.0f;
        //阶梯提升数量
        int stepUpNum = 50;
        //阶梯提升概率
        float stepUpProb = 0.0f;
        //保底（必出）抽卡数
        int maxRollNum = 700;
        String upUnitLevel = rollUnit.getLevel();
        /*SSR概率初始化：
         * 全图鉴：初始15%持续提升并在500次召唤后提升至100%
         * 非全图鉴：初始的4%持续提升并在500次召唤后提升至20%
         */
        if (YysRoll.SSR.getLevel().equals(upUnitLevel)) {
            rollProb = fullBuff?0.15f:0.04f;
            maxProb = fullBuff?1.0f:0.2f;
        }
        /*SP概率初始化：
         * 全图鉴：初始10%持续提升并在500次召唤后提升至100%
         * 非全图鉴：初始的3%持续提升并在500次召唤后提升至15%
         */
        else if (YysRoll.SP.getLevel().equals(upUnitLevel)) {
            rollProb = fullBuff?0.10f:0.03f;
            maxProb = fullBuff?1.0f:0.15f;
        }
        //抽卡次数
        int rollNum = 0;
        //概率up
        boolean up = true;
        int upNum = 3;
        //在对应阶级卡池中移除指定概率UP式神，后续进行额外抽取
        Map<Integer,RollUnit> levelUnitMap =  new HashMap<>(10);
        levelUnitMap.putAll(rollUnitMap.get(upUnitLevel));
        levelUnitMap.remove(rollUnit.getId());
        //抽卡开始
        while (rollNum<maxRollNum) {
            RollResultUnit rollResultUnit = null;
            rollNum++;
            String rollLevel = rollLevel(up, UP_RATE);
            if (YysRoll.SSR.getLevel().equals(rollLevel) ||  YysRoll.SP.getLevel().equals(rollLevel)){

                //先进行指定概率UP判定
                if (Math.random() <= rollProb) {
                    rollResultUnit = (RollResultUnit) rollUnit;
                    rollResultUnit.setSequence(rollNum);
                    rollResultUnit.setUp(up);
                    rollResultUnitList.add(rollResultUnit);
                    //为SSR时抽取皮肤
                    if (YysRoll.SSR.getLevel().equals(rollResultUnit.getLevel()) && rollUnit.getSpecialName() != null) {
                        rollResultUnit.setSpecial(rollSsrSkin());
                    }
                    break;
                }else {
                    rollResultUnit = (RollResultUnit) rollUnit(levelUnitMap);
                }
                //为SSR时抽取皮肤
                if (YysRoll.SSR.getLevel().equals(rollResultUnit.getLevel()) && rollUnit.getSpecialName() != null) {
                    rollResultUnit.setSpecial(rollSsrSkin());
                }
                //出货时减去Up次数
                up = upNum-- > 0;
            }else {
                rollResultUnit = (RollResultUnit) rollUnit(rollUnitMap.get(rollLevel));
            }
            if (rollResultUnit != null) {
                rollResultUnit.setSequence(rollNum);
                rollResultUnit.setUp(up);
                rollResultUnitList.add(rollResultUnit);
            }
        }
        //保底时
        if (rollNum++ == maxRollNum) {
            RollResultUnit rollResultUnit = (RollResultUnit) rollUnit;
            rollResultUnit.setSequence(rollNum);
            rollResultUnit.setUp(up);
            rollResultUnitList.add(rollResultUnit);
        }
        //输出结果
        rollResultData.setRollUnitList(rollResultUnitList);
        rollResultData.setRollNum(rollNum);
        return rollResultData;
    }
}
