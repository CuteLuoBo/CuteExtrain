package com.github.cuteluobo.command;

import com.amihaiemil.eoyaml.YamlMapping;
import com.amihaiemil.eoyaml.YamlNode;
import com.github.cuteluobo.CuteExtra;
import com.github.cuteluobo.enums.config.MainConfigEnum;
import com.github.cuteluobo.enums.config.impl.AiDrawConfigEnum;
import com.github.cuteluobo.enums.config.impl.WebUiConfig;
import com.github.cuteluobo.pojo.aidraw.AiImageCreateImg2ImgParameter;
import com.github.cuteluobo.pojo.aidraw.AiImageCreateParameter;
import com.github.cuteluobo.pojo.aidraw.StableDiffusionWebUiMethod;
import com.github.cuteluobo.repository.GlobalConfig;
import com.github.cuteluobo.repository.MessageTemp;
import com.github.cuteluobo.repository.WebUiServerRepository;
import com.github.cuteluobo.service.AiDrawService;
import com.github.cuteluobo.service.Impl.WebUiAiDrawServiceImpl;
import com.github.cuteluobo.task.BaseDownloader;
import com.github.cuteluobo.task.MyThreadFactory;
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
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.MessageReceipt;
import net.mamoe.mirai.message.action.AsyncRecallResult;
import net.mamoe.mirai.message.data.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * AI绘图指令
 * @author CuteLuoBo
 * @date 2022/10/8 14:01
 */
public class AiDrawCommand extends CompositeCommand {
    //TODO 增加重载和线上添加服务

    Logger logger = LoggerFactory.getLogger(AiDrawCommand.class);

