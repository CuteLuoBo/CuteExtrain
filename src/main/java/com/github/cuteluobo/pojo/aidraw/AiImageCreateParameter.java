package com.github.cuteluobo.pojo.aidraw;

/**
 * @author CuteLuoBo
 * @date 2022/10/11 17:14
 */
public class AiImageCreateParameter extends BaseAiImageCreateParameter{
    /**
     * 单次生成图片数量(1-16)
     */
    private int batchCount = 1;

    /**
     * 批量大小，跟显存占用相关(1-8)
     */
    private int batchSize = 1;

    public AiImageCreateParameter(String prompt) {
        super(prompt);
    }
    public AiImageCreateParameter() {
        super();
    }

    public int getBatchCount() {
        return batchCount;
    }

    public void setBatchCount(int batchCount) {
        this.batchCount = batchCount;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }
}
