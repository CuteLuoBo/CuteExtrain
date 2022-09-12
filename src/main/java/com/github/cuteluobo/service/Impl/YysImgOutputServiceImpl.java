package com.github.cuteluobo.service.Impl;

import com.github.cuteluobo.enums.RollGameType;
import com.github.cuteluobo.enums.RollModel;
import com.github.cuteluobo.enums.YysRoll;
import com.github.cuteluobo.pojo.RollImgResult;
import com.github.cuteluobo.pojo.RollResultData;
import com.github.cuteluobo.pojo.RollResultUnit;
import com.github.cuteluobo.pojo.TextDrawData;
import com.github.cuteluobo.repository.ResourceLoader;
import com.github.cuteluobo.service.ImgOutputService;
import com.github.cuteluobo.util.DrawUtils;
import com.github.cuteluobo.util.FastDrawContainerHelper;
import com.github.cuteluobo.util.FastDrawContainerHelperBuilder;
import com.github.cuteluobo.util.YysRollUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

/**
 * 阴阳师图片消息生成实现类
 * @author CuteLuoBo
 * @date 2022/6/20 15:45
 */
public class YysImgOutputServiceImpl implements ImgOutputService {
    Logger logger = LoggerFactory.getLogger(YysImgOutputServiceImpl.class);

    public static final YysImgOutputServiceImpl INSTANCE = new YysImgOutputServiceImpl();



    //TODO 额外增加抽象，对结果集进行解析封装，或使图片输出类，更通用

    /**
     * 默认资源加载文件夹
     */
    private File normalResourceFolder;

    private YysImgOutputServiceImpl() {
        normalResourceFolder = new File(ResourceLoader.INSTANCE.getNormalResourceFolder().getAbsolutePath() + File.separator + RollGameType.yys.getText());
    }


