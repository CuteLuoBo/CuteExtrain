package com.github.cuteluobo.CommandHandlers;

import com.github.cuteluobo.CuteExtra;
import net.mamoe.mirai.console.command.Command;
import net.mamoe.mirai.console.command.CommandOwner;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.CompositeCommand;
import net.mamoe.mirai.console.command.descriptor.CommandArgumentContext;
import net.mamoe.mirai.console.permission.Permission;
import net.mamoe.mirai.console.permission.PermissionId;
import net.mamoe.mirai.console.permission.PermissionService;
import org.jetbrains.annotations.NotNull;

/**
 * @author CuteLuoBo
 * @date 2021-04-07
 */
public class RollCommand extends CompositeCommand {
    private static String[] secondCommand = {"抽卡","r"};

    /**指令初始化*/
    public RollCommand() {
        super(CuteExtra.INSTANCE, "roll", secondCommand, "/roll 10",Permission.getRootPermission(),null);
    }

}
