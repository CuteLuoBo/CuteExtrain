package com.github.cuteluobo.pojo.aidraw;

import com.alibaba.fastjson.JSONArray;

/**
 * 接收结果
 *
 * @author CuteLuoBo
 * @date 2022/10/9 13:06
 */
public class StableDiffusionWebUi2ImgResult {
    /**
     * 平均生成时间
     */
    private double average_duration;

    /**
     * 双层嵌套数组
     * 0:生成的图像数据(数组)
     *      -每行为一图片数据，格式：data:image/png;base64,****
     * 1:生成操作的参数json
     * 2:用于显示的html代码
     */
    private JSONArray data;

    /**
     * 总花费时间
     */
    private double duration;

    /**
     * 是否正在生成
     */
    private boolean is_generating;

    public double getAverage_duration() {
        return average_duration;
    }

    public void setAverage_duration(double average_duration) {
        this.average_duration = average_duration;
    }

    public JSONArray getData() {
        return data;
    }

    public void setData(JSONArray data) {
        this.data = data;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public boolean isIs_generating() {
        return is_generating;
    }

    public void setIs_generating(boolean is_generating) {
        this.is_generating = is_generating;
    }
}
