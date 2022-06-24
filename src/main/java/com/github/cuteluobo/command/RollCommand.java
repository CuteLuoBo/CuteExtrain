package com.github.cuteluobo.command;

import cn.pomit.mybatis.ProxyHandlerFactory;
import com.github.cuteluobo.CuteExtra;
import com.github.cuteluobo.enums.RollModel;
import com.github.cuteluobo.mapper.YysUnitMapper;
import com.github.cuteluobo.model.YysUnit;
import com.github.cuteluobo.pojo.RollImgResult;
import com.github.cuteluobo.pojo.RollResultData;
import com.github.cuteluobo.pojo.RollUnit;
import com.github.cuteluobo.service.Impl.YysImgOutputServiceImpl;
import com.github.cuteluobo.service.Impl.YysRollServiceImpl;
import com.github.cuteluobo.util.FileIoUtils;
import kotlin.reflect.KClass;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.console.command.*;
import net.mamoe.mirai.console.command.descriptor.CommandArgumentContext;
import net.mamoe.mirai.console.command.descriptor.CommandValueArgumentParser;
import net.mamoe.mirai.console.command.descriptor.SimpleCommandArgumentContext;
import net.mamoe.mirai.console.permission.Permission;
import net.mamoe.mirai.console.permission.PermissionId;
import net.mamoe.mirai.console.permission.PermissionRegistryConflictException;
import net.mamoe.mirai.console.permission.PermissionService;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.MessageReceipt;
import net.mamoe.mirai.message.data.*;
import net.mamoe.mirai.utils.ExternalResource;
import net.mamoe.mirai.utils.LoggerAdapters;
import net.mamoe.mirai.utils.MiraiLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Objects;

import static com.github.cuteluobo.CuteExtra.basePermission;

/**
 * https://github.com/mamoe/mirai-console/blob/master/docs/Commands.md
 * @author CuteLuoBo
 * @date 2021-04-07
 */
public class RollCommand extends CompositeCommand {
    private static String[] secondCommand = {"阴阳师抽卡","yr"};
    private static final String PRIMARY = "yysRoll";
    Logger logger = LoggerFactory.getLogger(RollCommand.class);

    /**指令初始化*/
    public RollCommand() throws PermissionRegistryConflictException {
        super(CuteExtra.INSTANCE, PRIMARY, secondCommand
                ,"/yysRoll n 10"
                , PermissionService.getInstance().register(new PermissionId(CuteExtra.PLUGIN_ID,PRIMARY)
                ,"阴阳师抽卡权限"
                , basePermission)
                , SimpleCommandArgumentContext.EMPTY);
    }

    /**
     * 常规抽卡
     * @param sender 消息发送者
     * @param rollNum 抽卡次数
     * @param message UP/no UP
     */
    @SubCommand({"normal","n","普通"})
    public Boolean normal(@NotNull CommandSender sender,Integer rollNum, String message) throws IOException {
        boolean up = message != null && message.toLowerCase().contains("up");
        //设置最大单次500抽
        rollNum = Math.min(rollNum, 500);
        MessageChainBuilder chainBuilder = new MessageChainBuilder();
        User user = sender.getUser();
        long userId = 0;
        if (user != null) {
            userId = user.getId();
            chainBuilder.append(new At(user.getId()));
            chainBuilder.append("\n");
        }
        //TODO 增加时间统计
        String title = rollNum + "抽" + (up ? "UP" : "") + "模拟抽卡结果：\n";
        RollResultData rollResultData = YysRollServiceImpl.INSTANCE.rollUp(rollNum, up, null, null);
        chainBuilder = imageMessageNormalPut(chainBuilder, sender, rollResultData, RollModel.normal, title, String.valueOf(userId));
        sender.sendMessage(chainBuilder.build());
        return true;
    }

    /**
     * 常规抽卡
     * @param sender 消息发送者
     * @param rollNum 抽卡次数
     */
    @SubCommand({"normal","n","普通"})
    public Boolean normal(@NotNull CommandSender sender,Integer rollNum) throws IOException {
        return normal(sender, rollNum, null);
    }

    /**
     * 活动定向抽卡
     * @param sender 消息发送者
     * @param unitName   式神名称
     * @param isAll  是否全图
     * @return
     */
    @SubCommand({"specify","s","活动定向"})
    public Boolean specify(@NotNull  CommandSender sender, String unitName, String isAll){
        YysUnitMapper yysUnitMapper = ProxyHandlerFactory.getMapper(YysUnitMapper.class);
        if (unitName == null) {
            sender.sendMessage(buildNormalMessage(sender, "你没有输入指定UP的式神名称"));
            return false;
        }
        try {
            //获取指定式神
            YysUnit yysUnit = yysUnitMapper.selectOneByName(unitName, true);
            if (yysUnit == null) {
                sender.sendMessage(buildNormalMessage(sender,"没有找到对应式神"));
                return false;
            }
            //全图鉴BUFF
            boolean allBuff = isAll != null && isAll.contains("全图");
            //发送信息


            MessageChainBuilder chainBuilder = new MessageChainBuilder();
            User user = sender.getUser();
            if (user != null) {
//                chainBuilder.append(MessageSource.quote(messageEvent.getMessage()));
                chainBuilder.append(new At(user.getId()));
                chainBuilder.append("\n");
            }
//            chainBuilder.append("定向概率UP："+yysUnit.getName()+"("+(allBuff?"全图鉴":"非全图")+") 模拟抽卡结果：\n");
            String title = yysUnit.getLevel()+" "+yysUnit.getName()+" ("+(allBuff?"全图鉴":"非全图")+") 追梦结果：\n";
            RollResultData rollResultData = YysRollServiceImpl.INSTANCE.rollTextForSpecifyUnit(new RollUnit(yysUnit), allBuff);
            chainBuilder = imageMessageNormalPut(chainBuilder, sender, rollResultData, RollModel.specify, title, "x");
            sender.sendMessage(chainBuilder.build());
            return true;
        } catch (Exception e) {
            sender.sendMessage(buildNormalMessage(sender,"查询式神错误"));
            throw e;
        }
    }

