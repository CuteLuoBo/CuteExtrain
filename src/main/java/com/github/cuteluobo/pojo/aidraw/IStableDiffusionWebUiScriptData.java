package com.github.cuteluobo.pojo.aidraw;

import java.util.List;

/**
 * StableDiffusion-WebUi 脚本数据接口
 * @author CuteLuoBo
 * @date 2022/10/14 13:09
 */
public interface IStableDiffusionWebUiScriptData {
    /**
     * @return 获取脚本主参数
     */
    String getScript();

    /**
     * @return 获取排列好的参数列表
     */
    List<Object> getParameterList();
}
