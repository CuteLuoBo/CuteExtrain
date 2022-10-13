package com.github.cuteluobo.pojo.aidraw;

import com.github.cuteluobo.common.config.ConfigField;

/**
 * Web Ui服务器列表配置对象
 *
 * @author CuteLuoBo
 * @date 2022/10/11 22:55
 */
public class ConfigWebUiServer {

    /**
     * 通信链接
     */
    @ConfigField(description = "通信链接",value = "http://xxx")
    private String url;
    /**
     * 登录用户名
     */
    @ConfigField(description = "用户名",value = "username")
    private String username;
    /**
     * 密码
     */
    @ConfigField(description = "密码",value = "password")
    private String password;
    /**
     * 访问token
     */
    @ConfigField(description = "访问令牌",value = "token")
    private String token;
    /**
     * 是否允许NSFW内容生成
     */
    @ConfigField(description = "通信链接",value = "false")
    private boolean allowNsfw ;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isAllowNsfw() {
        return allowNsfw;
    }

    public void setAllowNsfw(boolean allowNsfw) {
        this.allowNsfw = allowNsfw;
    }
}
