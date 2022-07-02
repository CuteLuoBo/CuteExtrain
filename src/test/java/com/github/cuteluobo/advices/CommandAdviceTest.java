package com.github.cuteluobo.advices;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandAdviceTest {
    CommandAdvice commandAdvice = new CommandAdvice();

    @Test
    void commandLimitCheck() {
    }
}