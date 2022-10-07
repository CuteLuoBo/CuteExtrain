package com.github.cuteluobo.command;

import com.github.cuteluobo.CuteExtra;
import com.github.cuteluobo.model.YysUnit;
import com.github.cuteluobo.repository.GlobalConfig;
import com.github.cuteluobo.repository.InvitedEventRepository;
import com.github.cuteluobo.service.Impl.YysInfoUpdateServiceImpl;
import com.github.cuteluobo.service.Impl.YysRollServiceImpl;
import com.github.cuteluobo.util.UnitsInfoUpdateUtils;
import net.mamoe.mirai.console.command.CommandOwner;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.CommandSenderOnMessage;
import net.mamoe.mirai.console.command.CompositeCommand;
import net.mamoe.mirai.console.command.descriptor.SimpleCommandArgumentContext;
import net.mamoe.mirai.console.command.java.JCompositeCommand;
import net.mamoe.mirai.console.permission.PermissionId;
import net.mamoe.mirai.console.permission.PermissionRegistryConflictException;
import net.mamoe.mirai.console.permission.PermissionService;
import net.mamoe.mirai.event.events.BotInvitedJoinGroupRequestEvent;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * 阴阳师式神信息指令
 * @author CuteLuoBo
 * @date 2022/9/13 14:54
 */
public class YysUnitInfoCommand extends CompositeCommand {


    private static final String[] SECOND_COMMAND = {"yui","阴阳师式神信息"};
    private static final String PRIMARY = "yys_unit_info";
    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(0, 1, 30, TimeUnit.SECONDS
            , new SynchronousQueue<Runnable>(true)
            ,Executors.defaultThreadFactory());

    public YysUnitInfoCommand() throws PermissionRegistryConflictException {
        super(CuteExtra.INSTANCE, PRIMARY, SECOND_COMMAND
                ,"阴阳师式神信息操作"
                , PermissionService.getInstance().register(new PermissionId(CuteExtra.PLUGIN_ID,PRIMARY)
                        ,"阴阳师式神信息操作权限"
                        ,CuteExtra.INSTANCE.getParentPermission())
                , SimpleCommandArgumentContext.EMPTY);
    }

    /**
     * 从官网全量更新信息
     * @param sender 消息发送者
     * @return 操作结果
     */
    @SubCommand({"allUpdate","allup","全量更新"})
    public Boolean allUpdate(CommandSender sender){
        if (sender.getUser() != null && GlobalConfig.ADMIN_ID == sender.getUser().getId()) {
            long startTime = System.currentTimeMillis();
            //提交异步任务后直接返回指令正常执行结果
            threadPoolExecutor.submit(() -> {
                MessageChainBuilder messageChainBuilder = new MessageChainBuilder();
                //尝试更新式神信息
//                Callable<List<YysUnit>> task = UnitsInfoUpdateUtils.appendUnitsListTask();
                Callable<List<YysUnit>> task = YysInfoUpdateServiceImpl.getInstance().updateAllUnitInfoTask();
                try {
                    List<YysUnit> newUnitList = task.call();
                    long downloadInfoEndTime = System.currentTimeMillis();
                    if (newUnitList == null) {
                        sender.sendMessage("从官网获取式神信息失败");
                        return;
                    }
                    //下载新式神图片
                    List<Integer> newUnitIdList = newUnitList.stream().map(YysUnit::getUnitId).collect(Collectors.toList());
                    Callable<List<Integer>> imgTask = UnitsInfoUpdateUtils.updateUnitImageTask(newUnitIdList);
                    try {
                        List<Integer> okUnitIdList = imgTask.call();
                        long endTime = System.currentTimeMillis();
                        if (okUnitIdList != null) {
                            messageChainBuilder.append("更新操作完成：").append("\n").append("本次更新式神数量: ").append(String.valueOf(newUnitList.size())).append("\n").append("更新图片式神数量: ").append(String.valueOf(okUnitIdList.size())).append("\n");
                            messageChainBuilder.append("耗时:").append("\n")
                                    .append("式神信息").append(String.valueOf(downloadInfoEndTime - startTime)).append(" ms").append(", ")
                                    .append("式神图片").append(String.valueOf(endTime - downloadInfoEndTime)).append(" ms").append(",")
                                    .append("\n");
                        }
                    } catch (Exception e) {
                        sender.sendMessage("式神图片下载失败，错误消息："+e.getLocalizedMessage()+"更多消息请查看服务器日志");
                        CuteExtra.INSTANCE.getLogger().error("式神图片下载失败", e);
                    }
                    //小于阈值时，打印式神信息
                    int printMaxSize = 10;
                    if (newUnitList.size() <= printMaxSize) {
                        messageChainBuilder.append("详细列表：").append("\n");
                        newUnitList.forEach(u ->{
                            messageChainBuilder.append(String.valueOf(u.getUnitId())).append("-").append(u.getLevel()).append(" ").append(u.getName()).append("\n");
                        });
                    }
                    //重载抽卡数据库
                    YysRollServiceImpl.INSTANCE.reloadData();
                    sender.sendMessage(messageChainBuilder.build());
                } catch (Exception e) {
                    sender.sendMessage("式神资料更新失败，错误消息："+e.getLocalizedMessage()+"更多消息请查看服务器日志");
                    CuteExtra.INSTANCE.getLogger().error("式神资料更新失败", e);
                }
                sender.sendMessage("");
            });
            sender.sendMessage("指令已提交，后台运行中，等待结果");
            return true;
        }
        return false;
    }

    //TODO 增加数据库式神的信息读取与修改
}
