package dev.xplate;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import dev.xplate.bedrockr.BedrockRApp;
import dev.xplate.xplate.XplateApp;

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
		Map<String, Object> defaults1 = new HashMap<String, Object>();
		defaults1.put("server.ssl.bundle", "mybundle");
		defaults1.put("server.port", "8081");
		defaults1.put("spring.ssl.bundle.pem.mybundle.keystore.certificate", args[0]);
		defaults1.put(
				"spring.ssl.bundle.pem.mybundle.keystore.private-key", args[1]);
		defaults1.put("spring.ssl.bundle.pem.mybundle.truststore.certificate", args[0]);

		BedrockRApp.setDefaultProperties(defaults1);
		BedrockRApp.run(args);

		XplateApp.init(args.length >= 4 ? args[3] : "");

		SpringApplication XplateApp = new SpringApplication(XplateApp.class);
		Map<String, Object> defaults2 = new HashMap<String, Object>();
		defaults2.put("server.ssl.bundle", "mybundle");
		defaults2.put("server.port", "8082");
		defaults2.put("spring.ssl.bundle.pem.mybundle.keystore.certificate", args[0]);
		defaults2.put(
				"spring.ssl.bundle.pem.mybundle.keystore.private-key", args[1]);
		defaults2.put("spring.ssl.bundle.pem.mybundle.truststore.certificate", args[0]);

		XplateApp.setDefaultProperties(defaults2);
		XplateApp.run(args);
	}

}
