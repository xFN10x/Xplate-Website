package dev.xplate.xplate;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("dev.xplate.xplate")
public class XplateApp {
    public static String WEBSITE_PATH;

    public static void init(String websitePath) {
        if (Path.of(websitePath).toFile().exists() && !websitePath.isEmpty()) {
            WEBSITE_PATH = websitePath;
            Logger.getGlobal().info("Set website path to: " + websitePath);
        } else {
            Logger.getGlobal().info("Cloning Website from https://github.com/xFN10x/Personal-Website.git:deploy");

            ProcessBuilder proc = new ProcessBuilder();
            proc.directory(Path.of(System.getProperty("user.home")).toFile());
            if (Path.of(System.getProperty("user.home"), "Personal-Website").toFile().exists()) {
                try {
                    FileUtils
                            .deleteDirectory(Path.of(System.getProperty("user.home"), "Personal-Website").toFile());
                } catch (IOException e) {
                    Logger.getGlobal().log(Level.SEVERE,
                            "Failed to remove website dir", e);
                }
            }
            proc.command("git", "clone", "-b", "deploy",
                    "https://github.com/xFN10x/Personal-Website.git");
            try {
                proc.start();
                WEBSITE_PATH = Path.of(System.getProperty("user.home"), "Personal-Website").toString();
            } catch (IOException e) {
                Logger.getGlobal().log(Level.SEVERE,
                        "Failed to get website from git", e);
            }
        }
    }
}
