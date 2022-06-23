package com.github.cuteluobo.service.Impl;

import cn.pomit.mybatis.configuration.MybatisConfiguration;
import com.github.cuteluobo.pojo.RollImgResult;
import com.github.cuteluobo.pojo.RollResultData;
import com.github.cuteluobo.pojo.RollResultUnit;
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

    @Test
    void createImgResult() throws IOException {
        rollResultData = YysRollServiceImpl.INSTANCE.rollUp(1000,true,10,null);
        RollImgResult rollImgResult = yysImgOutputService.createImgResult(rollResultData, title);
        BufferedImage bufferedImage = rollImgResult.getBufferedImage();
        File writeFile = new File(testFile.getAbsolutePath() +File.separator + System.currentTimeMillis() + ".png");
        ImageIO.write(bufferedImage, "png", writeFile);
        System.out.println("writeFile: "+writeFile.getAbsolutePath());
    }
}