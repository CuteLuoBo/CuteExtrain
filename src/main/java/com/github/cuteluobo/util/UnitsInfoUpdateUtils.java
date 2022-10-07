package com.github.cuteluobo.util;

import cn.pomit.mybatis.ProxyHandlerFactory;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.cuteluobo.mapper.YysUnitMapper;
import com.github.cuteluobo.model.YysUnit;
import com.github.cuteluobo.repository.ResourceLoader;
import com.github.cuteluobo.task.BaseDownloader;
import com.github.cuteluobo.task.IDownloader;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * 式神信息更新工具
 *
 * @author CuteLuoBo
 * @date 2022/9/9 16:28
 */
public class UnitsInfoUpdateUtils {
    static Logger logger = LoggerFactory.getLogger(UnitsInfoUpdateUtils.class);
    //TODO 增加从官网下载式神图片的服务类
    //所有式神列表（无显示是否可抽取）：https://yys.res.netease.com/pc/zt/20161108171335/js/app/all_shishen.json
    //式神头像 https://yys.res.netease.com/pc/zt/20161108171335/data/shishen/{id}.png
    //式神身体：https://yys.res.netease.com/pc/zt/20161108171335/data/shishen_big_beforeAwake/{id}.png
    //素色+花体式神名称+阶级：https://yys.res.netease.com/pc/gw/20180913151832/data/name/{id}.png
    //式神书签类半身：https://yys.res.netease.com/pc/zt/20161108171335/data/mark_btn/{id}.png

    /**
     * interactive是否能抽取到,1=能抽，2=不能直接抽取(联动式神)
     * rarity=稀有度，1=N,2=R,3=SR,4=SSR,5=SP
     * */
    //式神列表：https://g37simulator.webapp.163.com/get_heroid_list?rarity={}&interactive={}&material_type=0&page=1&per_page=500

    /**
     * 式神头像图片URL前缀
     */
    public static final String UNIT_HEAD_IMAGE_URL_PREFIX = "https://yys.res.netease.com/pc/zt/20161108171335/data/shishen/";
    /**
     * 式神身体图片URL前缀
     */
    public static final String UNIT_BODY_IMAGE_URL_PREFIX = "https://yys.res.netease.com/pc/zt/20161108171335/data/shishen_big_beforeAwake/";
    /**
     * 默认保存的图片后缀
     */
    public static final String NORMAL_SAVE_IMAGE_SUFFIX = ".png";


    /**
     * 增量添加式神列表到数据库中
     * 将会从数据库中已有的ID往后累加，然后检索
     */
    public static Callable<List<YysUnit>> appendUnitsListTask() {
        //获取数据库中最新的式神ID
        YysUnitMapper yysUnitMapper = ProxyHandlerFactory.getMapper(YysUnitMapper.class);
        List<YysUnit> yysUnitList = yysUnitMapper.selectList();
//        int lastUnitId = 0;
//        YysUnit yysUnit = yysUnitList.get(0);
//        if (yysUnit != null) {
//            lastUnitId = yysUnit.getUnitId();
//        }
//        final int finalLastUnitId = lastUnitId;
        Callable<List<YysUnit>> task = () -> {
            File jsonFile = BaseDownloader.getInstance().downloadFileToTemp(new URL("https://yys.res.netease.com/pc/zt/20161108171335/js/app/all_shishen.json"));
            if (jsonFile != null && jsonFile.exists()) {
                String jsonString;
                //读取文件
                try (Reader reader = new FileReader(jsonFile);) {
                    int tempSize = 1024;
                    char[] temp = new char[tempSize];
                    StringBuilder sb = new StringBuilder();
                    int readCount;
                    while ((readCount = reader.read(temp, 0, tempSize)) != -1) {
                        sb.append(temp,0,readCount);
                    }
                    jsonString = sb.toString().trim();
                }
                List<YysUnit> newUnitList = new ArrayList<>(10);
                //解析
                List<JSONObject> jsonObjectList = JSON.parseArray(jsonString, JSONObject.class);
                int unitId;
                for (JSONObject jsonObject :
                        jsonObjectList) {
                    unitId = jsonObject.getIntValue("id");
                    //ID解析错误时，跳过
                    if (unitId == 0) {
                        break;
                    }
                    YysUnit old = yysUnitMapper.selectOneByUnitId(unitId);
                    if (old == null) {
                        //组装对象
                        YysUnit newUnit = new YysUnit();
                        newUnit.setUnitId(unitId);
                        newUnit.setLevel(jsonObject.getString("level"));
                        String name = jsonObject.getString("name");
                        newUnit.setName(name);
                        newUnit.setCanRoll(true);
                        //添加到数据库中，发生错误时，不添加进记录
                        try {
                            yysUnitMapper.addOne(newUnit);
                            newUnitList.add(newUnit);
                        } catch (Exception exception) {
                            logger.error("尝试添加新式神资料失败,ID:{},名称:{}", unitId, name, exception);
                            throw exception;
                        }
                    }
                }
                return newUnitList;
            }
            return null;
        };
        return task;
    }

    /**
     * 更新式神图片任务
     *
     * @param unitIdList 需要更新的式神ID列表
     * @return 任务->返回结果为成功更新的式神ID列表
     */
    public static Callable<List<Integer>> updateUnitImageTask(@NotNull List<Integer> unitIdList) {
        Callable<List<Integer>> callable = () -> {
            StringBuilder sb = new StringBuilder();
            List<URL> headUrl = new ArrayList<>(unitIdList.size());
            List<URL> bodyUrl = new ArrayList<>(unitIdList.size());
            for (Integer id :
                    unitIdList) {
                headUrl.add(new URL(UNIT_HEAD_IMAGE_URL_PREFIX + id + NORMAL_SAVE_IMAGE_SUFFIX));
                bodyUrl.add(new URL(UNIT_BODY_IMAGE_URL_PREFIX + id + NORMAL_SAVE_IMAGE_SUFFIX));
            }
            IDownloader downloader = BaseDownloader.getInstance();
            //下载
            downloader.downloadFileList(headUrl, null,
                    Paths.get(ResourceLoader.INSTANCE.getNormalResourceFolder().getAbsolutePath(), ResourceLoader.YYS_UNIT_IMAGE_HEAD));
            downloader.downloadFileList(bodyUrl, null,
                    Paths.get(ResourceLoader.INSTANCE.getNormalResourceFolder().getAbsolutePath(), ResourceLoader.YYS_UNIT_IMAGE_BODY));
            return unitIdList;
        };
        return callable;
    }
}