    /**
     * 活动定向抽卡
     * @param sender 消息发送者
     * @param arg1   式神名称
     * @return
     */
    @SubCommand({"specify","s","活动定向"})
    public Boolean specify(@NotNull  CommandSender sender,String arg1){
        return specify(sender, arg1, null);
    }


    /**
     * 指定抽卡
     * @param sender 消息发送者
     * @param unitName   式神名称
     * @param isUp  是否启用UP加成
     * @return
     */
    @SubCommand({"assign","a","指定"})
    public Boolean assign(@NotNull  CommandSender sender, String unitName, String isUp){
        YysUnitMapper yysUnitMapper = ProxyHandlerFactory.getMapper(YysUnitMapper.class);
        if (unitName == null) {
            sender.sendMessage(buildNormalMessage(sender, "你没有输入指定UP的式神名称"));
            return false;
        }
        try {
            //获取指定式神
            YysUnit yysUnit = yysUnitMapper.selectOneByName(unitName, true);
            if (yysUnit == null) {
                sender.sendMessage(buildNormalMessage(sender,"没有找到对应式神"));
                return false;
            }
            //概率upBUFF
            boolean upBuff = isUp != null && isUp.toLowerCase().contains("up");
            //发送信息
            MessageChainBuilder chainBuilder = new MessageChainBuilder();
            User user = sender.getUser();
            if (user != null) {
                chainBuilder.append(new At(user.getId()));
                chainBuilder.append("\n");
            }
            String title = yysUnit.getLevel()+" "+yysUnit.getName()+" ("+(upBuff?"UP":"无UP")+") 追梦结果：\n";
            RollResultData rollResultData = YysRollServiceImpl.INSTANCE.rollTextForAssignUnit(new RollUnit(yysUnit), upBuff);
            chainBuilder = imageMessageNormalPut(chainBuilder, sender, rollResultData, RollModel.assign, title, "x");
            sender.sendMessage(chainBuilder.build());
            return true;
        } catch (Exception e) {
            sender.sendMessage(buildNormalMessage(sender,"查询式神错误"));
            throw e;
        }
    }

    /**
     * 指定抽卡
     * @param sender 消息发送者
     * @param unitName   式神名称
     * @return
     */
    @SubCommand({"assign","a","指定"})
    public Boolean assign(@NotNull  CommandSender sender, String unitName){
        return assign(sender, unitName, "");
    }

    private MessageChain buildNormalMessage(CommandSender sender,String message) {
        MessageChainBuilder chainBuilder = new MessageChainBuilder();
        User user = sender.getUser();
        if (user != null) {
            chainBuilder.append(new At(user.getId()));
            chainBuilder.append("\n");
        }
        chainBuilder.append(message);
        return chainBuilder.build();
    }

    /**
     * 默认的信息链添加
     * @param chainBuilder
     * @param sender
     * @param rollResultData
     * @param rollModel
     * @param title
     * @param fileRollName
     * @return
     */
    private MessageChainBuilder imageMessageNormalPut(MessageChainBuilder chainBuilder,CommandSender sender,RollResultData rollResultData,RollModel rollModel,String title,String fileRollName) {
        try {
            Image image = conventToRollImage(rollResultData, rollModel, title, fileRollName, sender.getSubject());
            chainBuilder.append(image);
        } catch (Exception exception) {
            logger.error("图片生成/上传错误", exception);
            chainBuilder.append("图片生成/上传错误，请联系管理员，错误信息："+exception.getMessage()+"\n");
            chainBuilder.append(title);
            chainBuilder.append(rollResultData.printResultText());
        }
        return chainBuilder;
    }

    /**
     * 将抽卡信息传为图片
     * @param rollResultData 抽卡信息
     * @param rollModel      抽卡模式
     * @param title 标题
     * @param fileRollName   文件随机名
     * @param contact      聊天主体（上传用）
     * @return 上传后的Image对象
     * @throws IOException 图片生成或写入/读取错误
     */
    private Image conventToRollImage(RollResultData rollResultData,RollModel rollModel,String title,String fileRollName, Contact contact) throws IOException {
        RollImgResult rollImgResult = YysImgOutputServiceImpl.INSTANCE.createImgResult(rollResultData,title, rollModel);
        BufferedImage bufferedImage = rollImgResult.getBufferedImage();
        File imageFile = FileIoUtils.createFileTemp("yysRoll", fileRollName + "-" + rollResultData.getRollNum() + ".jpg");
        ImageIO.write(bufferedImage, "jpg", imageFile);
        return Contact.uploadImage(contact, imageFile);
    }
}
