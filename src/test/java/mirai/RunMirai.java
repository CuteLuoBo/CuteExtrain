package mirai;

import com.github.cuteluobo.CuteExtra;
import com.github.cuteluobo.service.Impl.YysImgOutputServiceImpl;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.GlobalScope;
import net.mamoe.mirai.console.command.Command;
import net.mamoe.mirai.console.command.CommandManager;
import net.mamoe.mirai.console.plugin.PluginManager;
import net.mamoe.mirai.console.terminal.MiraiConsoleImplementationTerminal;
import net.mamoe.mirai.console.terminal.MiraiConsoleTerminalLoader;

public class RunMirai {
   
    // 执行 gradle task: runMiraiConsole 来自动编译, shadow, 复制, 并启动 pure console.

    public static void main(String[] args) throws InterruptedException {// 默认在 /test 目录下运行

        MiraiConsoleTerminalLoader.INSTANCE.startAsDaemon(new MiraiConsoleImplementationTerminal()); // 启动 console
        PluginManager.INSTANCE.loadPlugin(CuteExtra.INSTANCE);
        PluginManager.INSTANCE.enablePlugin(CuteExtra.INSTANCE);
//        // 阻止主线程退出
        BuildersKt.runBlocking(GlobalScope.INSTANCE.getCoroutineContext(), (coroutineScope, continuation) -> CommandManager.INSTANCE.registerCommand((Command) continuation,false));
    }
}