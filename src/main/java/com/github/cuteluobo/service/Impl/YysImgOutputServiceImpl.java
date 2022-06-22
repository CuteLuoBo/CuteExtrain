package com.github.cuteluobo.service.Impl;

import com.github.cuteluobo.enums.YysRoll;
import com.github.cuteluobo.pojo.RollImgResult;
import com.github.cuteluobo.pojo.RollResultData;
import com.github.cuteluobo.pojo.RollResultUnit;
import com.github.cuteluobo.service.ImgOutputService;
import com.github.cuteluobo.util.DrawUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 阴阳师图片消息生成实现类
 * @author CuteLuoBo
 * @date 2022/6/20 15:45
 */
public class YysImgOutputServiceImpl implements ImgOutputService {
    Logger logger = LoggerFactory.getLogger(YysImgOutputServiceImpl.class);

    //TODO 输出路径交给具体的输出类进行处理
    /**
     * ，
     * 储存路径名称
     */
//    public static final String NORMAL_SAVE_FOLDER_NAME = "imgOutPut"+File.separator+"yys";

    /**
     * 完整储存路径
     */
//    public static final File NORMAL_SAVE_FOLDER = new File(CuteExtra.INSTANCE.getDataFolderPath().getFileName() + File.separator + NORMAL_SAVE_FOLDER_NAME);

    public static final YysImgOutputServiceImpl INSTANCE = new YysImgOutputServiceImpl();

    private YysImgOutputServiceImpl() {
//        if (!NORMAL_SAVE_FOLDER.exists()) {
//            try {
//                NORMAL_SAVE_FOLDER.mkdirs();
//            } catch (Exception e) {
//                logger.error("图片输出文件夹初始化失败",e);
//                throw e;
//            }
//        }
    }


