package com.github.cuteluobo.pojo;

import com.github.cuteluobo.enums.YysRoll;

import java.util.List;

/**
 * @author CuteLuoBo
 * @date 2021-04-13
 */
public class YysRollResultData extends RollResultData {

    /**
     * 打印抽取结果
     * @param showAllLevel 是否显示所有抽卡结果
     * @param hideLowLevelDetail 是否隐藏低阶细节（仅统计数量）
     * @return
     */
    @Override
    public String printResultText(boolean showAllLevel, boolean hideLowLevelDetail){
        final StringBuffer sb = new StringBuffer();
        //开头语改为由外部定义
//        sb.append(getRollNum()).append("次抽卡结果:").append("\n");
        List<RollResultUnit> rollResultUnitList = getRollUnitList();
        if (rollResultUnitList==null || rollResultUnitList.size()==0){
            sb.append("无事发生");
        }else {
            int rLevelNum = 0;
            int srLevelNum = 0;
            if (!showAllLevel) {
                for (int i = 0; i < rollResultUnitList.size(); i++) {
                    RollResultUnit rollResultUnit = rollResultUnitList.get(i);
                    if (YysRoll.SSR.getLevel().equals(rollResultUnit.getLevel()) || YysRoll.SSR.getLevel().equals(rollResultUnit.getLevel())) {
                        sb.append(rollResultUnit).append("\n");
                    }
                    String tipText = getTipMap().get(i);
                    if (tipText != null) {
                        sb.append(tipText).append("\n");
                    }
                    if (YysRoll.SR.getLevel().equals(rollResultUnit.getLevel())) {
                        srLevelNum++;
                    }
                    if (YysRoll.R.getLevel().equals(rollResultUnit.getLevel())) {
                        rLevelNum++;
                    }
                }
                sb.append("\n");
                sb.append(YysRoll.SR.getLevel()).append("阶数量：").append(srLevelNum).append("\n");
                sb.append(YysRoll.R.getLevel()).append("阶数量：").append(rLevelNum).append("\n");
            }else {
                for (RollResultUnit rollResultUnit : rollResultUnitList) {
                    sb.append(rollResultUnit).append("\n");
                    String tipText = getTipMap().get(rollResultUnit.getSequence());
                    if (tipText != null) {
                        sb.append(tipText).append("\n");
                    }
                }
            }
            //TODO 增加低阶输出（转换到子对象）
        }
        return sb.toString();
    }
}