    private static final String[] SECOND_COMMAND = {"aidraw","ad","ai绘图"};
    private static final String PRIMARY = "aidraw";

    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 10, 30, TimeUnit.SECONDS
            , new SynchronousQueue<>(true)
            ,new MyThreadFactory("AiDrawCommandPools"));

    public AiDrawCommand() throws PermissionRegistryConflictException {
        super(CuteExtra.INSTANCE, PRIMARY, SECOND_COMMAND
                ,"AI绘图"
                , PermissionService.getInstance().register(new PermissionId(CuteExtra.PLUGIN_ID,PRIMARY)
                        ,"AI绘图操作权限"
                        ,CuteExtra.INSTANCE.getBasePermission())
                , SimpleCommandArgumentContext.EMPTY);
    }

    //TODO 解决引用消息时，自动带的at导致无法直接输入命令的问题

    @SubCommand({"translate","tr","翻译"})
    public Boolean translate(@NotNull CommandSenderOnMessage<MessageEvent> sender,  MessageChain messageChain) {
        User user = sender.getUser();
        if (user != null) {
            return tagsMessageHandlerAndExecTask(true, true, true,true, sender, sender.getFromEvent().getMessage(),user.getId() != GlobalConfig.ADMIN_ID);
        }
        return true;
    }

    @SubCommand({"normal","n","默认"})
    public Boolean normal(@NotNull CommandSenderOnMessage<MessageEvent> sender, MessageChain messageChain){
        MessageEvent event = sender.getFromEvent();
        User user = sender.getUser();
        if (user != null) {
            return tagsMessageHandlerAndExecTask(true, true, true,false, sender, sender.getFromEvent().getMessage(),user.getId() != GlobalConfig.ADMIN_ID);
        }
        return true;
    }

    @SubCommand({"nosafe","h","不安全生成"})
    public Boolean nosafe(@NotNull CommandSenderOnMessage<MessageEvent> sender, MessageChain messageChain){
        User user = sender.getUser();
        //只处理非控制台消息
        if (user != null) {
            //只允许管理员执行
            if (user.getId() != GlobalConfig.ADMIN_ID) {
                return false;
            }
            return tagsMessageHandlerAndExecTask(false, true, true,true, sender, sender.getFromEvent().getMessage(),user.getId() != GlobalConfig.ADMIN_ID);
        }
        return false;
    }

    @SubCommand({"loadToken","lt","加载token"})
    public Boolean loadToken(@NotNull CommandSender sender, String... message){
        MessageChainBuilder chainBuilder = new MessageChainBuilder();
        User user = sender.getUser();
        long userId;
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
    public Boolean help(@NotNull CommandSender sender){
        MessageChainBuilder chainBuilder = new MessageChainBuilder();
        //回复消息
        if (sender instanceof CommandSenderOnMessage<?>) {
            CommandSenderOnMessage<?> senderOnMessage = (CommandSenderOnMessage<?>) sender;
            chainBuilder.append(new QuoteReply(senderOnMessage.getFromEvent().getSource()));
        }
        //消息主体
        chainBuilder.append("===AI绘图===").append("\n")
                .append("- 传入指定tags生成相关图片，描述越详细越精细，但禁止涩涩").append("\n")
                .append("- 可用前缀/aidraw 或 /ad").append("\n")
                .append("- 参数A：normal(n) -> 普通/直接提交tags不进行转换（建议）").append("\n")
                .append("- 参数B：translate(tr) -> 翻译/转为英文后提交，有机翻词不达意问题").append("\n")
                .append("- 注意！对于英文tags带空格的，需要使用’_‘(下划线)连接，否则会自动分割").append("\n")
                .append("- 默认生成尺寸为512x768，可通过高级参数调整").append("\n")
                .append("- 高级参数列表1：w=宽度(int)  h=高度(int)  seed=种子(long) steps=步数(int) p=提示 lp=负面提示").append("\n")
                .append("- 高级参数列表2：m=采样方法(string) dn=去噪强度(double) scale=提示可信度(double)").append("\n")
                .append("- 示例A：/ad n miku,lolita,flat_chest")
                .append("- 示例B：/ad n w=512 h=512 seed=998 miku,lolita,flat_chest")
                .append("- 示例C：/ad n w=512 h=512 \"p=miku,lolita,flat_chest\" \"lp=lowres, bad anatomy, bad hands\"");
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
    private boolean tagsMessageHandlerAndExecTask(boolean safe, boolean printCostTime, boolean showTags,boolean allowTranslate,CommandSender sender,MessageChain message,boolean hasLimit){
        MessageChainBuilder chainBuilder = new MessageChainBuilder();
        //回复消息
        if (sender instanceof CommandSenderOnMessage<?>) {
            CommandSenderOnMessage<?> senderOnMessage = (CommandSenderOnMessage<?>) sender;
            //回复源消息
            chainBuilder.append(new QuoteReply(senderOnMessage.getFromEvent().getSource()));
            YamlMapping configMapping = GlobalConfig.getInstance().getAiDrawMapping();
            String aiDrawEnable = configMapping.string(AiDrawConfigEnum.ENABLE.getLabel());
            if (configMapping == null || aiDrawEnable == null || !"true".equalsIgnoreCase(aiDrawEnable.trim())) {
                chainBuilder.append("ai绘图功能已关闭");
                sender.sendMessage(chainBuilder.build());
                return false;
            }
            //webui未启用时，检验novelai
            if (WebUiServerRepository.getInstance().isEmpty()) {
                String novelaiToken = configMapping.string(AiDrawConfigEnum.NOVELAI_TOKEN.getLabel());
                if (novelaiToken == null || novelaiToken.trim().length() == 0) {
                    chainBuilder.append("无可用服务，此功能无效");
                    sender.sendMessage(chainBuilder.build());
                    return false;
                }
            }
            AiImageCreateParameter aiImageCreateParameter;
            try{
                aiImageCreateParameter = messageChainParse(message,allowTranslate,hasLimit);
            } catch (NumberFormatException e) {
                chainBuilder.append("高级参数解析出现错误-数值转换异常\n").append(e.getLocalizedMessage());
                logger.debug("高级参数解析出现错误-数值转换错误", e);
                sender.sendMessage(chainBuilder.build());
                return false;
            } catch (IllegalArgumentException e) {
                chainBuilder.append("高级参数解析出现错误-传入枚举值不存在\n").append(e.getLocalizedMessage());
                logger.debug("高级参数解析出现错误-传入枚举值不存在", e);
                sender.sendMessage(chainBuilder.build());
                return false;
            } catch (Exception e) {
                chainBuilder.append("高级参数解析出现错误\n").append(e.getLocalizedMessage());
                logger.debug("用户指令高级参数解析出现错误", e);
                sender.sendMessage(chainBuilder.build());
                return false;
            }
//            if (String.join("", message).trim().length() == 0) {
//                chainBuilder.append("传入tags不能为空");
//                sender.sendMessage(chainBuilder.build());
//                return false;
//            }
            try {
                threadPoolExecutor.submit(createAiDrawTask(aiImageCreateParameter,safe,printCostTime,showTags,allowTranslate,chainBuilder.copy(),senderOnMessage));
                chainBuilder.append("任务已提交到队列，请耐心等待\n")
                        .append("等待任务数：").append(String.valueOf(threadPoolExecutor.getActiveCount())).append("\n")
                        .append("本次启动总累计完成任务数：").append(String.valueOf(threadPoolExecutor.getCompletedTaskCount()));
                MessageChain messages = chainBuilder.build();
                MessageReceipt<Contact> messageReceipt = sender.sendMessage(messages);
                //3秒后进行撤回避免刷屏
                if (messageReceipt != null) {
                    messageReceipt.recallIn(3000L);
                }
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
     * @param aiImageCreateParameter AI图片对象
     * @param safe          是否安全(NSFW)
     * @param printCostTime 打印耗时
     * @param showTags      显示tags
     * @param allowTranslate 允许自动翻译
     * @param chainBuilder  消息构造器
     * @param sender        指令执行者（用于异步返回）
     * @return 创建任务
     */
    private Runnable createAiDrawTask(AiImageCreateParameter aiImageCreateParameter, boolean safe, boolean printCostTime, boolean showTags,boolean allowTranslate, MessageChainBuilder chainBuilder, CommandSenderOnMessage<?> sender){
        return () -> {
            try {
                long statTime = System.currentTimeMillis();
                //判断是否含有中文文字以执行自动翻译
                String finalTags = aiImageCreateParameter.getPrompt();
                if (allowTranslate) {
                    if (StringUtils.isContainChinese(finalTags)) {
                        try {
                            aiImageCreateParameter.setPrompt(TranslateUtils.autoToEn(finalTags));
                        } catch (Exception e) {
                            chainBuilder.append("调用翻译API失效，使用直接传入").append("\n");
                            logger.error("调用翻译API失败", e);
                        }
                    }
                }
                //从API中获取图片信息
//                byte[] bytes = AiImgUtils.getImg("masterpiece, best quality,"+finalTags, safe);
                //获取服务器
                AiDrawService aiDrawService = WebUiServerRepository.getInstance().randomService(safe);
                if (aiDrawService == null) {
                    chainBuilder.append("无可用服务，指令执行失败");
                    sender.sendMessage(chainBuilder.build());
                    return;
                }
                List<byte[]> imgList;
                if (aiImageCreateParameter instanceof AiImageCreateImg2ImgParameter) {
                    imgList = aiDrawService.img2img((AiImageCreateImg2ImgParameter)aiImageCreateParameter);
                } else {
                    imgList = aiDrawService.txt2img(aiImageCreateParameter);
                }
                long getImgEndTime = System.currentTimeMillis();
                if (imgList.size() == 0) {
                    chainBuilder.append("无图片数据，可能结果：服务器连接token失效/生成参数错误");
                    sender.sendMessage(chainBuilder.build());
                    return;
                }
                long imgSaveTime = System.currentTimeMillis();
                List<File> fileList = saveFileList(imgList, finalTags, safe);
                //使用mirai接口上传
                for (File f :
                        fileList) {
                    Image image = Contact.uploadImage(Objects.requireNonNull(sender.getSubject()), f);
                    chainBuilder.append(image);
                }
                long imgUploadTime = System.currentTimeMillis();
                //耗时数据
                if (printCostTime){
                    chainBuilder.append("耗时：").append(String.valueOf(getImgEndTime - statTime)).append("+")
                            .append(String.valueOf(imgSaveTime - getImgEndTime)).append("+")
                            .append(String.valueOf(imgUploadTime - imgSaveTime)).append("=")
                            .append(String.valueOf(imgUploadTime - statTime)).append("ms")
                            .append(" ≈ ").append(BigDecimal.valueOf(imgUploadTime - statTime).divide(BigDecimal.valueOf(1000),2, RoundingMode.CEILING).toString()).append("s")
                    ;
                }
                //数据排版优化
                if (printCostTime && showTags) {
                    chainBuilder.append("\n");
                }
                //展示tags
                if (showTags) {
                    chainBuilder.append("生成tags：").append(aiImageCreateParameter.getPrompt());
                }
                sender.sendMessage(chainBuilder.build());
            } catch (Exception e) {
                chainBuilder.append("图片生成错误： ").append(e.getLocalizedMessage());
                sender.sendMessage(chainBuilder.build());
                logger.error("生成/上传图片错误", e);
            }
        };
    }

    private List<File> saveFileList(@NotNull List<byte[]> dataList,String tags,boolean safe){
        List<File> fileList = new ArrayList<>(dataList.size());
        for (byte[] bytes : dataList
        ) {
            //保存到本地
            File file;
            String saveTags = tags.replace(File.separatorChar, '-');
            try {
                file = FileIoUtils.createFileTemp(safe ? "safe" : "noSafe", (saveTags.length() > 64 ? saveTags.substring(0, 64) : saveTags) + ".png");
                Files.write(file.toPath(), bytes, StandardOpenOption.CREATE);
                fileList.add(file);
            }
            //可能的IO错误
            catch (IOException ioException) {
                logger.error("文件保存错误", ioException);
            }
        }
        return fileList;
    }

    /**
     * 传入信息链解析
     * @param parseMessage 待解析的信息
     * @param allowTranslate 允许启用翻译
     * @param hasLimit       指令是否有限制
     * @return 根据解析创建的图片生成数据
     */
    private AiImageCreateParameter messageChainParse(MessageChain parseMessage,boolean allowTranslate,boolean hasLimit) {
        AiImageCreateParameter aiImageCreateParameter;
        int steps = 30;
        int width = 512;
        int height = 768;
        int batchCount = 1;
        int batchSize = 1;
        long seed = System.currentTimeMillis() / 1000;
        BigDecimal scale = BigDecimal.valueOf(11);
        BigDecimal deNoise = new BigDecimal("0.7");
        String splitString = "=";
        String method = StableDiffusionWebUiMethod.Euler_A.getArgsText();
        Image uploadImage = null;
        Image quoteImage = null;
        //信息链解析
        Iterator<SingleMessage> iterator = parseMessage.iterator();
        List<String> argsList = new ArrayList<>();
        //提示
        List<String> promptList = new ArrayList<>();
        //自定义提示
        String customPrompt = null;
        String customLowPrompt = null;
        while (iterator.hasNext()) {
            SingleMessage sm = iterator.next();
            if (sm instanceof PlainText) {
                PlainText plainText = (PlainText) sm;
                String commandContent = plainText.getContent();
                //先解析以双引号划为整体的参数
                Matcher matcher = StringUtils.oneArg.matcher(commandContent);
                boolean hasFind = false;
                while (matcher.find()) {
                    hasFind = true;
                    argsList.add(matcher.group());
                }
                //取出后清空
                if (hasFind) {
                    commandContent = matcher.replaceAll("");
                }
                //第二次解析，取主指令与子指令之后传递的参数
                String[] args2 = commandContent.split(" ");
                int argsStartSplitIndex = 2;
                if (args2.length > argsStartSplitIndex) {
                    argsList.addAll(Arrays.asList(args2).subList(argsStartSplitIndex, args2.length));
                }
                //对累计的参数进行操作
                for (String arg :
                        argsList) {
                    //为特殊参数时进行解析，否则按默认情况存入提示列表中
                    if (arg.contains(splitString)) {
                        try {
                            String[] arr = arg.split(splitString);
                            if (arr.length >= 2) {
                                String key = arr[0];
                                String value = arr[1].trim();
                                switch (key) {
                                    case "steps":
                                        steps = Integer.parseInt(value);
                                        break;
                                    case "width":
                                    case "w":
                                        width = Integer.parseInt(value);
                                        break;
                                    case "height":
                                    case "h":
                                        height = Integer.parseInt(value);
                                        break;
                                    case "batchCount":
                                    case "batch-count":
                                    case "bc":
                                        batchCount = Integer.parseInt(value);
                                        break;
                                    case "batchSize":
                                    case "batch-size":
                                    case "bs":
                                        batchSize = Integer.parseInt(value);
                                        break;
                                    case "method":
                                    case "m":
                                        method = StableDiffusionWebUiMethod.valueOf(value).getArgsText();
                                        break;
                                    case "seed":
                                        seed = Long.parseLong(value);
                                        break;
                                    case "scale":
                                        scale = new BigDecimal(value);
                                        break;
                                    case "de-noise":
                                    case "deNoise":
                                    case "dn":
                                        deNoise = new BigDecimal(value);
                                        break;
                                    case "prompt":
                                    case "p":
                                        customPrompt = value;
                                        break;
                                    case "low-prompt":
                                    case "lowPrompt":
                                    case "lp":
                                        customLowPrompt = value;
                                        break;
                                    default:
                                }
                            }
                        } catch (NumberFormatException e) {
                            throw e;
                        } catch (IllegalArgumentException e) {
                            throw e;
                        } catch (Exception e) {
                            throw e;
                        }
                    }else {
                        promptList.add(arg);
                    }
                }
            }
            else if (sm instanceof Image) {
                uploadImage = (Image) sm;
            }
            //本次信息中没有读取图片时，读取回复的源信息，从中取图
            else if (uploadImage == null && sm instanceof QuoteReply) {
                QuoteReply quoteReply = (QuoteReply) sm;
                MessageSource source = quoteReply.getSource();

                //获取源数据ID
                int[] ids = source.getIds();
                if (ids.length > 0) {
                    int messageId = ids[0];
                    //尝试从缓存中获取储存的图片数据
                    MessageTemp messageTemp = MessageTemp.getInstance();
                    List<String> imageList = null;
                    //TODO 待找到合适的解决方案，目前无法通过转型进行分别获取
                    imageList = messageTemp.getFriendMessageImageIdMap().get(messageId);
                    if (imageList == null) {
                        imageList = messageTemp.getGroupMessageImageIdMap().get(messageId);
                    }
//                    if (source instanceof OnlineMessageSource.Incoming.FromFriend || source instanceof OnlineMessageSource.Outgoing.ToFriend) {
//                        imageList = messageTemp.getFriendMessageImageIdMap().get(messageId);
//                    } else if (source instanceof OnlineMessageSource.Incoming.FromGroup || source instanceof OnlineMessageSource.Outgoing.ToGroup) {
//                        imageList = messageTemp.getGroupMessageImageIdMap().get(messageId);
//                    }
                    //有数据时，进行赋值
                    if (imageList != null && imageList.size() > 0) {
                        String imageId = imageList.get(imageList.size()-1);
                        if (imageId != null) {
                            quoteImage = Image.fromId(imageId);
                        }
                    }
                }

            }
        }
        if (uploadImage == null && quoteImage != null) {
            uploadImage = quoteImage;
        }
        if (uploadImage != null) {
            //尝试从旧消息中取得图片实际数据
            try {
                String url = Image.queryUrl(uploadImage);
                File file = BaseDownloader.getInstance().downloadFileToTemp(new URL(url),".png");
                String base64 = FileIoUtils.loadFile2Base64(file);
                aiImageCreateParameter = new AiImageCreateImg2ImgParameter();
                //转型为i2i对象才可设置对应数据
                AiImageCreateImg2ImgParameter castTemp = (AiImageCreateImg2ImgParameter) aiImageCreateParameter;
                castTemp.setBase64ImgData("data:image/png;base64," + base64);
            } catch (Exception e) {
                aiImageCreateParameter = new AiImageCreateParameter();
                logger.error("获取消息中图片错误，转为普通文转图模式", e);
            }
        } else {
            aiImageCreateParameter = new AiImageCreateParameter();
        }
        //没有高级定义正面提示时，对筛出的参数进行装配
        if (customPrompt == null) {
            //翻译时URL传链接空格会出现未知错误
            if (allowTranslate) {
                customPrompt = String.join(",", promptList);
            } else {
                customPrompt = String.join(" ", promptList);
            }
        }
        //处理逗号并赋值
        customPrompt = StringUtils.tagsCommaHandler(customPrompt);
        aiImageCreateParameter.setPrompt(customPrompt);
        //定义负面提示时转换并赋值，否则使用默认
        if (customLowPrompt != null) {
            customLowPrompt = StringUtils.tagsCommaHandler(customLowPrompt);
            aiImageCreateParameter.setLowPrompt(customLowPrompt);
        }
        //对于宽高要求为64的倍数,自动尝试+64
        if (width % 64 != 0) {
            width = (width / 64 + 1) * 64;
        }
        if (height % 64 != 0) {
            height = (height / 64 + 1) * 64;
        }
        //是否启用限制，启用时从配置文件中读取
        if (hasLimit) {
            YamlMapping aiDrawMapping = GlobalConfig.getInstance().getAiDrawMapping();
            aiImageCreateParameter.setBatchCount(Math.min(batchCount,aiDrawMapping.integer(AiDrawConfigEnum.MAX_BATCH_COUNT.getLabel())));
            aiImageCreateParameter.setBatchSize(Math.min(batchSize,aiDrawMapping.integer(AiDrawConfigEnum.MAX_BATCH_SIZE.getLabel())));
            aiImageCreateParameter.setWidth(Math.min(width,aiDrawMapping.integer(AiDrawConfigEnum.MAX_WIDTH.getLabel())));
            aiImageCreateParameter.setHeight(Math.min(height,aiDrawMapping.integer(AiDrawConfigEnum.MAX_HEIGHT.getLabel())));
            aiImageCreateParameter.setSteps(Math.min(steps,aiDrawMapping.integer(AiDrawConfigEnum.MAX_STEPS.getLabel())));
            aiImageCreateParameter.setSeed(seed);
            aiImageCreateParameter.setCfgScale(scale);
            aiImageCreateParameter.setDenoisingStrength(deNoise);
            aiImageCreateParameter.setMethod(method);
        } else {
            aiImageCreateParameter.setBatchCount(batchCount);
            aiImageCreateParameter.setBatchSize(batchSize);
            aiImageCreateParameter.setWidth(width);
            aiImageCreateParameter.setHeight(height);
            aiImageCreateParameter.setSteps(steps);
            aiImageCreateParameter.setSeed(seed);
            aiImageCreateParameter.setCfgScale(scale);
            aiImageCreateParameter.setDenoisingStrength(deNoise);
            aiImageCreateParameter.setMethod(method);
        }
        return aiImageCreateParameter;
    }
}
