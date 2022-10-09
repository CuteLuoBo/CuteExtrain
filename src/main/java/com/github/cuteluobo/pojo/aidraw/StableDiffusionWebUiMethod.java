package com.github.cuteluobo.pojo.aidraw;

/**
 * StableDiffusionWebUi的相关生成方法
 * @author CuteLuoBo
 * @date 2022/10/9 12:11
 */
public enum StableDiffusionWebUiMethod {
    /**StableDiffusionWebUi的相关生成方法*/
    Euler_A("Euler a"),Euler("Euler"),LMS("LMS"),Heun("Heun"),DPM2("DPM2"),DPM2_A("DPM2 a"),DPM_FAST("DPM fast")
    ,DPM_adaptive("DPM adaptive"),LMS_Karras("LMS Karras"),DPM2_Karras("DPM2 Karras"),DPM2_A_Karras("DPM2 a Karras")
    ;
    private String argsText;

    StableDiffusionWebUiMethod(String argsText) {
        this.argsText = argsText;
    }

    public String getArgsText() {
        return argsText;
    }
}
