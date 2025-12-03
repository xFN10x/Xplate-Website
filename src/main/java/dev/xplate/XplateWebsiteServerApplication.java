package dev.xplate;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import dev.xplate.bedrockr.BedrockRApp;

public class XplateWebsiteServerApplication {

	public static void main(String[] args) {
		BedrockRApp.init(args);
		SpringApplication BedrockRApp = new SpringApplication(BedrockRApp.class);
		BedrockRApp.setDefaultProperties(Collections.singletonMap("server.port", "8081"));
		BedrockRApp.run(args);
	}

}
