package com.example.javafxchar.util;

import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;

public class ContextUtil {
    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static void setApplicationContext(ApplicationContext applicationContext) {
        ContextUtil.applicationContext = applicationContext;
    }

    private static <T> T getContextBean (Class<T> tClass) {
        return applicationContext.getBean(tClass);
    }
}
