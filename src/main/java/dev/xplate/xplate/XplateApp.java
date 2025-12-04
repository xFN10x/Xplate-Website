package dev.xplate.xplate;

import java.nio.file.Path;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("dev.xplate.xplate")
public class XplateApp {
    public static String WEBSITE_PATH;

    public static void init(String websitePath) {
        if (Path.of(websitePath).toFile().exists()) {
            WEBSITE_PATH = websitePath;
            System.out.println("Set website path to: " + websitePath);
        }
    }
}
