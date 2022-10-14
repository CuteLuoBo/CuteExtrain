package com.github.cuteluobo.pojo.aidraw;

import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;

/**
 * StableDiffusion-WebUi 转图片共用参数
 * @author CuteLuoBo
 * @date 2022/10/13 23:09
 */
public interface IStableDiffusionWebUi2ImgParameter {

    String promptStyle1 = "None";

    String promptStyle2 = "None";

    /**
     * 取样方法
     */
    String method = StableDiffusionWebUiMethod.Euler_A.getArgsText();

    /**
     * 针对面部修复
     */
    boolean restoreFaces = false;

    /**
     * 平铺图像
     */
    boolean tiling = false;

    /**
     * 变体种子-和原种子一起混合到1代中
     */
    int variationSeed = -1;

    /**
     * 变异强度-对原种子的影响程度(0-1)
     */
    BigDecimal variationStrength = new BigDecimal("0.2");

    /**
     * “从高度调整种子大小”：“尝试生成与使用相同种子以指定分辨率生成的图片类似的图片”，
     * (0-2048),64/step
     */
    int resizeHeight = 0;

    /**
     * “从宽度度调整种子大小”：“尝试生成与使用相同种子以指定分辨率生成的图片类似的图片”，
     * (0-2048),64/step
     */
    int resizeWidth = 0;

    /**
     * 是否启用变体种子(界面相关检查点)
     */
    boolean variationCheckBox = false;
}
