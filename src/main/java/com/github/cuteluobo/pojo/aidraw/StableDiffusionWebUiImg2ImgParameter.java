package com.github.cuteluobo.pojo.aidraw;

import com.alibaba.fastjson.JSONArray;

import java.math.BigDecimal;
import java.util.List;

/**
 * 图转图
 *
 * @author CuteLuoBo
 */
public class StableDiffusionWebUiImg2ImgParameter extends AiImageCreateImg2ImgParameter {

    /**
     * 虚拟组件(index)？
     */
    private int dummyComponent = 0;

    /**
     * 图片标记？
     */
    private String imgMask = null;

    /**
     * 图片蒙版修补相关
     */
    private String imgInpaint = null;
    /**
     * 图片蒙版修补相关
     */
    private String maskInpaint = null;

    /**
     * 标记模式
     */
    private String maskMode = "Draw mask";

    /**
     * 标记模糊？
     */
    private int maskBlur = 4;

    /**
     * 修复填充
     */
    private String inpaintingFill = "original";

    /**
     * 尺寸调整模式
     */
    private String resizeMode = "Just resize";

    /**
     * 全分辨率模式修复
     */
    private boolean inpaintFullRes = false;


    /**
     * 全分辨率+填充模式修复
     */
    private int inpaintFullResPadding = 32;

    /**
     * 修复蒙版反转
     */
    private String inpaintingMaskInvert = "Inpaint masked";

    /**
     * 主机上的批量导入路径
     */
    private String img2imgBatchInputDir = "";

    /**
     * 主机上的批量输出路径
     */
    private String img2imgBatchOutputDir = "";

    private String promptStyle1 = "None";

    private String promptStyle2 = "None";

    /**
     * 取样方法
     */
    private String method = StableDiffusionWebUiMethod.Euler_A.getArgsText();

    /**
     * 针对面部修复
     */
    private boolean restoreFaces = false;

    /**
     * 平铺图像
     */
    private boolean tiling = false;

    /**
     * 变体种子-和原种子一起混合到1代中
     */
    private int variationSeed = -1;

    /**
     * 变异强度-对原种子的影响程度(0-1)
     */
    private BigDecimal variationStrength = new BigDecimal("0.2");

    /**
     * “从高度调整种子大小”：“尝试生成与使用相同种子以指定分辨率生成的图片类似的图片”，
     * (0-2048),64/step
     */
    private int resizeHeight = 0;

    /**
     * “从宽度度调整种子大小”：“尝试生成与使用相同种子以指定分辨率生成的图片类似的图片”，
     * (0-2048),64/step
     */
    private int resizeWidth = 0;

    /**
     * 是否启用变体种子(界面相关检查点)
     */
    private boolean variationCheckBox = false;

    /**
     * 脚本相关参数
     */
    private StableDiffusionWebUiImg2ImgScriptParameter stableDiffusionWebUiImg2ImgScriptParameter = new StableDiffusionWebUiImg2ImgScriptParameter();

