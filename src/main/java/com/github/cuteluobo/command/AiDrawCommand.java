package com.github.cuteluobo.command;

import com.github.cuteluobo.CuteExtra;
import com.github.cuteluobo.enums.RollModel;
import com.github.cuteluobo.pojo.RollResultData;
import com.github.cuteluobo.repository.GlobalConfig;
import com.github.cuteluobo.service.Impl.YysRollServiceImpl;
import com.github.cuteluobo.util.AiImgUtils;
import com.github.cuteluobo.util.FileIoUtils;
import net.mamoe.mirai.Mirai;
import net.mamoe.mirai.console.command.CommandOwner;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.CommandSenderOnMessage;
import net.mamoe.mirai.console.command.CompositeCommand;
import net.mamoe.mirai.console.command.descriptor.CommandArgumentContext;
import net.mamoe.mirai.console.command.descriptor.SimpleCommandArgumentContext;
import net.mamoe.mirai.console.permission.Permission;
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
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.stream.Collectors;

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

    @SubCommand({"normal","n","默认"})
    public Boolean normal(@NotNull CommandSender sender, String... message) throws IOException {
        MessageChainBuilder chainBuilder = new MessageChainBuilder();
        User user = sender.getUser();
        long userId = 0;
        if (user != null) {
            userId = user.getId();
            //回复消息
            if (sender instanceof CommandSenderOnMessage) {
                CommandSenderOnMessage senderOnMessage = (CommandSenderOnMessage) sender;
                chainBuilder.append(new QuoteReply(senderOnMessage.getFromEvent().getSource()));
                if (GlobalConfig.getInstance().getNovelaiToken() == null || GlobalConfig.getInstance().getNovelaiToken().trim().length() == 0) {
                    chainBuilder.append("token未加载，此功能无效");
                    sender.sendMessage(chainBuilder.build());
                    return false;
                }
                Runnable runnable = () -> {
                    try {
                        byte[] bytes = AiImgUtils.getImg(String.join("", message).trim(), true);
                        if (bytes.length == 0) {
                            chainBuilder.append("无图片数据，可能结果：token失效/tags错误");
                            sender.sendMessage(chainBuilder.build());
                            return;
                        }
                        File file = FileIoUtils.createFileTemp("AiImgUtilsTest", ".png");
                        Files.write(file.toPath(), bytes, StandardOpenOption.CREATE);
                        Image image = Contact.uploadImage(Objects.requireNonNull(sender.getSubject()), file);
                        chainBuilder.append(image);
                        sender.sendMessage(chainBuilder.build());
                    } catch (Exception e) {
                        chainBuilder.append("图片生成错误： "+e.getLocalizedMessage());
                        sender.sendMessage(chainBuilder.build());
                        logger.error("生成/上传图片错误", e);
                    }
                };
                try {
                    threadPoolExecutor.submit(runnable);
                } catch (RejectedExecutionException rejectedExecutionException) {
                    chainBuilder.append("任务队列已满，请稍后重试");
                }
            }
        }
        return true;
    }

    @SubCommand({"nosafe","h","不安全生成"})
    public Boolean nosafe(@NotNull CommandSender sender, String... message) throws IOException {
        MessageChainBuilder chainBuilder = new MessageChainBuilder();
        User user = sender.getUser();
        long userId = 0;
        if (user != null) {
            userId = user.getId();
            if (userId != GlobalConfig.ADMIN_ID) {
                return false;
            }

            //回复消息
            if (sender instanceof CommandSenderOnMessage) {
                CommandSenderOnMessage senderOnMessage = (CommandSenderOnMessage) sender;
                chainBuilder.append(new QuoteReply(senderOnMessage.getFromEvent().getSource()));
                if (GlobalConfig.getInstance().getNovelaiToken() == null || GlobalConfig.getInstance().getNovelaiToken().trim().length() == 0) {
                    chainBuilder.append("token未加载，此功能无效");
                    sender.sendMessage(chainBuilder.build());
                    return false;
                }
                Runnable runnable = () -> {
                    try {
                        byte[] bytes = AiImgUtils.getImg(String.join("", message).trim(), false);
                        File file = FileIoUtils.createFileTemp("AiImgUtilsTest", ".png");
                        Files.write(file.toPath(), bytes, StandardOpenOption.CREATE);
                        Image image = Contact.uploadImage(Objects.requireNonNull(sender.getSubject()), file);
                        chainBuilder.append(image);
                        sender.sendMessage(chainBuilder.build());
                    } catch (Exception e) {
                        chainBuilder.append("图片生成错误： "+e.getLocalizedMessage());
                        sender.sendMessage(chainBuilder.build());
                        logger.error("生成/上传图片错误", e);
                    }
                };
                try {
                    threadPoolExecutor.submit(runnable);
                } catch (RejectedExecutionException rejectedExecutionException) {
                    chainBuilder.append("任务队列已满，请稍后重试");
                }
            }
        }
        return true;
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
}
