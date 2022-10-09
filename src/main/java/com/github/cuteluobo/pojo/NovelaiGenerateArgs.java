package com.github.cuteluobo.pojo;

import java.util.Random;

/**
 * AI生成参数
 * @author CuteLuoBo
 * @date 2022/10/7 23:48
 */
public class NovelaiGenerateArgs {
    private int height = 768;
    private int width = 512;
    private int n_samples = 1;
    private double noise = 0.2;
    private String sampler = "k_euler_ancestral";
    private int scale = 11;
    private int seed = (int) (System.currentTimeMillis() / 1000);
    private int steps = 33;
    private double strength = 0.7;
    private String uc = "lowres, bad anatomy, bad hands, text, error, missing fingers, extra digit, fewer digits, cropped, worst quality, low quality, normal quality, jpeg artifacts, signature, watermark, username, blurry";
    private int ucPreset = 0;
    /**
     * img2img时使用，传输数据为图片的BASE64编码。不带前缀
     */
    private String image;

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getN_samples() {
        return n_samples;
    }

    public void setN_samples(int n_samples) {
        this.n_samples = n_samples;
    }

    public double getNoise() {
        return noise;
    }

    public void setNoise(double noise) {
        this.noise = noise;
    }

    public String getSampler() {
        return sampler;
    }

    public void setSampler(String sampler) {
        this.sampler = sampler;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public int getSeed() {
        return seed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public double getStrength() {
        return strength;
    }

    public void setStrength(double strength) {
        this.strength = strength;
    }

    public String getUc() {
        return uc;
    }

    public void setUc(String uc) {
        this.uc = uc;
    }

    public int getUcPreset() {
        return ucPreset;
    }

    public void setUcPreset(int ucPreset) {
        this.ucPreset = ucPreset;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
