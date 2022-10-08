package com.github.cuteluobo.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.cuteluobo.pojo.NovelaiGenerateArgs;
import com.github.cuteluobo.pojo.YouDaoTranslateArgs;
import com.github.cuteluobo.repository.GlobalConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 翻译工具
 *  有道翻译API文档：https://ai.youdao.com/DOCSIRMA/html/%E8%87%AA%E7%84%B6%E8%AF%AD%E8%A8%80%E7%BF%BB%E8%AF%91/API%E6%96%87%E6%A1%A3/%E6%96%87%E6%9C%AC%E7%BF%BB%E8%AF%91%E6%9C%8D%E5%8A%A1/%E6%96%87%E6%9C%AC%E7%BF%BB%E8%AF%91%E6%9C%8D%E5%8A%A1-API%E6%96%87%E6%A1%A3.html
 * @author CuteLuoBo
 * @date 2022/10/8 11:28
 */
public class TranslateUtils {
    static Logger logger = LoggerFactory.getLogger(TranslateUtils.class);
    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 10, 30, TimeUnit.SECONDS
            , new SynchronousQueue<Runnable>(true)
            , Executors.defaultThreadFactory());
    private static HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).executor(threadPoolExecutor).build();
    public static String autoToEN(String text) throws URISyntaxException, IOException, InterruptedException {
//        HttpClient httpClient = HttpClient.newBuilder().build();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(new URI("https://fanyi.youdao.com/translate?&doctype=json&type=AUTO&i="+text))
                .GET()
                .header("content-type","application/json")
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        //正确http状态码时，进行读取
        if (response.statusCode() == 200 || response.statusCode() == 201) {
            String responseString = response.body();
            JSONObject responseJsonObject = JSON.parseObject(responseString);
            int errorCode = responseJsonObject.getIntValue("errorCode");
            //出现错误时，打印日志
            if (errorCode != 0) {
                logger.error("翻译API调用错误，返回错误码:{},全部返回结果：{}", errorCode, responseString);
            } else {
                //拼装并返回结果
                JSONArray array = responseJsonObject.getJSONArray("translateResult");
                JSONArray array1 = array.getJSONArray(0);
                JSONObject resultObject = array1.getJSONObject(0);
                return resultObject.getString("tgt");
            }
        }
        return null;
    }

//    public static String autoToEN(String text) throws URISyntaxException, IOException, InterruptedException {
//        YouDaoTranslateArgs translateArgs = new YouDaoTranslateArgs(text, GlobalConfig.YOU_DAO_TRANSLATE_APP_KEY, GlobalConfig.YOU_DAO_TRANSLATE_APP_SECRET);
//        translateArgs.signNow();
//        HttpClient httpClient = HttpClient.newBuilder().build();
//        String json = JSON.toJSONString(translateArgs);
//        HttpRequest httpRequest = HttpRequest.newBuilder()
//                .uri(new URI("https://openapi.youdao.com/api"))
//                .POST(HttpRequest.BodyPublishers.ofString(json))
//                .header("content-type","application/json")
//                .build();
//        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
//        //正确http状态码时，进行读取
//        if (response.statusCode() == 200 || response.statusCode() == 201) {
//            String responseString = response.body();
//            JSONObject responseJsonObject = JSON.parseObject(responseString);
//            int errorCode = responseJsonObject.getIntValue("errorCode");
//            //出现错误时，打印日志
//            if (errorCode != 0) {
//                logger.error("翻译API调用错误，返回错误码:{},全部返回结果：{}", errorCode, responseString);
//            } else {
//                //拼装并返回结果
//                JSONArray array = responseJsonObject.getJSONArray("translation");
//                String[] resultArray = array.toArray(new String[0]);
//                return Arrays.stream(resultArray).map(String::trim).collect(Collectors.joining(","));
//            }
//        }
//        return null;
//    }
}