    /**
     * 生成图片输出结果
     *
     * @param rollResultData 用于生成的抽取结果集
     * @param title          图片标题
     * @return 处理的图片数据
     */
    @Override
    public RollImgResult createImgResult(RollResultData rollResultData, String title, RollModel rollModel) throws IOException {
        int imageWidth = 1080;
        int imageHeight = 1700;
        int spaceHeight = imageHeight / 48;
        BufferedImage bufferedImage = new BufferedImage(imageWidth,imageHeight,BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        //抗锯齿
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        //填充背景
        try {
            //从文件中获取背景
            graphics2D.drawImage(
                    DrawUtils.loadImageFile(
                            new File(normalResourceFolder.getAbsolutePath() + File.separator + "background.jpg")
                    ),
                    0, 0, null);
        } catch (IOException ioException) {
            logger.error("读取并绘制图片背景失败，使用默认填充",ioException);
            //填充背景颜色
            graphics2D.setColor(new Color(247,186,129));
            graphics2D.fillRect(0,0, imageWidth,imageHeight);
        }
        //默认字体
        Font normalFont;
        try {
            normalFont = Font.createFont(Font.PLAIN, new File(normalResourceFolder.getAbsolutePath() + File.separator + "font.ttf"));
        } catch (Exception e) {
            logger.error("无法加载字体，可能造成显示效果异常!建议使用字体：田氏颜体大字库",e);
            normalFont = Font.decode(null);
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

        Color mainContainerBackgroundColor = new Color(236, 240, 241, 128);

        String copyrightString = "Created By Cute_LuoBo - YysBot";
        //构建并绘制
        FastDrawContainerHelper mainContainerHelper
                = new FastDrawContainerHelperBuilder(graphics2D)
                .setContainer(mainContainerWidth, mainContainerHeight, mainContainerX, mainContainerY)
                .setContainerBackground(mainContainerBackgroundColor)
                .setContainerRoundRate(mainContainerRoundRate)
                .addContainerTitle(copyrightString, normalFont, 15, Color.BLACK, null, mainContainerHeight - 20)
                .build();
        mainContainerHelper.drawAll();




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
        //背景颜色和字体颜色
        Color mainTitleContainerBackgroundColor = new Color(189, 195, 199, 204);
        Color mainTitleColor = Color.BLACK;
        //字体大小
        int mainTitleFontSize = 50;
        //构建并绘制
        FastDrawContainerHelper mainTitleContainerHelper
                = new FastDrawContainerHelperBuilder(graphics2D)
                .setContainer(mainTitleContainerWidth, mainTitleContainerHeight, mainTitleContainerX, mainTitleContainerY)
                .setContainerBackground(mainTitleContainerBackgroundColor)
                .setContainerRoundRate(mainTitleContainerRoundRate)
                .addContainerTitle(title, normalFont, mainTitleFontSize, mainTitleColor, null, null)
                .build();
        mainTitleContainerHelper.drawAll();

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
        //背景颜色和字体颜色
        Color winShowContainerBackgroundColor = new Color(189, 195, 199, 153);
        Color winShowContainerBorderColor = Color.BLACK;
        Color winShowContainerTitleColor = Color.BLACK;
        //字体大小
        int winTitleFontSize = 30;
        String winTitle = "出货结果";
        //构建并绘制
        FastDrawContainerHelper winShowContainerHelper
                = new FastDrawContainerHelperBuilder(graphics2D)
                .setContainer(winShowContainerWidth, winShowContainerHeight, winShowContainerX, winShowContainerY)
                .setContainerBackground(winShowContainerBackgroundColor)
                .setContainerBorder(winShowContainerBorderColor,1)
                .setContainerRoundRate(winShowContainerRoundRate)
                .addContainerTitle(winTitle, normalFont, winTitleFontSize, winShowContainerTitleColor, null, winShowContainerInsideSpace)
                .build();
        winShowContainerHelper.drawAll();

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
        int unitNormalStartX = winShowContainerX + winShowContainerInsideSpace + winUnitTopSpace;
        int unitStartX = unitNormalStartX;
        int unitStartY = winShowContainerY + winTitleFontSize + winShowContainerInsideSpace  + unitContainTopSpace;
        //循环输出式神结果
        List<RollResultUnit> winResultUnitList = rollResultData.getWinResultUnitList();
        //无事发生
        if (winResultUnitList == null || winResultUnitList.isEmpty()) {
            //新建列表替换
            TextDrawData textDrawData = new TextDrawData("（无事发生）", normalFont, 100, Color.BLACK
                    , null, null);
            List<TextDrawData> textDrawDataList = new ArrayList<>(1);
            textDrawDataList.add(textDrawData);
            winShowContainerHelper.setTextDrawList(textDrawDataList);
            winShowContainerHelper.drawAllText();
        } else {
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
                //样式
                int limitUpFontSize = 30;
                int limitUpTopSize = 10;
                Color limitUpColor = Color.BLACK;
                //修改原容器绘制类，只绘制指定文本
                FastDrawContainerHelper limitUpStringHelper = winShowContainerHelper;
                //关闭边框绘制
                limitUpStringHelper.setContainerBorder(false);
                //新建列表替换
                TextDrawData textDrawData = new TextDrawData(limitUpString, normalFont, limitUpFontSize, limitUpColor
                        , null, unitStartY - winShowContainerY + rollUnitContainHeight + limitUpTopSize);
                List<TextDrawData> textDrawDataList = new ArrayList<>(1);
                textDrawDataList.add(textDrawData);
                limitUpStringHelper.setTextDrawList(textDrawDataList);
                //绘制文本
                limitUpStringHelper.drawAllText();
            }
        }


        //====成就统计/定向抽卡结果====
        //容器
        //圆角弧度
        int achievementContainerRoundRate = 30;
        //横纵间隙
        int achievementContainerLateralOutSideSpace = 30;
        int achievementContainerVerticalOutSideSpace = 40;
        //容器宽高
        int achievementContainerWidth = mainContainerWidth - 2 * achievementContainerLateralOutSideSpace;
        int achievementContainerHeight = 260;
        //容器原点
        int achievementContainerX = mainContainerX + achievementContainerLateralOutSideSpace;
        int achievementContainerY = winShowContainerY + winShowContainerHeight + achievementContainerVerticalOutSideSpace;

        //背景颜色和字体颜色
        Color achievementContainerBackgroundColor = new Color(140, 181, 209, 128);



        //字体与边框距离
        int achievementFontLeftSpace = 30;
        int achievementFontTopSpace = 20;
        //构建并绘制
        FastDrawContainerHelper achievementContainerHelper
                = new FastDrawContainerHelperBuilder(graphics2D)
                .setContainer(achievementContainerWidth, achievementContainerHeight, achievementContainerX, achievementContainerY)
                .setContainerBackground(achievementContainerBackgroundColor)
                .setContainerRoundRate(achievementContainerRoundRate)
                .build();
        achievementContainerHelper.drawAll();

        //成就/追梦右侧式神大图
        RollResultUnit rightShowUnit = null;

        //文字输出
        List<TextDrawData> achievementList = new ArrayList<>(4);

        //标题
        int achievementTitleFontSize = 30;
        TextDrawData achievementTitleDrawData;
        Color achievementContainerTitleColor = Color.BLACK;
        String achievementTitle;

        //其他文字与标题的间隔
        int achievementFontTopTitleSpace = 30;
        //多行文字内部间隙
        int achievementFontInsideTopSpace = 20;
        //定向抽卡模式
        if (rollModel == RollModel.assign || rollModel == RollModel.specify) {
            //标题
            achievementTitle = "定向追梦结果：";
            //字体累加的间隔
            int tempFontSpaceTotal = 0;

            //有出货结果且不为空时
            if (winResultUnitList != null && !winResultUnitList.isEmpty()) {
                //获取到最后一个抽出的式神
                RollResultUnit showUnit = winResultUnitList.get(winResultUnitList.size() - 1);
                //指定右侧显示式神
                rightShowUnit = showUnit ;
                //非标题的文字字体大小
                int achievementOtherFontSize = 20;
                Color achievementOtherColor = Color.BLACK;

                //1.出货次数
                String achievement1 = "出货抽数： 第 " + showUnit.getSequence() + " 抽 " + showUnit.getLevel() + " - " + showUnit.getName();
                tempFontSpaceTotal += achievementFontTopTitleSpace + achievementTitleFontSize;
                //输出
                TextDrawData achievement1DrawData = new TextDrawData(achievement1, normalFont, achievementOtherFontSize, achievementOtherColor
                        , achievementFontLeftSpace, tempFontSpaceTotal);

                achievementList.add(achievement1DrawData);

                //2.当前阶级的抽出概率
                BigDecimal levelRollRate = showUnit.getLevelRollRate();

                String achievement2 = "定向阶级抽出概率： ";
                if (levelRollRate != null) {
                    achievement2 += levelRollRate.movePointRight(2) + " %";
                } else {
                    achievement2 += "? %";
                }
                tempFontSpaceTotal += achievementFontInsideTopSpace + achievementOtherFontSize;
                //输出
                TextDrawData achievement2DrawData = new TextDrawData(achievement2, normalFont, achievementOtherFontSize, achievementOtherColor
                        , achievementFontLeftSpace, tempFontSpaceTotal);
                achievementList.add(achievement2DrawData);


                //3.当前式神同阶级抽出概率
                BigDecimal unitRollRate = showUnit.getUnitRollRate();
                String achievement3 = "式神定向抽出概率： ";
                if (unitRollRate != null) {
                    achievement3 += unitRollRate.movePointRight(2) + " %";
                } else {
                    achievement3 += "无";
                }
                tempFontSpaceTotal += achievementFontInsideTopSpace + achievementOtherFontSize;
                //输出
                TextDrawData achievement3DrawData = new TextDrawData(achievement3, normalFont, achievementOtherFontSize, achievementOtherColor
                        , achievementFontLeftSpace, tempFontSpaceTotal);
                achievementList.add(achievement3DrawData);

            }

        }
        //其他抽卡模式
        else {
            //标题
            achievementTitle = "成就统计：";
            //3.成就数据统计-抽取最多式神
            Map<Integer, RollResultUnit> unitMap = new HashMap<>(16);
            Map<Integer, Integer> totalMap = new HashMap<>(16);
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
                int temp = entry.getValue();
                if (temp > totalNum) {
                    unitId = entry.getKey();
                    totalNum = temp;
                }
            }
            RollResultUnit maxUnit = unitMap.get(unitId);
            //设置为右侧输出式神
            rightShowUnit = maxUnit;

            //生成绘制数据

            if (maxUnit != null) {
                //字体大小
                int achievement1FontSize = 20;
                Color achievement1Color = Color.BLACK;
                String achievement1Title = "抽取最多式神： " + maxUnit.getLevel() + " " + maxUnit.getName() + " x " + totalNum + " 次";
                //构建成就输出
                TextDrawData achievement1 = new TextDrawData(achievement1Title, normalFont, achievement1FontSize, achievement1Color
                        , achievementFontLeftSpace, achievementFontTopTitleSpace + achievementTitleFontSize);

                achievementList.add(achievement1);

            }
            //TODO 增加更多输出
        }
        //标题文字数据
        achievementTitleDrawData = new TextDrawData(achievementTitle, normalFont, achievementTitleFontSize, achievementContainerTitleColor
                , achievementFontLeftSpace, achievementFontTopSpace);
        achievementList.add(achievementTitleDrawData);
        //设置列表并输出
        //替换原容器文本数据并绘制
        achievementContainerHelper.setTextDrawList(achievementList);
        achievementContainerHelper.drawAllText();


        //右侧式神大图
        if (rightShowUnit != null) {
            //TODO 增加右侧式神的阶级和名称标识
            int rightShowUnitStartX = achievementContainerX + 600;
            int rightShowUnitStartY = achievementContainerY - 50;
            //获取文件并绘制
            File rightShowUnitBodyFile = new File(normalResourceFolder.getAbsolutePath() + File.separator + "body" + File.separator + rightShowUnit.getOfficialId() + ".png");
            if (rightShowUnitBodyFile.exists()) {
                BufferedImage rightShowUnitImageBuffer = ImageIO.read(rightShowUnitBodyFile);
                graphics2D.drawImage(rightShowUnitImageBuffer, rightShowUnitStartX, rightShowUnitStartY, 400, 330, null);
            } else {
                logger.error("ID为{}的式神身体大图不存在", rightShowUnit.getOfficialId());
            }
        }



        //TODO 增加定向抽取时的额外界面

        //====欧气鉴定-statistic====
        //圆角弧度
        int statisticContainerRoundRate = 10;
        //横纵间隙
        int statisticContainerLateralOutSideSpace = 30;
        int statisticContainerVerticalOutSideSpace = 40;
        //容器宽高
        int statisticContainerWidth = mainContainerWidth - 2 * statisticContainerLateralOutSideSpace;
        int statisticContainerHeight = 320;
        //容器原点
        int statisticContainerX = mainContainerX + statisticContainerLateralOutSideSpace;
        int statisticContainerY = achievementContainerY + achievementContainerHeight + statisticContainerVerticalOutSideSpace;
        //背景颜色和字体颜色
        Color statisticContainerBackgroundColor = new Color(189, 195, 199, 153);
        Color statisticContainerTitleColor = Color.BLACK;
        //字体大小
        int statisticTitleFontSize = 30;
        int statisticTitleFontTopSpace = 15;
        String statisticTitle = "欧气鉴定";
        //构建并绘制
        FastDrawContainerHelper statisticContainerHelper
                = new FastDrawContainerHelperBuilder(graphics2D)
                .setContainer(statisticContainerWidth, statisticContainerHeight, statisticContainerX, statisticContainerY)
                .setContainerBackground(statisticContainerBackgroundColor)
                .setContainerRoundRate(statisticContainerRoundRate)
                .setContainerBorder(Color.BLACK,1)
                .addContainerTitle(statisticTitle, normalFont, statisticTitleFontSize, statisticContainerTitleColor, null, statisticTitleFontTopSpace)
                .build();
        statisticContainerHelper.drawAll();

        //其他文字输出
        List<TextDrawData> statisticTextDrawDataList = new ArrayList<>(3);

        int statisticNormalFontSize = 25;
        //与容器的左间隔
        int statisticNormalLeftSpace = 35;
        //与标题的间隔
        int statisticFontTopTitleSpace = 30;
        //多行文字内部间隙
        int statisticFontInsideTopSpace = (int) (statisticNormalFontSize * 0.8);
        //间隔缓存用
        int statisticTempSpace = statisticTitleFontSize + statisticTitleFontTopSpace;
        //抽取模式
        String rollModelString = "抽取模式： " + rollModel.getText();
        TextDrawData rollModelDrawData = new TextDrawData(rollModelString, normalFont, statisticNormalFontSize, Color.BLACK, statisticNormalLeftSpace, statisticTempSpace);
        statisticTextDrawDataList.add(rollModelDrawData);
        //累加文字间隔
        statisticTempSpace += statisticNormalFontSize + statisticFontInsideTopSpace;
        //抽卡次数
        String rollNumString = "抽取次数： " + rollResultData.getRollNum();
        TextDrawData rollNumDrawData = new TextDrawData(rollNumString, normalFont, statisticNormalFontSize, Color.BLACK, statisticNormalLeftSpace, statisticTempSpace);
        statisticTextDrawDataList.add(rollNumDrawData);
        //累加文字间隔
        statisticTempSpace += statisticNormalFontSize + statisticFontInsideTopSpace;
        Map<YysRoll, BigDecimal> yysRollBigDecimalMap = YysRollUtils.checkLevelRate(rollResultData.getRollResultUnitList());
        //抽卡概率
        StringBuilder rollRateStringBuilder = new StringBuilder();
        rollRateStringBuilder.append("出货次数： ").append(rollResultData.getWinResultUnitList()==null?0:rollResultData.getWinResultUnitList().size());
        if (!rollResultData.getWinResultUnitList().isEmpty()) {
            rollRateStringBuilder.append("， 其中 ");
            long ssrNum = rollResultData.getWinResultUnitList()
                    .stream()
                    .filter(unit -> YysRoll.SSR.getLevel().equals(unit.getLevel()))
                    .count();
            long spNum = rollResultData.getWinResultUnitList()
                    .stream()
                    .filter(unit -> YysRoll.SP.getLevel().equals(unit.getLevel()))
                    .count();
            if (ssrNum > 0) {
                rollRateStringBuilder.append("SSR阶 x ").append(ssrNum).append(" ");
            }
            if (spNum > 0) {
                rollRateStringBuilder.append("Sp阶 x ").append(spNum).append(" ");
            }
        }
        //出货概率
        BigDecimal winRate = yysRollBigDecimalMap.getOrDefault(YysRoll.SP, BigDecimal.ZERO).add(yysRollBigDecimalMap.getOrDefault(YysRoll.SSR, BigDecimal.ZERO));
        rollRateStringBuilder.append(" ，综合出货概率为： ").append(winRate.movePointRight(2)).append(" %");
        TextDrawData rollRateDrawData = new TextDrawData(rollRateStringBuilder.toString(), normalFont, statisticNormalFontSize, Color.BLACK, statisticNormalLeftSpace, statisticTempSpace);
        statisticTextDrawDataList.add(rollRateDrawData);


        //评分
        //累加文字间隔
        statisticTempSpace += statisticNormalFontSize + statisticFontInsideTopSpace;
        //字体大小
        int statisticCommentFontSize = 80;
        int statisticCommentFontTopSpace = 15;
        Color statisticCommentColor = Color.BLACK;
        StringBuilder statisticCommentStringBuilder = new StringBuilder();
        statisticCommentStringBuilder.append("鉴定为： ");
        //默认概率
        float normalWinRate = YysRoll.SP.getRollProb() + YysRoll.SSR.getRollProb();
        float nowWinRate = winRate.floatValue();
        //
        if (nowWinRate >= normalWinRate * 1.5) {
            statisticCommentStringBuilder.append("欧");
            statisticCommentColor = Color.RED;
        } else if (nowWinRate <= normalWinRate * 0.8) {
            statisticCommentStringBuilder.append("非");
            statisticCommentColor = new Color(8, 14, 44);
        } else {
            statisticCommentStringBuilder.append("平");
            statisticCommentColor = new Color(231, 109, 137);
        }
        TextDrawData statisticCommentDrawData = new TextDrawData(statisticCommentStringBuilder.toString(), normalFont, statisticCommentFontSize,statisticCommentColor,null, statisticTempSpace + statisticCommentFontTopSpace);
        statisticTextDrawDataList.add(statisticCommentDrawData);

        statisticContainerHelper.setTextDrawList(statisticTextDrawDataList);
        statisticContainerHelper.drawAllText();
        //结束写入
        graphics2D.dispose();
        //转换结果对象
        RollImgResult rollImgResult = new RollImgResult();
        rollImgResult.setRollNum(rollResultData.getRollNum());
        rollImgResult.setRollResultUnitList(rollResultData.getRollResultUnitList());
        rollImgResult.setWinResultUnitList(rollResultData.getWinResultUnitList());
        rollImgResult.setTipMap(rollResultData.getTipMap());
        rollImgResult.setBufferedImage(bufferedImage);
        return rollImgResult;
    }