    /**
     * 获取传回用的参数数组
     *
     * @param data 参数对象
     * @return 转换完成的JSONArray
     */
    public static JSONArray createDataArray(StableDiffusionWebUiImg2ImgParameter data) {
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(data.getDummyComponent());
        jsonArray.add(data.getPrompt());
        jsonArray.add(data.getLowPrompt());
        jsonArray.add(data.getPromptStyle1());
        jsonArray.add(data.getPromptStyle2());
        jsonArray.add(data.getBase64ImgData());
        jsonArray.add(data.getImgMask());
        jsonArray.add(data.getImgInpaint());
        jsonArray.add(data.getMaskInpaint());
        jsonArray.add(data.getMaskMode());
        jsonArray.add(data.getSteps());
        jsonArray.add(data.getMethod());
        jsonArray.add(data.getMaskBlur());
        jsonArray.add(data.getInpaintingFill());
        jsonArray.add(data.isRestoreFaces());
        jsonArray.add(data.isTiling());
        jsonArray.add(data.getBatchCount());
        jsonArray.add(data.getBatchSize());
        jsonArray.add(data.getCfgScale());
        jsonArray.add(data.getDenoisingStrength());
        jsonArray.add(data.getSeed());
        jsonArray.add(data.getVariationSeed());
        jsonArray.add(data.getVariationStrength());
        jsonArray.add(data.getResizeHeight());
        jsonArray.add(data.getResizeWidth());
        jsonArray.add(data.isVariationCheckBox());
        jsonArray.add(data.getHeight());
        jsonArray.add(data.getWidth());
        jsonArray.add(data.getResizeMode());
        jsonArray.add(data.isInpaintFullRes());
        jsonArray.add(data.getInpaintFullResPadding());
        jsonArray.add(data.getInpaintingMaskInvert());
        jsonArray.add(data.getImg2imgBatchInputDir());
        jsonArray.add(data.getImg2imgBatchOutputDir());
//        List<Object> scriptParameter = data.getStableDiffusionWebUiImg2ImgScriptParameter().getParameterList();
        jsonArray.addAll(data.getStableDiffusionWebUiImg2ImgScriptParameter().getParameterList());
        return jsonArray;
    }

    /**
     * 从基本参数中转换完全对象
     *
     * @param data 基本数据
     * @return 传回对象
     */
    public static StableDiffusionWebUiImg2ImgParameter convert(AiImageCreateImg2ImgParameter data) {
        StableDiffusionWebUiImg2ImgParameter webUiData = new StableDiffusionWebUiImg2ImgParameter();
        webUiData.setPrompt(data.getPrompt());
        webUiData.setLowPrompt(data.getLowPrompt());
        webUiData.setBatchCount(data.getBatchCount());
        webUiData.setBatchSize(data.getBatchSize());
        webUiData.setHeight(data.getHeight());
        webUiData.setWidth(data.getWidth());
        webUiData.setCfgScale(data.getCfgScale());
        webUiData.setDenoisingStrength(data.getDenoisingStrength());
        webUiData.setMethod(data.getMethod());
        webUiData.setSeed(data.getSeed());
        webUiData.setSteps(data.getSteps());
        webUiData.setBase64ImgData(data.getBase64ImgData());
        return webUiData;
    }

    public StableDiffusionWebUiImg2ImgScriptParameter getStableDiffusionWebUiImg2ImgScriptParameter() {
        return stableDiffusionWebUiImg2ImgScriptParameter;
    }

    public void setStableDiffusionWebUiImg2ImgScriptParameter(StableDiffusionWebUiImg2ImgScriptParameter stableDiffusionWebUiImg2ImgScriptParameter) {
        this.stableDiffusionWebUiImg2ImgScriptParameter = stableDiffusionWebUiImg2ImgScriptParameter;
    }

    /**
     * webui源码中的接收参数
     * inputs=[
     * dummy_component,
     * img2img_prompt,
     * img2img_negative_prompt,
     * img2img_prompt_style,
     * img2img_prompt_style2,
     * init_img,
     * init_img_with_mask,
     * init_img_inpaint,
     * init_mask_inpaint,
     * mask_mode,
     * steps,
     * sampler_index,
     * mask_blur,
     * inpainting_fill,
     * restore_faces,
     * tiling,
     * batch_count,
     * batch_size,
     * cfg_scale,
     * denoising_strength,
     * seed,
     * subseed, subseed_strength, seed_resize_from_h, seed_resize_from_w, seed_checkbox,
     * height,
     * width,
     * resize_mode,
     * inpaint_full_res,
     * inpaint_full_res_padding,
     * inpainting_mask_invert,
     * img2img_batch_input_dir,
     * img2img_batch_output_dir,
     * ] + custom_inputs,
     * outputs=[
     * img2img_gallery,
     * generation_info,
     * html_info
     * ],
     * show_progress=False,
     * )
     */


    public int getDummyComponent() {
        return dummyComponent;
    }

    public void setDummyComponent(int dummyComponent) {
        this.dummyComponent = dummyComponent;
    }

