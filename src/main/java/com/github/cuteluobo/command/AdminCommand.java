package com.github.cuteluobo.command;

import com.github.cuteluobo.CuteExtra;
import com.github.cuteluobo.repository.GlobalConfig;
import com.github.cuteluobo.repository.WebUiServerRepository;
import com.github.cuteluobo.task.MyThreadFactory;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.CompositeCommand;
import net.mamoe.mirai.console.command.descriptor.SimpleCommandArgumentContext;
import net.mamoe.mirai.console.permission.PermissionId;
import net.mamoe.mirai.console.permission.PermissionRegistryConflictException;
import net.mamoe.mirai.console.permission.PermissionService;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.MessageChain;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 管理员指令
 * @author CuteLuoBo
 * @date 2022/10/14 17:53
 */
public class AdminCommand extends CompositeCommand {
    //TODO 增加重载和线上添加服务

    Logger logger = LoggerFactory.getLogger(AdminCommand.class);

    private static final String[] SECOND_COMMAND = {"admin","manage","管理"};
    private static final String PRIMARY = "manage";

    public AdminCommand() throws PermissionRegistryConflictException {
        super(CuteExtra.INSTANCE, PRIMARY, SECOND_COMMAND
                ,"机器人管理"
                , PermissionService.getInstance().register(new PermissionId(CuteExtra.PLUGIN_ID,PRIMARY)
                        ,"机器人管理操作权限"
                        ,CuteExtra.INSTANCE.getAdminPermission())
                , SimpleCommandArgumentContext.EMPTY);
    }

    @SubCommand({"reload","重载"})
    public Boolean reload(@NotNull CommandSender sender,String modelName) {
        User user = sender.getUser();
        if (user != null) {
            //只允许管理员执行
            if (user.getId() != GlobalConfig.ADMIN_ID) {
                return false;
            }
        }
        if ("config".equalsIgnoreCase(modelName)) {
            try {
                reloadConfig();
                sender.sendMessage("配置重载完成");
            } catch (Exception e) {
                sender.sendMessage("配置重载出现错误：\n"+e.getLocalizedMessage());
                logger.error("配置重载出现错误:",e);
            }
        }
        return true;
    }

    private void reloadConfig() {
        GlobalConfig.getInstance().reloadConfig();
        WebUiServerRepository.getInstance().reloadServerList();
    }
}
