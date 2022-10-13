package com.github.cuteluobo.service;

import com.github.cuteluobo.excepiton.ServiceException;
import com.github.cuteluobo.pojo.aidraw.AiImageCreateParameter;
import com.github.cuteluobo.pojo.aidraw.BaseAiImageCreateImg2ImgParameter;
import com.github.cuteluobo.pojo.aidraw.BaseAiImageCreateParameter;
import com.github.cuteluobo.task.MyThreadFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author CuteLuoBo
 * @date 2022/10/11 15:36
 */
public interface AiDrawService {

    /**
     * 文本->图片
     * @param parameter 图片生成参数
     * @return 图片的字节数据，长度为0时表示生成错误或无数据
     * @throws IOException 网络IO异常
     * @throws InterruptedException 网络线程异常
     * @throws URISyntaxException 请求API创建URI异常
     * @throws ServiceException 获取异常/任务执行异常
     */
    public byte[] txt2img(AiImageCreateParameter parameter) throws IOException, InterruptedException, URISyntaxException, ServiceException;

    public byte[] img2img(BaseAiImageCreateImg2ImgParameter parameter);
}
