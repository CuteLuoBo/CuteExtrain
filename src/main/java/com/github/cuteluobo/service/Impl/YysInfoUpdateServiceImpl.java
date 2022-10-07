package com.github.cuteluobo.service.Impl;

import cn.pomit.mybatis.ProxyHandlerFactory;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.cuteluobo.mapper.YysUnitMapper;
import com.github.cuteluobo.model.YysUnit;
import com.github.cuteluobo.pojo.RollUnit;
import com.github.cuteluobo.service.InfoUpdateService;
import com.github.cuteluobo.service.InfoUpdateTaskService;
import com.github.cuteluobo.task.BaseDownloader;
import com.github.cuteluobo.task.MyThreadFactory;
import com.github.cuteluobo.util.UnitsInfoUpdateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 阴阳师式神更新服务实现类
 *
 * @author CuteLuoBo
 * @date 2022/9/17 17:07
 */
public class YysInfoUpdateServiceImpl implements InfoUpdateService, InfoUpdateTaskService<YysUnit> {

    private volatile static YysInfoUpdateServiceImpl instance;
    /**
     * 线程池
     */
    private ThreadPoolExecutor pools;

    /**
     * 操作Mapper
     */
    private YysUnitMapper yysUnitMapper;

    /**
     * 式神是否可抽取
     */
    private final int[] interactiveArray = {0, 1};

    /**
     * 式神阶级数组
     */
    private final int[] rarityArray = {1, 2, 3, 4, 5};

    private final Map<Integer, String> rarityTextMap = new HashMap<>();

    private YysInfoUpdateServiceImpl() {
        pools = new ThreadPoolExecutor(0, 2
                , 5, TimeUnit.SECONDS
                , new SynchronousQueue<>(), new MyThreadFactory("yysInfoUpdate"));
        yysUnitMapper = ProxyHandlerFactory.getMapper(YysUnitMapper.class);
        //初始化阶级map
        rarityTextMap.put(1, "N");
        rarityTextMap.put(2, "R");
        rarityTextMap.put(3, "SR");
        rarityTextMap.put(4, "SSR");
        rarityTextMap.put(5, "SP");
    }

    Logger logger = LoggerFactory.getLogger(YysInfoUpdateServiceImpl.class);
    //所有式神列表（无显示是否可抽取）：https://yys.res.netease.com/pc/zt/20161108171335/js/app/all_shishen.json
    //式神头像 https://yys.res.netease.com/pc/zt/20161108171335/data/shishen/{id}.png
    //式神身体：https://yys.res.netease.com/pc/zt/20161108171335/data/shishen_big_beforeAwake/{id}.png
    //素色+花体式神名称+阶级：https://yys.res.netease.com/pc/gw/20180913151832/data/name/{id}.png
    //式神书签类半身：https://yys.res.netease.com/pc/zt/20161108171335/data/mark_btn/{id}.png

    /**
     * interactive是否能抽取到,1=能抽，2=不能直接抽取(联动式神)
     * rarity=稀有度，1=N,2=R,3=SR,4=SSR,5=SP
     */
    //式神列表：https://g37simulator.webapp.163.com/get_heroid_list?rarity={}&interactive={}&material_type=0&page=1&per_page=500

    public static final String UNIT_INFO_URL = "https://g37simulator.webapp.163.com/get_heroid_list?&material_type=0&page=1&per_page=500";

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
     * 更新全部单位信息
     */
    @Override
    public void updateAllUnitInfo() {
        Callable<List<YysUnit>> task = updateAllUnitInfoTask();
        pools.submit(task);
    }

