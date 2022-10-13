package com.github.cuteluobo.pojo.aidraw;

import java.math.BigDecimal;

/**
 * 基础AI生成参数 txt -> img
 * @author CuteLuoBo
 * @date 2022/10/11 16:20
 */
public class BaseAiImageCreateParameter {
    public BaseAiImageCreateParameter() {
    }

    public BaseAiImageCreateParameter(String prompt) {
        this.prompt = prompt;
    }

    /**
     * 提示
     */
    private String prompt;
    /**
     * 消极提示
     */
    private String lowPrompt = "lowres, bad anatomy, bad hands, text, error, missing fingers, extra digit, fewer digits, cropped, worst quality, low quality, normal quality, jpeg artifacts, signature, watermark, username, blurry";

    /**
     * 图片高度
     */
    private int height = 768;

    /**
     * 图片宽度
     */
    private int width = 512;

    private String method = StableDiffusionWebUiMethod.Euler_A.getArgsText();

    /**
     * 生成步数(1-150)
     */
    private int steps = 30;

    /**
     * 生成种子
     */
    private long seed = System.currentTimeMillis() / 1000;

    /**
     * “去噪强度”：“确定算法对图像内容的尊重程度。值为0时，什么都不会改变，值为1时，您将得到一个不相关的图像。当值低于1.0时，处理所需的步骤将少于“采样步骤”滑块指定的步骤。”，
     */
    private BigDecimal denoisingStrength = new BigDecimal("0.7");

    /**
     * 与提示的吻合程度，值越低越有新意（意料之外的图像增加）
     */
    private BigDecimal cfgScale = BigDecimal.valueOf(11);

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getLowPrompt() {
        return lowPrompt;
    }

    public void setLowPrompt(String lowPrompt) {
        this.lowPrompt = lowPrompt;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public long getSeed() {
        return seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public BigDecimal getDenoisingStrength() {
        return denoisingStrength;
    }

    public void setDenoisingStrength(BigDecimal denoisingStrength) {
        this.denoisingStrength = denoisingStrength;
    }

    public BigDecimal getCfgScale() {
        return cfgScale;
    }

    public void setCfgScale(BigDecimal cfgScale) {
        this.cfgScale = cfgScale;
    }
}
