package com.github.cuteluobo;

import kotlin.coroutines.CoroutineContext;
import kotlinx.coroutines.Job;
import net.mamoe.mirai.console.permission.PermissionId;
import net.mamoe.mirai.console.permission.PermissionRegistryConflictException;
import net.mamoe.mirai.console.permission.PermissionService;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.message.MessageReceipt;
import net.mamoe.mirai.message.data.*;
import net.mamoe.mirai.utils.MiraiLogger;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * TODO 参考文档：https://github.com/Karlatemp/mirai-console-example-plugin/blob/master/src/main/kotlin/org/example/my/plugin/MyPluginMain.kt
 * https://github.com/mamoe/mirai-console/blob/master/docs/Commands.md
 * https://github.com/mamoe/mirai-console/blob/master/docs/PluginData.md
 * https://github.com/mamoe/mirai-console/blob/master/docs/Permissions.md
 * 主类
 * @author Cute_LuoBo
 */
public final class SimulatedExtraction extends JavaPlugin {

    private MiraiLogger logger  = getLogger();

    /**初始化*/
    public SimulatedExtraction() {
        super(new JvmPluginDescriptionBuilder(
                "SimulatedExtraction",
                "0.1.0")
                .author("Cute_LuoBo-635419450")
                .info("模拟抽卡插件")
                .build());
    }



    public void onLoad() {

    }

    @Override
    public void onEnable() {
        try {
            permissionExecute();
        } catch (PermissionRegistryConflictException e) {
            e.printStackTrace();
        }
    }

    /**初始化权限*/
    private void permissionExecute() throws PermissionRegistryConflictException {
        PermissionService.getInstance().register(new PermissionId("CuteExtra","normal"),"插件默认权限",null);
    }

}          