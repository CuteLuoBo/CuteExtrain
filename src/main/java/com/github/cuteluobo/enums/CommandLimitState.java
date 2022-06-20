package com.github.cuteluobo.enums;

/**
 * @author CuteLuoBo
 */
public enum  CommandLimitState {
    /**
     * 数字状态，状态码，信息
     */
    IGNORE(0,"IGNORE","无视消息");

    private Integer state;
    private String code;
    private String text;

    CommandLimitState(Integer state, String code, String text) {
        this.state = state;
        this.code = code;
        this.text = text;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
