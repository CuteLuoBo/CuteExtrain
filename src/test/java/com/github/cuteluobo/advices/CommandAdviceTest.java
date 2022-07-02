package com.github.cuteluobo.advices;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;
import org.aspectj.runtime.reflect.*;

import static org.junit.jupiter.api.Assertions.*;

class CommandAdviceTest {
    CommandAdvice commandAdvice = new CommandAdvice();

    @Test
    void commandLimitCheck() {
        //TODO 找到关于aspectJ中的切点测试
    }
}