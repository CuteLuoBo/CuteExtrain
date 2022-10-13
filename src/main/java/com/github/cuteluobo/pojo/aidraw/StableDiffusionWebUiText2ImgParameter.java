package com.github.cuteluobo.pojo.aidraw;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;

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
    private int ResizeHeight = 0;

    /**
     * “从宽度度调整种子大小”：“尝试生成与使用相同种子以指定分辨率生成的图片类似的图片”，
     * (0-2048),64/step
     */
    private int ResizeWidth = 0;

    /**
     * 从现有图像重建提示并将其放入提示字段。
     */
    private boolean interrogate = false;

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
    private boolean ScaleLatent = false;

    /**
     * “去噪强度”：“确定算法对图像内容的尊重程度。值为0时，什么都不会改变，值为1时，您将得到一个不相关的图像。当值低于1.0时，处理所需的步骤将少于“采样步骤”滑块指定的步骤。”，
     */
    private BigDecimal DenoisingStrength = new BigDecimal("0.7");

    /**
     * 图片处理脚本
     * “None”：“不要做任何特别的事”，
     *
     * “Prompt matrix”：“使用竖线字符（|）将提示分成多个部分，脚本将为每个部分的组合创建一张图片（第一部分除外，它将以所有组合出现）”，
     *
     * “X/Y plot”：“创建一个网格，其中图像将具有不同的参数。使用下面的输入指定哪些参数将由列和行共享”，
     *
     * “Custom code”：“运行Python代码。仅限高级用户。必须使用--allow代码运行程序才能使其正常工作”，
     */
    private String script = "None";

    /**
     * 脚本参数
     * script:Prompt matrix
     * Put variable parts at start of prompt
     */
    private boolean scriptArgs1 = false;
    /**
     * 脚本参数-未知结果
     * script:Prompts from file or textbox
     * Show Textbox
     */
    private boolean scriptArgs2 = false;
    /**
     * 上传文件的文本解析结果
     * script:Prompts from file or textbox
     * {
     *     data:String,
     *     name:String,
     *     size:int
     * }
     */
    private JSONObject faceRestorationModel = null;

    /**
     * 脚本参数-未知结果
     * script:Prompts from file or textbox
     * 关联的文本框内容
     */
    private String scriptArgs4 = "";

    /**
     * 脚本参数-未知结果
     * script:X/Y plot
     * X type key1
     */
    private String scriptArgs5 = "Seed";

    /**
     * 脚本参数-未知结果
     * script:X/Y plot
     * X type value1
     */
    private String scriptArgs6 = "";

    /**
     * 脚本参数-未知结果
     * script:X/Y plot
     * X type key2
     */
    private String scriptArgs7 = "Steps";

    /**
     * 脚本参数-未知结果
     * script:X/Y plot
     * X type value2
     */
    private String scriptArgs8 = "";

    /**
     * 绘制图例
     * script:X/Y plot
     * 可选参数
     */
    private boolean DrawLegend = true;

    /**
     * Keep -1 for seeds
     * 保留种子
     * script:X/Y plot
     * 可选参数
     */
    private boolean KeepSeeds = false;

    /**
     * 未知参数
     */
    private String unkown1 = null;

    /**
     * 传递的json，可能用于实际解析执行
     * 或者是上次的执行结果
     */
    private String json = "";

    /**
     * 传递的html代码，可能是上次执行结果
     */
    private String html = "";

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
        jsonArray.add(data.getScript());
        jsonArray.add(data.isScriptArgs1());
        jsonArray.add(data.isScriptArgs2());
        jsonArray.add(data.getFaceRestorationModel());
        jsonArray.add(data.getScriptArgs4());
        jsonArray.add(data.getScriptArgs5());
        jsonArray.add(data.getScriptArgs6());
        jsonArray.add(data.getScriptArgs7());
        jsonArray.add(data.getScriptArgs8());
        jsonArray.add(data.isDrawLegend());
        jsonArray.add(data.isKeepSeeds());
        jsonArray.add(data.getUnkown1());
        jsonArray.add(data.getJson());
        jsonArray.add(data.getHtml());
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
        return ResizeHeight;
    }

    public void setResizeHeight(int resizeHeight) {
        ResizeHeight = resizeHeight;
    }

    public int getResizeWidth() {
        return ResizeWidth;
    }

    public void setResizeWidth(int resizeWidth) {
        ResizeWidth = resizeWidth;
    }

    public boolean isInterrogate() {
        return interrogate;
    }

    public void setInterrogate(boolean interrogate) {
        this.interrogate = interrogate;
    }

    public boolean isHighresFix() {
        return highresFix;
    }

    public void setHighresFix(boolean highresFix) {
        this.highresFix = highresFix;
    }

    public boolean isScaleLatent() {
        return ScaleLatent;
    }

    public void setScaleLatent(boolean scaleLatent) {
        ScaleLatent = scaleLatent;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public boolean isScriptArgs1() {
        return scriptArgs1;
    }

    public void setScriptArgs1(boolean scriptArgs1) {
        this.scriptArgs1 = scriptArgs1;
    }

    public boolean isScriptArgs2() {
        return scriptArgs2;
    }

    public void setScriptArgs2(boolean scriptArgs2) {
        this.scriptArgs2 = scriptArgs2;
    }

    public JSONObject getFaceRestorationModel() {
        return faceRestorationModel;
    }

    public void setFaceRestorationModel(JSONObject faceRestorationModel) {
        this.faceRestorationModel = faceRestorationModel;
    }

    public String getScriptArgs4() {
        return scriptArgs4;
    }

    public void setScriptArgs4(String scriptArgs4) {
        this.scriptArgs4 = scriptArgs4;
    }

    public String getScriptArgs5() {
        return scriptArgs5;
    }

    public void setScriptArgs5(String scriptArgs5) {
        this.scriptArgs5 = scriptArgs5;
    }

    public String getScriptArgs6() {
        return scriptArgs6;
    }

    public void setScriptArgs6(String scriptArgs6) {
        this.scriptArgs6 = scriptArgs6;
    }

    public String getScriptArgs7() {
        return scriptArgs7;
    }

    public void setScriptArgs7(String scriptArgs7) {
        this.scriptArgs7 = scriptArgs7;
    }

    public String getScriptArgs8() {
        return scriptArgs8;
    }

    public void setScriptArgs8(String scriptArgs8) {
        this.scriptArgs8 = scriptArgs8;
    }

    public boolean isDrawLegend() {
        return DrawLegend;
    }

    public void setDrawLegend(boolean drawLegend) {
        DrawLegend = drawLegend;
    }

    public boolean isKeepSeeds() {
        return KeepSeeds;
    }

    public void setKeepSeeds(boolean keepSeeds) {
        KeepSeeds = keepSeeds;
    }

    public String getUnkown1() {
        return unkown1;
    }

    public void setUnkown1(String unkown1) {
        this.unkown1 = unkown1;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}
