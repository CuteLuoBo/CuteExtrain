package com.github.cuteluobo.command;

import com.github.cuteluobo.CuteExtra;
import com.github.cuteluobo.repository.GlobalConfig;
import com.github.cuteluobo.util.AiImgUtils;
import com.github.cuteluobo.util.FileIoUtils;
import com.github.cuteluobo.util.StringUtils;
import com.github.cuteluobo.util.TranslateUtils;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.CommandSenderOnMessage;
import net.mamoe.mirai.console.command.CompositeCommand;
import net.mamoe.mirai.console.command.descriptor.SimpleCommandArgumentContext;
import net.mamoe.mirai.console.permission.PermissionId;
import net.mamoe.mirai.console.permission.PermissionRegistryConflictException;
import net.mamoe.mirai.console.permission.PermissionService;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * AI绘图指令
 * @author CuteLuoBo
 * @date 2022/10/8 14:01
 */
public class AiDrawCommand extends CompositeCommand {

    Logger logger = LoggerFactory.getLogger(AiDrawCommand.class);

    private static final String[] SECOND_COMMAND = {"aidraw","ad","ai绘图"};
    private static final String PRIMARY = "aidraw";

    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 10, 30, TimeUnit.SECONDS
            , new SynchronousQueue<Runnable>(true)
            ,Executors.defaultThreadFactory());

    public AiDrawCommand() throws PermissionRegistryConflictException {
        super(CuteExtra.INSTANCE, PRIMARY, SECOND_COMMAND
                ,"AI绘图"
                , PermissionService.getInstance().register(new PermissionId(CuteExtra.PLUGIN_ID,PRIMARY)
                        ,"AI绘图操作权限"
                        ,CuteExtra.INSTANCE.getBasePermission())
                , SimpleCommandArgumentContext.EMPTY);
    }


    @SubCommand({"translate","tr","翻译"})
    public Boolean translate(@NotNull CommandSender sender, String... message) throws IOException {
        User user = sender.getUser();
        if (user != null) {
            return tagsMessageHandlerAndExecTask(true, true, true,true, sender, message);
        }
        return true;
    }

    @SubCommand({"normal","n","默认"})
    public Boolean normal(@NotNull CommandSender sender, String... message) throws IOException {
        User user = sender.getUser();
        if (user != null) {
            return tagsMessageHandlerAndExecTask(true, true, true,false, sender, message);
        }
        return true;
    }

    @SubCommand({"nosafe","h","不安全生成"})
    public Boolean nosafe(@NotNull CommandSender sender, String... message) throws IOException {
        User user = sender.getUser();
        //只处理非控制台消息
        if (user != null) {
            //只允许管理员执行
            if (user.getId() != GlobalConfig.ADMIN_ID) {
                return false;
            }
            return tagsMessageHandlerAndExecTask(false, true, true,true, sender, message);
        }
        return false;
    }

    @SubCommand({"loadToken","lt","加载token"})
    public Boolean loadToken(@NotNull CommandSender sender, String... message) throws IOException {
        MessageChainBuilder chainBuilder = new MessageChainBuilder();
        User user = sender.getUser();
        long userId = 0;
        if (user != null) {
            userId = user.getId();
            if (userId != GlobalConfig.ADMIN_ID) {
                return false;
            }
            String closeMessage = "null";
            //传入null时，表示清空token（暂时关闭功能）
            if (closeMessage.equals(String.join("", message).trim())) {
                GlobalConfig.getInstance().setNovelaiToken(null);
                sender.sendMessage("token已清空");
                return true;
            }
            String token = String.join(" ", message).trim();
            if (token.length() > 0) {
                try {
                    if (GlobalConfig.getInstance().saveNovelaiTokenFile(token)) {
                        sender.sendMessage("token保存成功");
                    } else {
                        sender.sendMessage("token保存失败");
                    }
                } catch (Exception e) {
                    sender.sendMessage("token保存时出现错误"+e.getLocalizedMessage());
                    logger.error("token保存出现错误", e);
                }
            }
        }
        return true;
    }

    @SubCommand({"help","帮助"})
    public Boolean help(@NotNull CommandSender sender) throws IOException {
        MessageChainBuilder chainBuilder = new MessageChainBuilder();
        //回复消息
        if (sender instanceof CommandSenderOnMessage) {
            CommandSenderOnMessage senderOnMessage = (CommandSenderOnMessage) sender;
            chainBuilder.append(new QuoteReply(senderOnMessage.getFromEvent().getSource()));
        }
        //消息主体
        chainBuilder.append("===AI绘图===").append("\n")
                .append("传入指定tags生成相关图片，描述越详细越精细，但禁止涩涩").append("\n")
                .append("可用前缀/aidraw 或 /ad").append("\n")
                .append("参数A：normal(n) -> 普通/直接提交tags不进行转换（建议）").append("\n")
                .append("参数B：translate(tr) -> 翻译/转为英文后提交，有机翻词不达意问题").append("\n")
                .append("提示，对于英文tags带空格的，需要使用’_‘(下划线)连接，否则会自动分割").append("\n")
                .append("示例：/ad n miku,lolita,flat_chest");
        sender.sendMessage(chainBuilder.build());
        return true;
    }

    /**
     * 封装信息操作和任务执行
     * @param safe          是否安全(NSFW)
     * @param printCostTime 打印耗时
     * @param showTags      显示tags
     * @param allowTranslate 允许自动翻译
     * @param sender        指令执行者（用于异步返回）
     * @param message        指令传入参数
     * @return 指令执行结果
     */
    private boolean tagsMessageHandlerAndExecTask(boolean safe, boolean printCostTime, boolean showTags,boolean allowTranslate,CommandSender sender,String... message){
        MessageChainBuilder chainBuilder = new MessageChainBuilder();
        //回复消息
        if (sender instanceof CommandSenderOnMessage) {
            CommandSenderOnMessage senderOnMessage = (CommandSenderOnMessage) sender;
            //回复源消息
            chainBuilder.append(new QuoteReply(senderOnMessage.getFromEvent().getSource()));
            if (GlobalConfig.getInstance().getNovelaiToken() == null || GlobalConfig.getInstance().getNovelaiToken().trim().length() == 0) {
                chainBuilder.append("token未加载，此功能无效");
                sender.sendMessage(chainBuilder.build());
                return false;
            }
            //校验tags
            if (String.join("", message).trim().length() == 0) {
                chainBuilder.append("传入tags不能为空");
                sender.sendMessage(chainBuilder.build());
                return false;
            }
            String tags = String.join(",", message);
            tags = StringUtils.tagsCommaHandler(tags);
            try {
                threadPoolExecutor.submit(createAiDrawTask(tags,safe,printCostTime,showTags,allowTranslate,chainBuilder,senderOnMessage));
            } catch (RejectedExecutionException rejectedExecutionException) {
                chainBuilder.append("任务队列已满，请稍后重试");
                sender.sendMessage(chainBuilder.build());
                return false;
            }
        }
        return true;
    }

    /**
     * 创建AI绘图任务
     * @param tags 传入的tags（不转换直接上传）
     * @param safe          是否安全(NSFW)
     * @param printCostTime 打印耗时
     * @param showTags      显示tags
     * @param allowTranslate 允许自动翻译
     * @param chainBuilder  消息构造器
     * @param sender        指令执行者（用于异步返回）
     * @return 创建任务
     */
    private Runnable createAiDrawTask(String tags, boolean safe, boolean printCostTime, boolean showTags,boolean allowTranslate, MessageChainBuilder chainBuilder, CommandSenderOnMessage sender){
        return () -> {
            try {
                long statTime = System.currentTimeMillis();
                //判断是否含有中文文字以执行自动翻译
                String finalTags = tags;
                if (allowTranslate) {
                    if (StringUtils.isContainChinese(tags)) {
                        try {
                            finalTags = TranslateUtils.autoToEn(tags);
                        } catch (Exception e) {
                            finalTags = tags;
                            chainBuilder.append("调用翻译API失效，使用直接传入");
                        }
                    }
                }
                //从API中获取图片信息
                byte[] bytes = AiImgUtils.getImg(finalTags, safe);
                long getImgEndTime = System.currentTimeMillis();
                if (bytes.length == 0) {
                    chainBuilder.append("无图片数据，可能结果：token失效/tags错误");
                    sender.sendMessage(chainBuilder.build());
                    return;
                }
                //保存到本地
                File file = FileIoUtils.createFileTemp(safe?"safe":"noSafe", finalTags.replace(File.separatorChar, '-')+".png");
                Files.write(file.toPath(), bytes, StandardOpenOption.CREATE);
                long imgSaveTime = System.currentTimeMillis();
                //使用mirai接口上传
                Image image = Contact.uploadImage(Objects.requireNonNull(sender.getSubject()), file);
                long imgUploadTime = System.currentTimeMillis();
                chainBuilder.append(image);
                //耗时数据
                if (printCostTime){
                    chainBuilder.append("耗时：").append(String.valueOf(getImgEndTime - statTime)).append("+")
                            .append(String.valueOf(imgSaveTime - getImgEndTime)).append("+")
                            .append(String.valueOf(imgUploadTime - imgSaveTime)).append("=")
                            .append(String.valueOf(imgUploadTime - statTime)).append("ms")
                    ;
                }
                if (showTags) {
                    chainBuilder.append("生成tags：").append(finalTags);
                }
                sender.sendMessage(chainBuilder.build());
            } catch (Exception e) {
                chainBuilder.append("图片生成错误： "+e.getLocalizedMessage());
                sender.sendMessage(chainBuilder.build());
                logger.error("生成/上传图片错误", e);
            }
        };
    }
}
