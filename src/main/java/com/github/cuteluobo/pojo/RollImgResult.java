package com.github.cuteluobo.pojo;

import java.awt.image.BufferedImage;

/**
 * 抽卡
 * @author CuteLuoBo
 * @date 2022-6-18
 */
public class RollImgResult extends RollResultData {
    /**
     * 生成图片信息
     */
    private BufferedImage bufferedImage;

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }
}
