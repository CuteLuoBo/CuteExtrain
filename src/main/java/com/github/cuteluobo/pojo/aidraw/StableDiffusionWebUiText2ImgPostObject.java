package com.github.cuteluobo.pojo.aidraw;

import com.alibaba.fastjson.JSONArray;

/**
 * 通信约定对象
 *
 * @author CuteLuoBo
 * @date 2022/10/9 13:04
 */
public class StableDiffusionWebUiText2ImgPostObject {
    private JSONArray data;
    /**
     * 通信类型，11=提交操作
     */
    private int fn_index = 11;

    /**
     * session哈希
     */
    private String session_hash;

    public JSONArray getData() {
        return data;
    }

    public void setData(JSONArray data) {
        this.data = data;
    }

    public int getFn_index() {
        return fn_index;
    }

    public void setFn_index(int fn_index) {
        this.fn_index = fn_index;
    }

    public String getSession_hash() {
        return session_hash;
    }

    public void setSession_hash(String session_hash) {
        this.session_hash = session_hash;
    }
}
