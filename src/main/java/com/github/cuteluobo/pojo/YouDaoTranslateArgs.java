package com.github.cuteluobo.pojo;

import com.alibaba.fastjson.annotation.JSONField;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * 有道翻译API通信参数
 * @author CuteLuoBo
 * @date 2022/10/8 11:37
 */
public class YouDaoTranslateArgs {
    private String q;
    private String from = "auto";
    private String to = "en";
    private String appKey;
    @JSONField(serialize = false)
    private String appSecret;
    private String salt = UUID.randomUUID().toString();
    private String sign;
    private String signType = "v3";
    private final String curtime = String.valueOf(System.currentTimeMillis() / 1000);

    public YouDaoTranslateArgs(String q, String from, String to, String appKey, String appSecret) {
        this.q = q;
        this.from = from;
        this.to = to;
        this.appKey = appKey;
        this.appSecret = appSecret;
    }

    public YouDaoTranslateArgs(String q, String to, String appKey, String appSecret) {
        this.q = q;
        this.to = to;
        this.appKey = appKey;
        this.appSecret = appSecret;
    }

    public YouDaoTranslateArgs(String q, String appKey, String appSecret) {
        this.q = q;
        this.appKey = appKey;
        this.appSecret = appSecret;
    }

    /**
     * 进行签名
     */
    public void signNow() {
        String signString = appKey + truncate(q) + salt + curtime + appSecret;
        sign = getDigest(signString);
    }

    private static String getDigest(String string) {
        if (string == null) {
            return null;
        }
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        byte[] btInput = string.getBytes(StandardCharsets.UTF_8);
        try {
            MessageDigest mdInst = MessageDigest.getInstance("SHA-256");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    private static String truncate(String q) {
        if (q == null) {
            return null;
        }
        int len = q.length();
        String result;
        return len <= 20 ? q : (q.substring(0, 10) + len + q.substring(len - 10, len));
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getCurtime() {
        return curtime;
    }
}
