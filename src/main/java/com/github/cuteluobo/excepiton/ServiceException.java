package com.github.cuteluobo.excepiton;

/**
 * 服务错误
 * @author CuteLuoBo
 * @date 2022/10/10 1:39
 */
public class ServiceException extends Exception {
    public ServiceException(String message) {
        super(message);
    }
}
