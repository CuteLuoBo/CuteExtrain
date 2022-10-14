package com.github.cuteluobo.pojo.aidraw;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;
import java.util.List;

/**
 * StableDiffusion-WebUi的请求参数-文本->文字
 *
 * @author CuteLuoBo
 * @date 2022/10/9 12:03
 */
public class StableDiffusionWebUiText2ImgParameter extends AiImageCreateParameter {
    public StableDiffusionWebUiText2ImgParameter(String prompt, int steps, int batchCount, int batchSize, int height, int width) {
        super(prompt);
        this.prompt = prompt;
        this.steps = steps;
        this.batchCount = batchCount;
        BatchSize = batchSize;
        this.height = height;
        this.width = width;
    }
    public StableDiffusionWebUiText2ImgParameter(String prompt, int height, int width) {
        super(prompt);
        this.prompt = prompt;
        this.height = height;
        this.width = width;
    }
    public StableDiffusionWebUiText2ImgParameter() {
        super();
    }

    /**
     * webui中接收数据的特定结构体
     * txt2img_args = dict(
     *                 fn=wrap_gradio_gpu_call(modules.txt2img.txt2img),
     *                 _js="submit",
     *                 inputs=[
     *                     txt2img_prompt,
     *                     txt2img_negative_prompt,
     *                     txt2img_prompt_style,
     *                     txt2img_prompt_style2,
     *                     steps,
     *                     sampler_index,
     *                     restore_faces,
     *                     tiling,
     *                     batch_count,
     *                     batch_size,
     *                     cfg_scale,
     *                     seed,
     *                     subseed, subseed_strength, seed_resize_from_h, seed_resize_from_w, seed_checkbox,
     *                     height,
     *                     width,
     *                     enable_hr,
     *                     scale_latent,
     *                     denoising_strength,
     *                 ] + custom_inputs,
     *                 outputs=[
     *                     txt2img_gallery,
     *                     generation_info,
     *                     html_info
     *                 ],
     *                 show_progress=False,
     *             )*/

    /**
     * 提示
     */
    private String prompt;
    /**
     * 消极提示
     */
    private String lowPrompt = "lowres, bad anatomy, bad hands, text, error, missing fingers, extra digit, fewer digits, cropped, worst quality, low quality, normal quality, jpeg artifacts, signature, watermark, username, blurry";

    /**
     * 提示类型1
     */
    private String prompt_Styles1 = "None";
    /**
     * 提示类型2
     */
    private String prompt_Styles2 = "None";

    /**
     * 生成步数(1-150)
     */
    private int steps = 30;

    /**
     * 生成方法
     */
    private String method = StableDiffusionWebUiMethod.Euler_A.getArgsText();

    /**
     * 针对面部修复
     */
    private boolean restoreFaces ;

    /**
     * 平铺图像
     */
    private boolean tiling ;

    /**
     * 单次生成图片数量(1-16)
     */
    private int batchCount = 1;

    /**
     * 批量大小，跟显存占用相关(1-8)
     */
    private int BatchSize = 1;

    /**
     * 与提示的吻合程度，值越低越有新意（意料之外的图像增加）
     */
    private BigDecimal cfgScale = BigDecimal.valueOf(11);

    /**
     * 生成种子
     */
    private long seed = -1;

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
     * 图片高度
     */
    private int height = 768;

    /**
     * 图片宽度
     */
    private int width = 512;

    /**
     * “Highres.fix”：“使用两步过程以较小的分辨率、较高的比例部分创建图像，然后在不改变构图的情况下改进图像的细节”，
     */
    private boolean highresFix = false;

    /**
     * “缩放潜在”：“使用潜在空间中的图像缩放。替代方法是从潜在表示生成完整图像，将其放大，然后将其移回潜在空间。”，
     */
    private boolean scaleLatent = false;

    /**
     * “去噪强度”：“确定算法对图像内容的尊重程度。值为0时，什么都不会改变，值为1时，您将得到一个不相关的图像。当值低于1.0时，处理所需的步骤将少于“采样步骤”滑块指定的步骤。”，
     */
    private BigDecimal denoisingStrength = new BigDecimal("0.7");

    /**
     * 脚本相关参数
     */
    private StableDiffusionWebUiScriptBaseParameter stableDiffusionWebUiScriptBaseParameter = new StableDiffusionWebUiScriptBaseParameter();


    /**
     * 获取传回用的参数数组
     * @param data 参数对象
     * @return 转换完成的JSONArray
     */
    public static JSONArray createDataArray(StableDiffusionWebUiText2ImgParameter data) {
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(data.getPrompt());
        jsonArray.add(data.getLowPrompt());
        jsonArray.add(data.getPrompt_Styles1());
        jsonArray.add(data.getPrompt_Styles2());
        jsonArray.add(data.getSteps());
        jsonArray.add(data.getMethod());
        jsonArray.add(data.isRestoreFaces());
        jsonArray.add(data.isTiling());
        jsonArray.add(data.getBatchCount());
        jsonArray.add(data.getBatchSize());
        jsonArray.add(data.getCfgScale());
        jsonArray.add(data.getSeed());
        jsonArray.add(data.getVariationSeed());
        jsonArray.add(data.getVariationStrength());
        jsonArray.add(data.getResizeHeight());
        jsonArray.add(data.getResizeWidth());
        jsonArray.add(data.isInterrogate());
        jsonArray.add(data.getHeight());
        jsonArray.add(data.getWidth());
        jsonArray.add(data.isHighresFix());
        jsonArray.add(data.isScaleLatent());
        jsonArray.add(data.getDenoisingStrength());
        List<Object> scriptParameter = data.getStableDiffusionWebUiScriptBaseParameter().getParameterList();
        jsonArray.addAll(scriptParameter);
        return jsonArray;
    }

    /**
     * 从基本参数中转换完全对象
     * @param data 基本数据
     * @return 传回对象
     */
    public static StableDiffusionWebUiText2ImgParameter convert(AiImageCreateParameter data) {
        StableDiffusionWebUiText2ImgParameter webUiData = new StableDiffusionWebUiText2ImgParameter();
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
        return webUiData;
    }

    public StableDiffusionWebUiScriptBaseParameter getStableDiffusionWebUiScriptBaseParameter() {
        return stableDiffusionWebUiScriptBaseParameter;
    }

    public void setStableDiffusionWebUiScriptBaseParameter(StableDiffusionWebUiScriptBaseParameter stableDiffusionWebUiScriptBaseParameter) {
        this.stableDiffusionWebUiScriptBaseParameter = stableDiffusionWebUiScriptBaseParameter;
    }

    public String getPrompt_Styles1() {
        return prompt_Styles1;
    }

    public void setPrompt_Styles1(String prompt_Styles1) {
        this.prompt_Styles1 = prompt_Styles1;
    }

    public String getPrompt_Styles2() {
        return prompt_Styles2;
    }

    public void setPrompt_Styles2(String prompt_Styles2) {
        this.prompt_Styles2 = prompt_Styles2;
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

    public boolean isInterrogate() {
        return variationCheckBox;
    }

    public void setInterrogate(boolean interrogate) {
        this.variationCheckBox = interrogate;
    }

    public boolean isHighresFix() {
        return highresFix;
    }

    public void setHighresFix(boolean highresFix) {
        this.highresFix = highresFix;
    }

    public boolean isScaleLatent() {
        return scaleLatent;
    }

    public void setScaleLatent(boolean scaleLatent) {
        this.scaleLatent = scaleLatent;
    }
}
