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

    public final float UP_RATE = 2.5f;
    public final float SSR_SKIN_RATE = 0.1f;
    private final float NORMAL_HIGH_LEVEL_RATE = YysRoll.SSR.getRollProb()+YysRoll.SP.getRollProb();
    private YysUnitMapper yysUnitMapper;
    private Map<String, List<RollUnit>> rollUnitMap = new HashMap<>();

    private YysRollServiceImpl(){
       yysUnitMapper = ProxyHandlerFactory.getMapper(YysUnitMapper.class);
       //填充卡池式神数据
        List<YysUnit> yysUnitList = yysUnitMapper.selectList(true);
        if (yysUnitList != null) {
            List<RollUnit> rollUnitList = yysUnitList.stream().map(RollUnit::new).collect(Collectors.toList());
            for (RollUnit rollUnit : rollUnitList) {
                //获取已有阶级式神列表，为null时进行初始化
                List<RollUnit> levelRollUnitList = rollUnitMap.get(rollUnit.getLevel());
                if (levelRollUnitList == null) {
                    levelRollUnitList = new ArrayList<>();
                    rollUnitMap.put(rollUnit.getLevel(), levelRollUnitList);
                }
                //对引用对象操作应该可以存进map里？
                levelRollUnitList.add(rollUnit);
            }
        }
    }


    /**
     * 抽取文字结果
     *
     * @param rollNum  抽取次数
     * @param up       是否启用概率up
     * @param upRate   up倍率
     * @param upNum    up
     * @param winProb 高阶抽取概率，为null时使用默认概率
     * @return 抽取结果
     */
    @Override
    public RollResultData rollText(Integer rollNum, Boolean up, Float upRate, Integer upNum, Float winProb) {
        //数据库对象转换为RollUnit
        List<RollResultUnit> rollResultUnitList = new ArrayList<>();
        //初始返回数据对象
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
            String rollLevel = rollLevel(up, upRate);
        }
        rollResultData.setRollUnitList(rollResultUnitList);
        return null;
    }

    /**内部抽卡类，
     * TODO 返回抽取阶级，通过抽卡阶级在<阶级，式神列表>map内进行抽取式神*/
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
     * TODO 抽取指定阶级中的式神
     * 随机抽取可参考 https://bbs.nga.cn/read.php?tid=8477978 #4楼
     * @param level 阶级
     * @param upUnitId  概率UP的式神ID
     * @param upUnitProb    概率UP的式神抽取概率
     * @return 抽取结果
     */
    private RollUnit rollUnit(@NotNull String level, Integer upUnitId,Float upUnitProb){
        List<RollUnit> rollUnitList = rollUnitMap.get(level);
        if (rollUnitList != null) {

        }
        return null;
    }

    /**
     * 抽取SSR皮肤(10%)
     * @return 是否抽中
     */
    private Boolean rollSsrSkin() {
        Random random = new Random();
        int rollNum = random.nextInt(9);
        if (rollNum == 0) {
            return true;
        }else {
            return false;
        }
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
     *
     * @param rollUnitId 抽取对象ID
     * @param fullBuff   是否启用全图鉴加成
     * @return
     */
    @Override
    public RollResultData rollTextForSpecifyUnit(Integer rollUnitId, Boolean fullBuff) {
        return null;
    }
}
