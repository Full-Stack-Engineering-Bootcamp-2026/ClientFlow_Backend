package com.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

@SpringBootApplication(scanBasePackages = "com.app")
@EnableJpaAuditing
public class Application {

    public static void main(String[] args) throws Exception {

        File envFile = new File(".env");
        if (envFile.exists()) {
            Properties props = new Properties();
            props.load(new FileInputStream(envFile));
            props.forEach((key, value) ->
                System.setProperty(key.toString(), value.toString())
            );
        }

        SpringApplication.run(Application.class, args);
    }
}