    public String getImgMask() {
        return imgMask;
    }

    public void setImgMask(String imgMask) {
        this.imgMask = imgMask;
    }

    public String getImgInpaint() {
        return imgInpaint;
    }

    public void setImgInpaint(String imgInpaint) {
        this.imgInpaint = imgInpaint;
    }

    public String getMaskInpaint() {
        return maskInpaint;
    }

    public void setMaskInpaint(String maskInpaint) {
        this.maskInpaint = maskInpaint;
    }

    public String getMaskMode() {
        return maskMode;
    }

    public void setMaskMode(String maskMode) {
        this.maskMode = maskMode;
    }

    public int getMaskBlur() {
        return maskBlur;
    }

    public void setMaskBlur(int maskBlur) {
        this.maskBlur = maskBlur;
    }

    public String getInpaintingFill() {
        return inpaintingFill;
    }

    public void setInpaintingFill(String inpaintingFill) {
        this.inpaintingFill = inpaintingFill;
    }

    public String getResizeMode() {
        return resizeMode;
    }

    public void setResizeMode(String resizeMode) {
        this.resizeMode = resizeMode;
    }

    public boolean isInpaintFullRes() {
        return inpaintFullRes;
    }

    public void setInpaintFullRes(boolean inpaintFullRes) {
        this.inpaintFullRes = inpaintFullRes;
    }

    public int getInpaintFullResPadding() {
        return inpaintFullResPadding;
    }

    public void setInpaintFullResPadding(int inpaintFullResPadding) {
        this.inpaintFullResPadding = inpaintFullResPadding;
    }

    public String getInpaintingMaskInvert() {
        return inpaintingMaskInvert;
    }

    public void setInpaintingMaskInvert(String inpaintingMaskInvert) {
        this.inpaintingMaskInvert = inpaintingMaskInvert;
    }

    public String getImg2imgBatchInputDir() {
        return img2imgBatchInputDir;
    }

    public void setImg2imgBatchInputDir(String img2imgBatchInputDir) {
        this.img2imgBatchInputDir = img2imgBatchInputDir;
    }

    public String getImg2imgBatchOutputDir() {
        return img2imgBatchOutputDir;
    }

    public void setImg2imgBatchOutputDir(String img2imgBatchOutputDir) {
        this.img2imgBatchOutputDir = img2imgBatchOutputDir;
    }

    public String getPromptStyle1() {
        return promptStyle1;
    }

    public void setPromptStyle1(String promptStyle1) {
        this.promptStyle1 = promptStyle1;
    }

    public String getPromptStyle2() {
        return promptStyle2;
    }

    public void setPromptStyle2(String promptStyle2) {
        this.promptStyle2 = promptStyle2;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public void setMethod(String method) {
        this.method = method;
    }

    public boolean isRestoreFaces() {
        return restoreFaces;
    }

    public void setRestoreFaces(boolean restoreFaces) {
        this.restoreFaces = restoreFaces;
    }

    public boolean isTiling() {
        return tiling;
    }

    public void setTiling(boolean tiling) {
        this.tiling = tiling;
    }

    public int getVariationSeed() {
        return variationSeed;
    }

    public void setVariationSeed(int variationSeed) {
        this.variationSeed = variationSeed;
    }

    public BigDecimal getVariationStrength() {
        return variationStrength;
    }

    public void setVariationStrength(BigDecimal variationStrength) {
        this.variationStrength = variationStrength;
    }

    public int getResizeHeight() {
        return resizeHeight;
    }

    public void setResizeHeight(int resizeHeight) {
        this.resizeHeight = resizeHeight;
    }

    public int getResizeWidth() {
        return resizeWidth;
    }

    public void setResizeWidth(int resizeWidth) {
        this.resizeWidth = resizeWidth;
    }

    public boolean isVariationCheckBox() {
        return variationCheckBox;
    }

    public void setVariationCheckBox(boolean variationCheckBox) {
        this.variationCheckBox = variationCheckBox;
    }
}
