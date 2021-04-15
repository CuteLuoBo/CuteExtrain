package com.github.cuteluobo.untils;

import com.github.cuteluobo.CuteExtra;

/**
 * 指令限制器
 * @author CuteLuoBo
 * @date 2021-04-15
 */
public class CommandLimitUntil {
    private static CommandLimitUntil INSTANCE ;

    //TODO 构思用来储存用户执行指令的方式，纯嵌套map/map+对象

    private CommandLimitUntil(){

    }


    /**
     * 获取单例
     * @return 单例
     */
    public static CommandLimitUntil getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CommandLimitUntil();
        }
        return INSTANCE;
    }
}
