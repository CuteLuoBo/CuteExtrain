package com.github.cuteluobo.pojo.aidraw;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author CuteLuoBo
 * @date 2022/10/14 13:16
 */
public class StableDiffusionWebUiImg2ImgScriptParameter extends StableDiffusionWebUiScriptBaseParameter{

    /**
     * 启用的脚本
     * -img2img alternative test - 替代测试
     * -Loopback
     * -Outpainting mk2
     * -Poor man's outpainting
     * -Prompt matrix
     * -Prompts from file or textbox
     * -SD upscale
     * -X/Y plot
     */
    private String script = "None";

    /**
     * img2img alternative test脚本参数
     */
    private String s1_originalPrompt = "";
    private String s1_OriginalNegativePrompt = "";
    private int s1_DecodeCfgScale = 1;
    private int s1_DecodeSteps = 50;
    private BigDecimal s1_Randomness = BigDecimal.ZERO;
    private boolean s1_SigmaAdjustmentForFindingNoiseForImage = false;

    /**
     * Loopback脚本参数
     */
    private int s2_Loops = 4;
    private BigDecimal s2_DenoisingStrengthChangeFactor = BigDecimal.ONE;


    /**
     * Outpainting mk2 脚本参数
     */
    private String s3_TipHtml = "";
    private int s3_Pixels2Expand = 128;
    private int s3_MaskBlur = 8;
    private String[] s3_OutpaintingDirection = {"left", "right", "up", "down"};
    private BigDecimal s3_FallOffExponent = BigDecimal.ONE;
    private BigDecimal s3_ColorVariation = new BigDecimal("0.05");

    /**
     * Poor man's outpainting脚本参数
     */
    private int s4_Pixels2Expand = 128;
    private int s4_MaskBlur = 8;
    private String s4_MaskedContent = "fill";
    private String[] s4_OutpaintingDirection = {"left", "right", "up", "down"};

    /**
     * SD upscale脚本参数
     */
    private String s5_TipHtml = "";
    private int s5_TileOverlap = 64;
    private String s5_Upscaler = "None";

    /**
     * @return 获取排列好的参数列表
     */
    @Override
    public List<Object> getParameterList() {
        List<Object> objectList = new ArrayList<>();
        objectList.add(this.getScript());
        objectList.add(this.getS1_originalPrompt());
        objectList.add(this.getS1_OriginalNegativePrompt());
        objectList.add(this.getS1_DecodeCfgScale());
        objectList.add(this.getS1_DecodeSteps());
        objectList.add(this.getS1_Randomness());
        objectList.add(this.isS1_SigmaAdjustmentForFindingNoiseForImage());

        objectList.add(this.getS2_Loops());
        objectList.add(this.getS2_DenoisingStrengthChangeFactor());

        objectList.add(this.getS3_TipHtml());
        objectList.add(this.getS3_Pixels2Expand());
        objectList.add(this.getS3_MaskBlur());
        objectList.add(this.getS3_OutpaintingDirection());
        objectList.add(this.getS3_FallOffExponent());
        objectList.add(this.getS3_ColorVariation());

        objectList.add(this.getS4_Pixels2Expand());
        objectList.add(this.getS4_MaskBlur());
        objectList.add(this.getS4_MaskedContent());
        objectList.add(this.getS4_OutpaintingDirection());
        //base部分
        objectList.add(this.isS1_PutVariablePartsAtStartOfPrompt());

        objectList.add(this.isS2_ShowTextboxCheck());
        objectList.add(this.getS2_faceRestorationModel());
        objectList.add(this.getS2_allText());
        //新插入的一个SD upscale
        objectList.add(this.getS5_TipHtml());
        objectList.add(this.getS5_TileOverlap());
        objectList.add(this.getS5_Upscaler());
        //继续base部分
        objectList.add(this.getS3_XTypeKey1());
        objectList.add(this.getS4_XTypeValue1());
        objectList.add(this.getS4_XTypeKey2());
        objectList.add(this.getS4_XTypeValue2());
        objectList.add(this.isS4_DrawLegend());
        objectList.add(this.isS4_KeepSeeds());

        objectList.add(this.getUnkown1());
        objectList.add(this.getJson());
        objectList.add(this.getHtml());
        return objectList;
    }

    public String getS5_TipHtml() {
        return s5_TipHtml;
    }

    public void setS5_TipHtml(String s5_TipHtml) {
        this.s5_TipHtml = s5_TipHtml;
    }

    public int getS5_TileOverlap() {
        return s5_TileOverlap;
    }

    public void setS5_TileOverlap(int s5_TileOverlap) {
        this.s5_TileOverlap = s5_TileOverlap;
    }

    public String getS5_Upscaler() {
        return s5_Upscaler;
    }

