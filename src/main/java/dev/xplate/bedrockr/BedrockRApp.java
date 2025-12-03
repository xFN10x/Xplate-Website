package dev.xplate.bedrockr;

import java.nio.file.Path;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("dev.xplate.bedrockr")
public class BedrockRApp {
    public static String WEBSITE_PATH;

    public static void init(String[] args) {
        if (args.length >= 1 && Path.of(args[0]).toFile().exists()) {
            WEBSITE_PATH = args[0];
            System.out.println("Set website path to: " + args[0]);
        }
    }
}
