package com.github.cuteluobo.service.Impl;

import cn.pomit.mybatis.configuration.MybatisConfiguration;
import com.github.cuteluobo.enums.RollModel;
import com.github.cuteluobo.enums.YysRoll;
import com.github.cuteluobo.pojo.RollImgResult;
import com.github.cuteluobo.pojo.RollResultData;
import com.github.cuteluobo.pojo.RollResultUnit;
import com.github.cuteluobo.pojo.RollUnit;
import com.github.cuteluobo.util.YysRollUtils;
import org.apache.ibatis.session.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 阴阳师抽卡图片生成类测试
 */
class YysImgOutputServiceImplTest {
    YysImgOutputServiceImpl yysImgOutputService = YysImgOutputServiceImpl.INSTANCE;

    RollResultData rollResultData = YysRollUtils.generateTestData(1, 2, 30, 40, 0);
    String title = "阴阳师模拟抽卡v0.1Beta";

    static File testFile = new File("testInfo"+File.separator);

    /**
     * TODO 对多个需要装配数据库的Test类，设置统一处理方法
     * 初始化数据库配置文件
     */
    private static void setUpDataBase() {
        Properties properties = new Properties();
        properties.put("mybatis.mapper.scan", "com.github.cuteluobo.mapper");
        properties.put("mybatis.datasource.type", "POOLED");
        properties.put("mybatis.datasource.driver", "org.sqlite.JDBC");
        URL url = YysRollServiceImplTest.class.getClassLoader().getResource("database.sqlite");
        properties.put("mybatis.datasource.url", "jdbc:sqlite:" + url);
        properties.put("mybatis.datasource.username", "");
        properties.put("mybatis.datasource.password", "");
        properties.setProperty("mapUnderscoreToCamelCase", "true");
        properties.put("mybatis.logImpl", "STDOUT_LOGGING");
        MybatisConfiguration.initConfiguration(properties);
        Configuration configuration = MybatisConfiguration.getSqlSessionFactory().getConfiguration();
        configuration.setMapUnderscoreToCamelCase(true);
    }

    @BeforeAll
    static void setUp() {
        setUpDataBase();
        if (!testFile.exists()) {
            testFile.mkdirs();
        }
    }

    /**
     * 普通抽卡
     * @throws IOException
     */
    @Test
    void createImgNormal() throws IOException {
        //普通抽卡
        rollResultData = YysRollServiceImpl.INSTANCE.roll(100,null);
        RollImgResult rollImgResult = yysImgOutputService.createImgResult(rollResultData, "普通100", RollModel.normal);
        assertNotNull(rollImgResult);
        assertNotNull(rollImgResult.getBufferedImage());
        writeOnTestFolder(rollImgResult.getBufferedImage(),"normal");
        //普通UP抽卡
        rollResultData = YysRollServiceImpl.INSTANCE.rollUp(100,true,3,0.05f);
        rollImgResult = yysImgOutputService.createImgResult(rollResultData, "普通100UP", RollModel.normal);
        assertNotNull(rollImgResult);
        assertNotNull(rollImgResult.getBufferedImage());
        writeOnTestFolder(rollImgResult.getBufferedImage(),"normal");
    }

    /**
     * 活动定向抽卡
     * @throws IOException
     */
    @Test
    void createImgSpecify() throws IOException {
        //活动定向抽卡-全图
        rollResultData = YysRollServiceImpl.INSTANCE.rollTextForSpecifyUnit(new RollUnit(1, 1L, YysRoll.SSR.getLevel(), "test"), true);
        RollImgResult rollImgResult = yysImgOutputService.createImgResult(rollResultData, "活动定向-全图", RollModel.specify);
        assertNotNull(rollImgResult);
        assertNotNull(rollImgResult.getBufferedImage());
        writeOnTestFolder(rollImgResult.getBufferedImage(),"normal");

        //活动定向抽卡-非全图
        rollResultData = YysRollServiceImpl.INSTANCE.rollTextForSpecifyUnit(new RollUnit(1, 1L, YysRoll.SSR.getLevel(), "test"), false);
        rollImgResult = yysImgOutputService.createImgResult(rollResultData, "活动定向-非全图", RollModel.specify);
        assertNotNull(rollImgResult);
        assertNotNull(rollImgResult.getBufferedImage());
        writeOnTestFolder(rollImgResult.getBufferedImage(),"normal");
    }

    /**
     * 普通定向抽卡
     * @throws IOException
     */
    @Test
    void createImgAssign() throws IOException {
        RollUnit assignUnit = (RollUnit) YysRollServiceImpl.INSTANCE.getRollUnitMap().get(YysRoll.SSR.getLevel()).values().toArray()[0];
        rollResultData = YysRollServiceImpl.INSTANCE.rollTextForAssignUnit(assignUnit,true);
        RollImgResult rollImgResult = yysImgOutputService.createImgResult(rollResultData, "普通定向-UP", RollModel.assign);
        assertNotNull(rollImgResult);
        assertNotNull(rollImgResult.getBufferedImage());
        writeOnTestFolder(rollImgResult.getBufferedImage(),"normal");
        rollResultData = YysRollServiceImpl.INSTANCE.rollTextForAssignUnit(assignUnit,false);
        rollImgResult = yysImgOutputService.createImgResult(rollResultData, "普通定向-无UP", RollModel.assign);
        assertNotNull(rollImgResult);
        assertNotNull(rollImgResult.getBufferedImage());
        writeOnTestFolder(rollImgResult.getBufferedImage(),"normal");
    }

    /**
     * 写入到测试文件夹中
     */
    private void writeOnTestFolder(BufferedImage bufferedImage,String testName) throws IOException {
        File writeFile = new File(testFile.getAbsolutePath() + File.separator + System.currentTimeMillis() + '-' + testName + ".png");
        ImageIO.write(bufferedImage, "png", writeFile);
        System.out.println("writeFile: "+writeFile.getAbsolutePath());
    }
}