package com.github.cuteluobo.pojo.aidraw;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author CuteLuoBo
 * @date 2022/10/14 13:08
 */
public class StableDiffusionWebUiScriptBaseParameter implements IStableDiffusionWebUiScriptData {

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
    private boolean s1_PutVariablePartsAtStartOfPrompt = false;
    /**
     * 脚本参数-未知结果
     * script:Prompts from file or textbox
     * Show Textbox
     */
    private boolean s2_ShowTextboxCheck = false;
    /**
     * 上传文件的文本解析结果
     * script:Prompts from file or textbox
     * {
     *     data:String,
     *     name:String,
     *     size:int
     * }
     */
    private JSONObject s2_faceRestorationModel = null;

    /**
     * 脚本参数-未知结果
     * script:Prompts from file or textbox
     * 关联的文本框内容
     */
    private String s2_allText = "";

    /**
     * 脚本参数-未知结果
     * script:X/Y plot
     * X type key1
     */
    private String s3_XTypeKey1 = "Seed";

    /**
     * 脚本参数-未知结果
     * script:X/Y plot
     * X type value1
     */
    private String s4_XTypeValue1 = "";

    /**
     * 脚本参数-未知结果
     * script:X/Y plot
     * X type key2
     */
    private String s4_XTypeKey2 = "Steps";

    /**
     * 脚本参数-未知结果
     * script:X/Y plot
     * X type value2
     */
    private String s4_XTypeValue2 = "";

    /**
     * 绘制图例
     * script:X/Y plot
     * 可选参数
     */
    private boolean s4_DrawLegend = true;

    /**
     * Keep -1 for seeds
     * 保留种子
     * script:X/Y plot
     * 可选参数
     */
    private boolean s4_KeepSeeds = false;

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
     * @return 获取排列好的参数列表
     */
    @Override
    public List<Object> getParameterList() {
        List<Object> objectList = new ArrayList<>();
        objectList.add(this.getScript());
        objectList.add(this.isS1_PutVariablePartsAtStartOfPrompt());
        objectList.add(this.isS2_ShowTextboxCheck());
        objectList.add(this.getS2_faceRestorationModel());
        objectList.add(this.getS2_allText());
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

    @Override
    public String getScript() {
        return script;
    }



    public void setScript(String script) {
        this.script = script;
    }

    public boolean isS1_PutVariablePartsAtStartOfPrompt() {
        return s1_PutVariablePartsAtStartOfPrompt;
    }

    public void setS1_PutVariablePartsAtStartOfPrompt(boolean s1_PutVariablePartsAtStartOfPrompt) {
        this.s1_PutVariablePartsAtStartOfPrompt = s1_PutVariablePartsAtStartOfPrompt;
    }

    public boolean isS2_ShowTextboxCheck() {
        return s2_ShowTextboxCheck;
    }

    public void setS2_ShowTextboxCheck(boolean s2_ShowTextboxCheck) {
        this.s2_ShowTextboxCheck = s2_ShowTextboxCheck;
    }

    public JSONObject getS2_faceRestorationModel() {
        return s2_faceRestorationModel;
    }

    public void setS2_faceRestorationModel(JSONObject s2_faceRestorationModel) {
        this.s2_faceRestorationModel = s2_faceRestorationModel;
    }

    public String getS2_allText() {
        return s2_allText;
    }

    public void setS2_allText(String s2_allText) {
        this.s2_allText = s2_allText;
    }

    public String getS3_XTypeKey1() {
        return s3_XTypeKey1;
    }

    public void setS3_XTypeKey1(String s3_XTypeKey1) {
        this.s3_XTypeKey1 = s3_XTypeKey1;
    }

    public String getS4_XTypeValue1() {
        return s4_XTypeValue1;
    }

    public void setS4_XTypeValue1(String s4_XTypeValue1) {
        this.s4_XTypeValue1 = s4_XTypeValue1;
    }

    public String getS4_XTypeKey2() {
        return s4_XTypeKey2;
    }

    public void setS4_XTypeKey2(String s4_XTypeKey2) {
        this.s4_XTypeKey2 = s4_XTypeKey2;
    }

    public String getS4_XTypeValue2() {
        return s4_XTypeValue2;
    }

    public void setS4_XTypeValue2(String s4_XTypeValue2) {
        this.s4_XTypeValue2 = s4_XTypeValue2;
    }

    public boolean isS4_DrawLegend() {
        return s4_DrawLegend;
    }

    public void setS4_DrawLegend(boolean s4_DrawLegend) {
        this.s4_DrawLegend = s4_DrawLegend;
    }

    public boolean isS4_KeepSeeds() {
        return s4_KeepSeeds;
    }

    public void setS4_KeepSeeds(boolean s4_KeepSeeds) {
        this.s4_KeepSeeds = s4_KeepSeeds;
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
