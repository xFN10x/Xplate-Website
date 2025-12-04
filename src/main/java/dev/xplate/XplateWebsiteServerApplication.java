package dev.xplate;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import dev.xplate.bedrockr.BedrockRApp;
import dev.xplate.xplate.XplateApp;

public class XplateWebsiteServerApplication {

	public static void main(String[] args) {
		if (args.length < 3) {
			System.out.println("Missing key store info");
		}
		// arg 0 is keystore loc, 1 is password, 2 is name.
		// 3 is bedrockR website path, 4 is xplate website path
		if (args.length >= 4)
			BedrockRApp.init(args[3]);

		SpringApplication BedrockRApp = new SpringApplication(BedrockRApp.class);
		BedrockRApp.setDefaultProperties(Collections.singletonMap("server.port", "8081"));
		BedrockRApp.setDefaultProperties(Collections.singletonMap("server.ssl.key-store", args[0]));
		BedrockRApp.setDefaultProperties(Collections.singletonMap("server.ssl.store-password", args[1]));
		BedrockRApp.setDefaultProperties(Collections.singletonMap("server.ssl.store-alias", args[2]));
		BedrockRApp.run(args);

		if (args.length >= 5)
			XplateApp.init(args[4]);
		SpringApplication XplateApp = new SpringApplication(XplateApp.class);
		XplateApp.setDefaultProperties(Collections.singletonMap("server.port", "8082"));
		XplateApp.setDefaultProperties(Collections.singletonMap("server.ssl.key-store", args[0]));
		BedrockRApp.setDefaultProperties(Collections.singletonMap("server.ssl.store-password", args[1]));
		BedrockRApp.setDefaultProperties(Collections.singletonMap("server.ssl.store-alias", args[2]));
		XplateApp.run(args);
	}

}