    private void drawRollUnitContainerAndData(Graphics2D graphics2D, int startX, int startY, int width, int height, Font font, RollResultUnit rollResultUnit) throws IOException {
        //1.绘制容器
        //背景颜色和字体颜色
        Color backgroundColor = new Color(236, 240, 241, 150);
        int roundRate = 20;
        //构建并绘制
        FastDrawContainerHelper drawHelper
                = new FastDrawContainerHelperBuilder(graphics2D)
                .setContainer(width, height, startX, startY)
                .setContainerBackground(backgroundColor)
                .setContainerRoundRate(roundRate)
                .build();
        drawHelper.drawAll();
        //2.绘制阶级图标
        String level = rollResultUnit.getLevel();
        File levelImageFile = new File(normalResourceFolder.getAbsolutePath() + File.separator + level.toLowerCase() + ".png");
        try {
            BufferedImage levelImage = ImageIO.read(levelImageFile);
            int levelImageSpace = 2;
            graphics2D.drawImage(levelImage, startX + levelImageSpace, startY + levelImageSpace, 45, 30, null);
        } catch (IOException ioException) {
            logger.error("无法加载阶级图片,路径：{}",levelImageFile.getAbsolutePath(),ioException);
        }


        //3.绘制式神图标
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
        File unitImageFile = new File(normalResourceFolder.getAbsolutePath() + File.separator + "head" + File.separator + yysUnitId + ".png");
        if (unitImageFile.exists()) {
            BufferedImage unitImage = ImageIO.read(unitImageFile);
            graphics2D.drawImage(unitImage, unitStartX, unitStartY, unitImageWidth, unitImagerHeight, null);
        }else {
            logger.error("无法找到ID为{}的头像",yysUnitId);
        }

        //4.描边
        graphics2D.setColor(Color.GRAY);
        graphics2D.draw(new Ellipse2D.Float(unitStartX, unitStartY, unitImageWidth, unitImagerHeight));

        //5.绘制文本列表
        ArrayList<TextDrawData> textDrawDataList = new ArrayList<>(2);
        //抽卡次数
        String rollNumString = "第 " + rollResultUnit.getSequence() + " 抽 " + (rollResultUnit.getUp() ? "(UP)" : "");
        //文字参数
        int rollNumFontSize = height / 10;
        TextDrawData rollNum = new TextDrawData(rollNumString, font, rollNumFontSize, Color.BLACK
                , null, unitImagerHeight + unitImagerLateralSpace * 2);
        textDrawDataList.add(rollNum);
        //式神名称
        String unitNameString = rollResultUnit.getName();
        int unitNameFontSize = height / 8;
        TextDrawData unitName = new TextDrawData(unitNameString, font, unitNameFontSize, Color.BLACK
                , null, unitImagerHeight + unitImagerLateralSpace * 2 + rollNumFontSize + 10);
        textDrawDataList.add(unitName);
        drawHelper.setTextDrawList(textDrawDataList);
        //绘制
        drawHelper.drawAllText();
    }
}
