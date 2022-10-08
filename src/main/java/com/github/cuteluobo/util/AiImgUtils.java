package com.github.cuteluobo.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.cuteluobo.pojo.NovelaiGenerateArgs;
import com.github.cuteluobo.repository.GlobalConfig;
import io.ktor.http.HttpStatusCode;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Base64;
import java.util.stream.Collectors;

/**
 * Ai图片工具类
 * @author CuteLuoBo
 * @date 2022/10/7 23:17
 */
public class AiImgUtils {
    static class PostData{
        String input;
        String model;
        NovelaiGenerateArgs parameters;

        public PostData(String input, String model, NovelaiGenerateArgs param) {
            this.input = input;
            this.model = model;
            this.parameters = param;
        }

        public String getInput() {
            return input;
        }

        public void setInput(String input) {
            this.input = input;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public NovelaiGenerateArgs getParameters() {
            return parameters;
        }

        public void setParameters(NovelaiGenerateArgs parameters) {
            this.parameters = parameters;
        }
    }
    public static byte[] getImg(String text, String model, Boolean noH) throws URISyntaxException, IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newBuilder().build();
        PostData postData = new PostData(TranslateUtils.autoToEN(tagsSplice(text)), model, new NovelaiGenerateArgs());
        String json = JSON.toJSONString(postData);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(new URI("https://api.novelai.net/ai/generate-image"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("content-type","application/json")
                .headers("authorization", GlobalConfig.getInstance().getNovelaiToken())
                .build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        //正常返回结果时
        if (response.statusCode() == 200 || response.statusCode() == 201) {
            String result = response.body().trim();
            String data = result.split("data:")[1];
            byte[] bytes = Base64.getDecoder().decode(data);
            return bytes;
        }
        return new byte[0];
    }

    //TODO 完成机器人指令和图片上传


    private static String tagsSplice(String text) {
        //中英文逗号替换
        text = text.replace("，", ",");
        //空格分割
        String[] temp = text.split(" ");
        return Arrays.stream(temp).map(String::trim).collect(Collectors.joining(","));
    }
}