    /**
     * 生成图片输出结果
     *
     * @param rollResultData 用于生成的抽取结果集
     * @param title          图片标题
     * @return 处理的图片数据
     */
    @Override
    public RollImgResult createImgResult(RollResultData rollResultData, String title) throws IOException {
        int imageWidth = 1080;
        int imageHeight = 1700;
        int spaceHeight = imageHeight / 48;
        BufferedImage bufferedImage = new BufferedImage(imageWidth,imageHeight,BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        //抗锯齿
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        //填充背景
        try {
            //从文件中获取
            //TODO 后续转为统一引入
            graphics2D.drawImage(DrawUtils.loadImageFile(new File("J:\\image\\背景-1080x1700.jpg")),0,0,null);
        } catch (IOException ioException) {
            logger.error("读取并绘制图片背景失败，使用默认填充",ioException);
            //填充背景颜色
            graphics2D.setColor(new Color(247,186,129));
            graphics2D.fillRect(0,0, imageWidth,imageHeight);
        }

        //====设置中间的半透明容器遮罩====
        //圆角弧度
        int mainContainerRoundRate = 10;
        //横纵间隙
        int mainContainerLateralOutSideSpace = 20;
        int mainContainerVerticalOutSideSpace = 30;
        //容器宽高
        int mainContainerWidth = imageWidth - 2 * mainContainerLateralOutSideSpace;
        int mainContainerHeight = imageHeight - 2 * mainContainerVerticalOutSideSpace;
        //容器原点
        int mainContainerX = mainContainerLateralOutSideSpace;
        int mainContainerY = mainContainerVerticalOutSideSpace;
        //设置颜色并绘制
        graphics2D.setColor(new Color(236,240,241, 128));
        graphics2D.fillRoundRect(mainContainerX, mainContainerY
                , mainContainerWidth , mainContainerHeight
                , mainContainerRoundRate, mainContainerRoundRate);

        int leftSideX = imageWidth / 6;

        //默认字体
        Font normalFont = new Font("悟空大字库",Font.BOLD,12);
        try {
            //TODO 转为统一引入并设置缺少字体的运行情况
            normalFont = Font.createFont(Font.PLAIN, new File("J:\\image\\悟空大字库.ttf"));
        } catch (FontFormatException | IOException ioException) {

        }


        //====主标题====

        //1.绘制主标题容器遮罩
        //圆角弧度
        int mainTitleContainerRoundRate = 10;
        //横纵间隙
        int mainTitleContainerLateralOutSideSpace = 20;
        int mainTitleContainerVerticalOutSideSpace = 30;
        //主标题容器宽高
        int mainTitleContainerWidth = mainContainerWidth - 2 * mainTitleContainerLateralOutSideSpace;
        int mainTitleContainerHeight = 140;
        //容器原点
        int mainTitleContainerX = mainContainerX + mainTitleContainerLateralOutSideSpace;
        int mainTitleContainerY = mainContainerY + mainTitleContainerVerticalOutSideSpace;
        //绘制容器
        drawRoundRectContainer(graphics2D
                , new Color(189, 195, 199, 204), null
                , mainTitleContainerX, mainTitleContainerY
                , mainTitleContainerWidth, mainTitleContainerHeight
                , mainTitleContainerRoundRate);

        //2.绘制标题
        int mainTitleFontSize = 50;
        graphics2D.setFont(normalFont.deriveFont((float) mainTitleFontSize));
        graphics2D.setColor(Color.BLACK);

        //计算起始坐标
        int mainTitleStartX = mainTitleContainerX + DrawUtils.getCenterStringStartPoint(
                graphics2D.getFontMetrics(),
                title,
                mainTitleContainerWidth);

        //起始Y点= 容器起始Y + 字体居中间隔 + 字体像素大小
        int mainTitleStartY = mainTitleContainerY + (mainTitleContainerHeight - mainTitleFontSize) /2 + mainTitleFontSize;
        //绘制标题
        graphics2D.drawString(title,  mainTitleStartX, mainTitleStartY);
        float mainTitleEndY = mainTitleStartY + mainTitleFontSize;

        //====出货结果区====
        //1.绘制容器遮罩
        //圆角弧度
        int winShowContainerRoundRate = 10;
        //横纵间隙
        int winShowContainerLateralOutSideSpace = 20;
        int winShowContainerVerticalOutSideSpace = 30;
        //容器宽高
        int winShowContainerWidth = mainContainerWidth - 2 * winShowContainerLateralOutSideSpace;
        int winShowContainerHeight = 750;
        //容器原点
        int winShowContainerX = mainContainerX + winShowContainerLateralOutSideSpace;
        int winShowContainerY = mainTitleContainerY + mainTitleContainerHeight + winShowContainerVerticalOutSideSpace;
        //容器内空
        int winShowContainerInsideSpace = 20;
        //绘制容器
        drawRoundRectContainer(graphics2D
                , new Color(189,195,199, 153), Color.BLACK
                , winShowContainerX, winShowContainerY
                , winShowContainerWidth, winShowContainerHeight
                , winShowContainerRoundRate);
        //2.标题
        float winTitleFontSize = 30f;
        graphics2D.setFont(normalFont.deriveFont(winTitleFontSize));
        graphics2D.setColor(Color.BLACK);
        String winTitle = "出货结果";
        float winTitleStartX = winShowContainerX + DrawUtils.getCenterStringStartPoint(
                graphics2D.getFontMetrics(),
                winTitle,
                winShowContainerWidth);
        float winTitleStartY = winShowContainerY + winShowContainerInsideSpace + winTitleFontSize;
        //绘制
        graphics2D.drawString(winTitle, winTitleStartX, winTitleStartY);

        //3.详细式神结果输出

        //距离标题的间隔
        int winUnitTopSpace = 15;

        //结果单体的宽高和间隙
        int rollUnitContainWidth = 150;
        int rollUnitContainHeight = 200;
        //单行显示的数量
        int lineShowUnitNum = 5;
        //最大可现实的行数
        int maxLine = 3;
        int maxShowNum = lineShowUnitNum * maxLine;
        //单体容器的间隔
        int unitContainSpace = (winShowContainerWidth - winShowContainerInsideSpace * 2) / lineShowUnitNum  - rollUnitContainWidth;
        //顶部间隔
        int unitContainTopSpace = 15;
        //内部Y间隔
        int unitContainInsideTopSpace = rollUnitContainHeight / 10;
        int unitNormalStartX = winShowContainerX + winShowContainerInsideSpace + 15;
        int unitStartX = unitNormalStartX;
        int unitStartY = (int) (winTitleStartY + unitContainTopSpace);
        //循环输出式神结果
        List<RollResultUnit> winResultUnitList = rollResultData.getWinResultUnitList();
        for (int i = 0; i < Math.min(maxShowNum, winResultUnitList.size()); i++) {
            //每行的开头重置X和Y
            if (i != 0 && i % lineShowUnitNum == 0) {
                unitStartX = unitNormalStartX;
                unitStartY += rollUnitContainHeight + unitContainInsideTopSpace;
            }
            //绘制主体
            drawRollUnitContainerAndData(graphics2D
                    , unitStartX, unitStartY
                    , rollUnitContainWidth, rollUnitContainHeight
                    , normalFont
                    , winResultUnitList.get(i));
            //X偏移容器宽度+间隔
            unitStartX += rollUnitContainWidth + unitContainSpace;
        }
        //超限提示语
        if (winResultUnitList.size() > maxShowNum) {
            //累加结果
            int ssrNum = 0;
            int spNum = 0;
            for (int i = maxShowNum; i < winResultUnitList.size(); i++) {
                RollResultUnit rollResultUnit = winResultUnitList.get(i);
                if (YysRoll.SSR.getLevel().equals(rollResultUnit.getLevel())) {
                    ssrNum++;
                } else if (YysRoll.SP.getLevel().equals(rollResultUnit.getLevel())) {
                    spNum++;
                }
            }
            //构建字符串
            StringBuilder limitUpStringBuilder = new StringBuilder();
            limitUpStringBuilder.append("剩余 ");
            if (ssrNum > 0) {
                limitUpStringBuilder.append(ssrNum).append(" 个SSR ");
            }
            if (spNum > 0) {
                limitUpStringBuilder.append(spNum).append(" 个SP ");
            }
            String limitUpString = limitUpStringBuilder.toString();
            //样式和绘制
            int limitUpFontSize = 30;
            int limitUpTopSize = 10;
            graphics2D.setFont(normalFont.deriveFont((float) limitUpFontSize));
            graphics2D.setColor(Color.BLACK);
            //计算起始坐标
            int limitUpStartX = winShowContainerX + DrawUtils.getCenterStringStartPoint(
                    graphics2D.getFontMetrics(),
                    limitUpString,
                    winShowContainerWidth);

            //起始Y点= 容器起始Y + 字体居中间隔 + 字体像素大小
            int limitUpStartY = unitStartY + rollUnitContainHeight + limitUpTopSize + limitUpFontSize;
            //绘制
            graphics2D.drawString(limitUpString,  limitUpStartX, limitUpStartY);
        }

        //====成就统计====
        //1.绘制主标题容器遮罩
        //圆角弧度
        int achievementContainerRoundRate = 10;
        //横纵间隙
        int achievementContainerLateralOutSideSpace = 30;
        int achievementContainerVerticalOutSideSpace = 40;
        //容器宽高
        int achievementContainerWidth = mainContainerWidth - 2 * achievementContainerLateralOutSideSpace;
        int achievementContainerHeight = 260;
        //容器原点
        int achievementContainerX = mainContainerX + achievementContainerLateralOutSideSpace;
        int achievementContainerY = winShowContainerY + winShowContainerHeight + achievementContainerVerticalOutSideSpace;
        //绘制容器
        drawRoundRectContainer(graphics2D
                , new Color(189, 195, 199, 204), null
                , achievementContainerX, achievementContainerY
                , achievementContainerWidth, achievementContainerHeight
                , achievementContainerRoundRate);

        //2.绘制标题
        //字体靠左间隔
        int achievementFontLeftSpace = 30;
        int achievementFontTopSpace = 20;

        //字体大小
        int achievementTitleFontSize = 30;
        String achievementTitle = "成就统计：";
        graphics2D.setFont(normalFont.deriveFont((float) achievementTitleFontSize));
        graphics2D.setColor(Color.BLACK);
        //容器原点
        int achievementTitleX = achievementContainerX + achievementFontLeftSpace;
        int achievementTitleY = achievementContainerY + achievementFontTopSpace + achievementTitleFontSize;
        graphics2D.drawString(achievementTitle, achievementTitleX, achievementTitleY);

        //3.成就数据统计-抽取最多式神
        Map<Integer, RollResultUnit> unitMap = new HashMap<>();
        Map<Integer, Integer> totalMap = new HashMap<>();
        //数据累加
        rollResultData.getRollResultUnitList().forEach( rollResultUnit -> {
            unitMap.put(rollResultUnit.getId(), rollResultUnit);
            totalMap.put(rollResultUnit.getId(), totalMap.getOrDefault(rollResultUnit.getId(), 0)+1);
        });
        int unitId = 0;
        int totalNum = 0;
        //找出最大值
        for (Map.Entry<Integer, Integer> entry : totalMap.entrySet()
        ) {
            unitId = entry.getKey();
            totalNum = entry.getValue();
        }

        RollResultUnit maxUnit = unitMap.get(unitId);

        //统计输出
        //与标题的间隔
        int achievementFontTopTitleSpace = 30;
        //内部间隙
        int achievementFontInsideTopSpace = 20;

        //字体大小
        int achievement1FontSize = 20;
        String achievement1Title = "抽取最多式神： " + maxUnit.getLevel() + " " + maxUnit.getName() + " x " + totalNum + " 次";
        graphics2D.setFont(normalFont.deriveFont((float) achievement1FontSize));
        graphics2D.setColor(Color.BLACK);
        //输出原点
        int achievement1X = achievementTitleX + achievementFontLeftSpace;
        int achievement1Y = achievementTitleY + achievementFontTopTitleSpace + achievement1FontSize;
        graphics2D.drawString(achievement1Title, achievement1X, achievement1Y);
        //TODO 增加更多成就统计

        //成就/追梦右侧式神大图
        RollResultUnit rightShowUnit = maxUnit;
        int rightShowUnitStartX = achievementContainerX + 600;
        int rightShowUnitStartY = achievementContainerY - 50;
        //获取文件并绘制
        File rightShowUnitBodyFile = new File("J:\\image\\body" + File.separator + rightShowUnit.getOfficialId() + ".png");
        if (rightShowUnitBodyFile.exists()) {
            BufferedImage rightShowUnitImageBuffer = ImageIO.read(rightShowUnitBodyFile);
            graphics2D.drawImage(rightShowUnitImageBuffer, rightShowUnitStartX, rightShowUnitStartY, 400, 330, null);
        } else {
            logger.error("ID为{}的式神身体大图不存在", rightShowUnit.getOfficialId());
        }
        //TODO 增加右侧式神的阶级和名称标识

        //TODO 增加欧气鉴定模块

        //结束写入
        graphics2D.dispose();
        //转换结果对象
        RollImgResult rollImgResult = new RollImgResult();
        rollImgResult.setRollNum(rollResultData.getRollNum());
        rollImgResult.setRollResultUnitList(rollResultData.getRollResultUnitList());
        rollImgResult.setWinResultUnitList(rollResultData.getWinResultUnitList());
        rollImgResult.setTipMap(rollResultData.getTipMap());
        rollImgResult.setBufferedImage(bufferedImage);

//        graphics2D.setColor(new Color(172,133,117));
        return rollImgResult;
    }

    private void drawRollUnitContainerAndData(Graphics2D graphics2D, int startX, int startY, int width, int height, Font font, RollResultUnit rollResultUnit) throws IOException {
        //1.绘制容器
        drawRoundRectContainer(graphics2D, new Color(236, 240, 241, 150), null, startX, startY, width, height, 20);
        //绘制阶级图标
        String level = rollResultUnit.getLevel();
        //TODO 后续转为统一获取文件
        BufferedImage levelImage = ImageIO.read(new File("J:\\image"+File.separator + level.toLowerCase() + ".png"));
        int levelImageSpace = 2;
        graphics2D.drawImage(levelImage, startX + levelImageSpace, startY + levelImageSpace, 45, 30, null);

        //2.绘制式神图标
        long yysUnitId = rollResultUnit.getOfficialId();



        //头像上下间隔
        int unitImagerLateralSpace = 10;
        //头像左右间隔
        int unitImagerVerticalSpace = 15;
        //头像宽高
        int unitImageWidth = width - unitImagerVerticalSpace * 2;
        int unitImagerHeight = unitImageWidth;
        //头像起始点X
        int unitStartX = startX + unitImagerVerticalSpace;
        int unitStartY = startY + unitImagerLateralSpace;
        //TODO 后续转为统一获取文件
        File unitImageFile = new File("J:\\image\\head" + File.separator + yysUnitId + ".png");
        if (unitImageFile.exists()) {
            BufferedImage unitImage = ImageIO.read(new File("J:\\image\\head"+File.separator + yysUnitId + ".png"));
            graphics2D.drawImage(unitImage, unitStartX, unitStartY, unitImageWidth, unitImagerHeight, null);
        }else {
            logger.error("无法找到ID为{}头像",yysUnitId);
        }

        //描边
        graphics2D.setColor(Color.GRAY);
        graphics2D.draw(new Ellipse2D.Float(unitStartX,unitStartY,unitImageWidth,unitImagerHeight));

        //绘制抽卡次数
        //文字
        String rollNumString  = "第 " + rollResultUnit.getSequence() + " 抽";
        //文字参数
        int rollNumFontSize = height / 10;
        graphics2D.setFont(font.deriveFont((float) rollNumFontSize));
        graphics2D.setColor(Color.BLACK);
        //绘制起始点
        int rollNumStartX = startX + DrawUtils.getCenterStringStartPoint(graphics2D.getFontMetrics(), rollNumString, width);
        int rollNumStartY = unitStartY + unitImagerHeight + unitImagerLateralSpace + rollNumFontSize;
        graphics2D.drawString(rollNumString,rollNumStartX,rollNumStartY);

        //绘制式神名称
        //文字
        String unitNameString = rollResultUnit.getName();
        //文字参数
        int unitNameFontSize = height / 8;
        graphics2D.setFont(font.deriveFont((float) unitNameFontSize));
        graphics2D.setColor(Color.BLACK);
        //绘制起始点
        int unitNameStartX = startX + DrawUtils.getCenterStringStartPoint(graphics2D.getFontMetrics(), unitNameString, width);
        int unitNameStartY = rollNumStartY + unitNameFontSize  + 10;
        graphics2D.drawString(unitNameString,unitNameStartX,unitNameStartY);
    }

    /**
     * 绘制圆角矩形容器
     *
     * @param graphics2D               绘制对象
     * @param containerBackgroundColor 容器的背景颜色，为null时即不填充背景颜色
     * @param borderColor              边框的颜色，为null时即不绘制边框
     * @param startX                   起始X
     * @param startY                   起始Y
     * @param width                    容器宽度
     * @param height                   容器高度
     * @param roundRate                圆角弧度
     */
    private void drawRoundRectContainer(Graphics2D graphics2D, Color containerBackgroundColor, Color borderColor, int startX, int startY, int width, int height, int roundRate) {
        //容器
        graphics2D.setColor(containerBackgroundColor);
        graphics2D.fillRoundRect(startX, startY
                , width, height
                , roundRate, roundRate);
        //绘制容器外框
        graphics2D.setColor(borderColor);
        graphics2D.draw(new RoundRectangle2D.Float(startX, startY
                , width, height
                , roundRate, roundRate));
    }

}