    /**
     * 更新全部单位信息，返回创建的 task
     *
     * @return 本次更新单位信息列表
     */
    @Override
    public Callable<List<YysUnit>> updateAllUnitInfoTask() {
        Callable<List<YysUnit>> task = () -> {
            List<YysUnit> yysUnitList = yysUnitMapper.selectList();
            Map<Integer, YysUnit> idMap = yysUnitList.stream().collect(Collectors.toMap(YysUnit::getUnitId, u -> u));
            BaseDownloader baseDownloader = BaseDownloader.getInstance();
            //变动列表
            List<YysUnit> changeList = new ArrayList<>(20);
            for (int r :
                    rarityArray) {
                String level = rarityTextMap.getOrDefault(r, "UNKOWN");
                for (int i :
                        interactiveArray) {
                    boolean canRoll = i == 0;
                    File infoJsonFile = baseDownloader.downloadFileToTemp(assembleUrl(r, i),r+i+"-"+System.currentTimeMillis()+".json");
                    if (infoJsonFile != null && infoJsonFile.exists()) {
                        String jsonString;
                        //读取文件
                        try (Reader reader = new FileReader(infoJsonFile);) {
                            int tempSize = 1024;
                            char[] temp = new char[tempSize];
                            StringBuilder sb = new StringBuilder();
                            int readCount;
                            while ((readCount = reader.read(temp, 0, tempSize)) != -1) {
                                sb.append(temp,0,readCount);
                            }
                            jsonString = sb.toString().trim();
                        }
                        JSONObject result = JSON.parseObject(jsonString);
                        //结果失败时，打印日志并放弃此次解析
                        boolean success = result.getBoolean("success");
                        if (!success) {
                            logger.warn("{}阶级+可抽取状态{}，数据查询请求失败:", level, i);
                            logger.debug("尝试解析的json:{}",jsonString);
                            break;
                        }
                        String dataString = result.getString("data");
                        //无结果时跳过
                        if (dataString == null || "null".equals(dataString)) {
                            logger.debug("{}阶级+可抽取状态{}，式神数据列表无结果:", level, i);
                            logger.debug("尝试解析的json:{}",jsonString);
                            break;
                        }
                        //解析
                        JSONObject dataObject = JSON.parseObject(dataString);
                        Map<String, Object> dataMap = dataObject.getInnerMap();
                        for (Map.Entry<String, Object> entry :
                                dataMap.entrySet()) {
                            String idString = entry.getKey();
                            int id = Integer.parseInt(idString);
                            JSONObject valueObject = (JSONObject) entry.getValue();
                            //TODO 检验转换map时object的数据
                            String name = valueObject.getString("name");
                            YysUnit newUnit = new YysUnit();
                            newUnit.setUnitId(id);
                            newUnit.setLevel(level);
                            newUnit.setName(name);
                            newUnit.setCanRoll(canRoll);
                            //记录不存在时，添加到变动列表
                            if (idMap.get(id) == null) {
                                changeList.add(newUnit);
                            }
                        }
                    }
                }
            }
            //TODO 实现mapper批量添加
            yysUnitMapper.addAll(changeList);
            return changeList;
        };
        return task;
    }

    /**
     *
     * TODO 更新指定单位信息，返回创建的 task
     *
     * @param unitIdList 指定更新单位ID列表
     * @return 本次更新单位信息列表
     */
    @Override
    public Callable<List<YysUnit>> updateAssignUnitInfoTask(List<Integer> unitIdList) {
        return null;
    }

    /**
     * 更新指定单位信息
     *
     * @param unitIdList 式神ID列表
     */
    @Override
    public void updateAssignUnitInfo(List<Integer> unitIdList) {

    }

    private URL assembleUrl(int rarity, int interactive) throws MalformedURLException {
        return new URL(UNIT_INFO_URL + "&rarity=" + rarity + "&interactive=" + interactive);
    }

    /**
     * 返回实例
     *
     * @return 实例
     */
    public static YysInfoUpdateServiceImpl getInstance() {
        if (instance == null) {
            synchronized (YysInfoUpdateServiceImpl.class) {
                instance = new YysInfoUpdateServiceImpl();
            }
        }
        return instance;
    }
}
