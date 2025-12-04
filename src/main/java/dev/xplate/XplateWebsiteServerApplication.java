package dev.xplate;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import dev.xplate.bedrockr.BedrockRApp;

public class XplateWebsiteServerApplication {

	public static void main(String[] args) {
		if (args.length < 2) {
			Logger.getGlobal().info("Missing key store info");
			return;
		}
		// arg 0 is cert, 1 is private key, 2 is public.
		// 3 is bedrockR website path, 4 is xplate website path

		BedrockRApp.init(args.length >= 3 ? args[2] : "");

		SpringApplication BedrockRApp = new SpringApplication(BedrockRApp.class);
		Map<String, Object> defaults = new HashMap<String, Object>();
		defaults.put("server.ssl.bundle", "mybundle");
		defaults.put("server.port", "8081");
		defaults.put("spring.ssl.bundle.pem.mybundle.keystore.certificate", args[0]);
		defaults.put(
				"spring.ssl.bundle.pem.mybundle.keystore.private-key", args[1]);
		defaults.put("spring.ssl.bundle.pem.mybundle.truststore.certificate", args[0]);

		BedrockRApp.setDefaultProperties(defaults);
		BedrockRApp.run(args);

		/*
		 * if (args.length >= 5)
		 * XplateApp.init(args[4]);
		 * SpringApplication XplateApp = new SpringApplication(XplateApp.class);
		 * XplateApp.setDefaultProperties(Collections.singletonMap("server.port",
		 * "8082"));
		 * 
		 * XplateApp.run(args);
		 */
	}

}