    public void setS5_Upscaler(String s5_Upscaler) {
        this.s5_Upscaler = s5_Upscaler;
    }

    @Override
    public String getScript() {
        return script;
    }

    @Override
    public void setScript(String script) {
        this.script = script;
    }

    public String getS1_originalPrompt() {
        return s1_originalPrompt;
    }

    public void setS1_originalPrompt(String s1_originalPrompt) {
        this.s1_originalPrompt = s1_originalPrompt;
    }

    public String getS1_OriginalNegativePrompt() {
        return s1_OriginalNegativePrompt;
    }

    public void setS1_OriginalNegativePrompt(String s1_OriginalNegativePrompt) {
        this.s1_OriginalNegativePrompt = s1_OriginalNegativePrompt;
    }

    public int getS1_DecodeCfgScale() {
        return s1_DecodeCfgScale;
    }

    public void setS1_DecodeCfgScale(int s1_DecodeCfgScale) {
        this.s1_DecodeCfgScale = s1_DecodeCfgScale;
    }

    public int getS1_DecodeSteps() {
        return s1_DecodeSteps;
    }

    public void setS1_DecodeSteps(int s1_DecodeSteps) {
        this.s1_DecodeSteps = s1_DecodeSteps;
    }

    public BigDecimal getS1_Randomness() {
        return s1_Randomness;
    }

    public void setS1_Randomness(BigDecimal s1_Randomness) {
        this.s1_Randomness = s1_Randomness;
    }

    public boolean isS1_SigmaAdjustmentForFindingNoiseForImage() {
        return s1_SigmaAdjustmentForFindingNoiseForImage;
    }

    public void setS1_SigmaAdjustmentForFindingNoiseForImage(boolean s1_SigmaAdjustmentForFindingNoiseForImage) {
        this.s1_SigmaAdjustmentForFindingNoiseForImage = s1_SigmaAdjustmentForFindingNoiseForImage;
    }

    public int getS2_Loops() {
        return s2_Loops;
    }

    public void setS2_Loops(int s2_Loops) {
        this.s2_Loops = s2_Loops;
    }

    public BigDecimal getS2_DenoisingStrengthChangeFactor() {
        return s2_DenoisingStrengthChangeFactor;
    }

    public void setS2_DenoisingStrengthChangeFactor(BigDecimal s2_DenoisingStrengthChangeFactor) {
        this.s2_DenoisingStrengthChangeFactor = s2_DenoisingStrengthChangeFactor;
    }

    public String getS3_TipHtml() {
        return s3_TipHtml;
    }

    public void setS3_TipHtml(String s3_TipHtml) {
        this.s3_TipHtml = s3_TipHtml;
    }

    public int getS3_Pixels2Expand() {
        return s3_Pixels2Expand;
    }

    public void setS3_Pixels2Expand(int s3_Pixels2Expand) {
        this.s3_Pixels2Expand = s3_Pixels2Expand;
    }

    public int getS3_MaskBlur() {
        return s3_MaskBlur;
    }

    public void setS3_MaskBlur(int s3_MaskBlur) {
        this.s3_MaskBlur = s3_MaskBlur;
    }

    public String[] getS3_OutpaintingDirection() {
        return s3_OutpaintingDirection;
    }

    public void setS3_OutpaintingDirection(String[] s3_OutpaintingDirection) {
        this.s3_OutpaintingDirection = s3_OutpaintingDirection;
    }

    public BigDecimal getS3_FallOffExponent() {
        return s3_FallOffExponent;
    }

    public void setS3_FallOffExponent(BigDecimal s3_FallOffExponent) {
        this.s3_FallOffExponent = s3_FallOffExponent;
    }

    public BigDecimal getS3_ColorVariation() {
        return s3_ColorVariation;
    }

    public void setS3_ColorVariation(BigDecimal s3_ColorVariation) {
        this.s3_ColorVariation = s3_ColorVariation;
    }

    public int getS4_Pixels2Expand() {
        return s4_Pixels2Expand;
    }

    public void setS4_Pixels2Expand(int s4_Pixels2Expand) {
        this.s4_Pixels2Expand = s4_Pixels2Expand;
    }

    public int getS4_MaskBlur() {
        return s4_MaskBlur;
    }

    public void setS4_MaskBlur(int s4_MaskBlur) {
        this.s4_MaskBlur = s4_MaskBlur;
    }

    public String getS4_MaskedContent() {
        return s4_MaskedContent;
    }

    public void setS4_MaskedContent(String s4_MaskedContent) {
        this.s4_MaskedContent = s4_MaskedContent;
    }

    public String[] getS4_OutpaintingDirection() {
        return s4_OutpaintingDirection;
    }

    public void setS4_OutpaintingDirection(String[] s4_OutpaintingDirection) {
        this.s4_OutpaintingDirection = s4_OutpaintingDirection;
    }
}
