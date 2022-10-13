package com.github.cuteluobo.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.cuteluobo.excepiton.ServiceException;
import com.github.cuteluobo.pojo.aidraw.*;
import com.github.cuteluobo.service.AiDrawService;
import com.github.cuteluobo.service.SystemLoginService;
import com.github.cuteluobo.task.MyThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Base64;
import java.util.Optional;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 对接webui的绘图实现类
 * @author CuteLuoBo
 * @date 2022/10/11 16:49
 */
public class WebUiAiDrawServiceImpl implements AiDrawService{
    static Logger logger = LoggerFactory.getLogger(WebUiAiDrawServiceImpl.class);
    private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(1, 10, 30, TimeUnit.SECONDS
            , new SynchronousQueue<>(true)
            , new MyThreadFactory("WebUiAiDrawServiceImpl"));
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(60)).version(HttpClient.Version.HTTP_1_1).executor(THREAD_POOL_EXECUTOR).build();

    private String mainUrl;
    private String token;
    private String userName;
    private String password;

    public WebUiAiDrawServiceImpl(String mainUrl, String token) {
        this.mainUrl = mainUrl;
        this.token = token;
    }

    public WebUiAiDrawServiceImpl(String mainUrl, String userName, String password){
        this.mainUrl = mainUrl;
        this.userName = userName;
        this.password = password;
    }

    public WebUiAiDrawServiceImpl(String mainUrl, String token, String userName, String password) {
        this.mainUrl = mainUrl;
        this.token = token;
        this.userName = userName;
        this.password = password;
    }

    /**
     * 文本->图片
     * @param parameter 图片生成参数
     * @return 图片的字节数据，长度为0时表示生成错误或无数据
     * @throws IOException 网络IO异常
     * @throws InterruptedException 网络线程异常
     * @throws URISyntaxException 请求API创建URI异常
     * @throws ServiceException 获取异常/任务执行异常
     */
    @Override
    public byte[] txt2img(AiImageCreateParameter parameter) throws IOException, InterruptedException, URISyntaxException, ServiceException {
        if (token == null) {
            login();
        }
        StableDiffusionWebUiText2ImgPostObject postData = new StableDiffusionWebUiText2ImgPostObject();
        StableDiffusionWebUiText2ImgParameter parameter1 = StableDiffusionWebUiText2ImgParameter.convert(parameter);
        postData.setData(StableDiffusionWebUiText2ImgParameter.createDataArray(parameter1));
        String json = JSON.toJSONString(postData);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(new URI(mainUrl+"/api/predict/"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("content-type","application/json")
                .headers("Cookie", "access-token="+token)
                .build();
        HttpResponse<String> response = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        //正常返回结果时
        if (response.statusCode() == 200 || response.statusCode() == 201) {
            String result = response.body().trim();
            StableDiffusionWebUiText2ImgResult stableDiffusionWebUiText2ImgResult = JSON.parseObject(result, StableDiffusionWebUiText2ImgResult.class);
            if (stableDiffusionWebUiText2ImgResult != null) {
                JSONArray dataArray = stableDiffusionWebUiText2ImgResult.getData();
                if (dataArray != null && !dataArray.isEmpty()) {
                    JSONArray imageDataArray = dataArray.getJSONArray(0);
                    if (imageDataArray != null && !imageDataArray.isEmpty()) {
                        String data = imageDataArray.getString(0).split("base64,")[1];
                        byte[] bytes = Base64.getDecoder().decode(data);
                        return bytes;
                    }
                }
            }
        } else {
            //令牌失效
            if (response.statusCode() == 401) {
                token = null;
                login();
            }
            logger.error("请求错误,状态码:{},响应文本:{}", response.statusCode(), response.body());
        }
        return new byte[0];
    }

    @Override
    public byte[] img2img(BaseAiImageCreateImg2ImgParameter parameter) {
        //TODO 待完成
        return new byte[0];
    }

    /**
     * 调用内部用户密码进行登录
     * @return 返回此次登录的token
     */
    public boolean login() throws URISyntaxException, IOException, InterruptedException, ServiceException {
        if (userName == null || password == null) {
            throw new ServiceException("token失效且账号信息未填写");
        }
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(new URI(mainUrl+"/login"))
                .POST(HttpRequest.BodyPublishers.ofString("username="+userName+"&"+"password="+password))
                .header("content-type","application/x-www-form-urlencoded")
                .build();
        HttpResponse<String> response = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        //从cookie中取token
        if (response.statusCode() ==200 ||  response.statusCode() == 302) {
            Optional<String> cookie = response.headers().firstValue("set-cookie");
            if (cookie.isPresent()) {
                String cookieString = cookie.get();
                if (cookieString.trim().length() != 0) {
                    int start = cookieString.indexOf("=");
                    int end = cookieString.indexOf(";");
                    if (start != -1 && end != -1) {
                        token = cookieString.substring(start + 1, end);
                        return true;
                    }
                }
            }
        } else if (response.statusCode() == 422) {
            logger.error("尝试登录失败，返回结果:{}",response.body());
            throw new ServiceException("尝试登录失败，请检查服务器登录信息");
        }
        return false;
    }

    public String getMainUrl() {
        return mainUrl;
    }

    public void setMainUrl(String mainUrl) {
        this.mainUrl = mainUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
