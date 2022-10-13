package com.github.cuteluobo.pojo.aidraw;

/**
 * 基本的AI绘图 img->img参数
 * @author CuteLuoBo
 * @date 2022/10/11 16:33
 */
public class BaseAiImageCreateImg2ImgParameter extends BaseAiImageCreateParameter {
    /**
     * BASE64编码的图片格式
     */
    private String base64ImgData;

    public BaseAiImageCreateImg2ImgParameter(String prompt) {
        super(prompt);
    }

    public String getBase64ImgData() {
        return base64ImgData;
    }

    public void setBase64ImgData(String base64ImgData) {
        this.base64ImgData = base64ImgData;
    }
}
