package com.example.javafxchar;

import com.example.javafxchar.util.ContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class MainApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(MainApplication.class, args);
        ContextUtil.setApplicationContext(context);

        CharApplication fxApplication = new CharApplication();
        fxApplication.startChar();
    }
}
