package dev.xplate;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import dev.xplate.bedrockr.BedrockRApp;
import dev.xplate.xplate.XplateApp;

public class XplateWebsiteServerApplication {

	public static void main(String[] args) {
		if (args.length < 3) {
			Logger.getGlobal().info("Not enough args");
			return;
		}
		// arg 0 is cert, 1 is private key, 2 is b or x, indicating what process starts.
		// 3 is bedrockR website path, 4 is xplate website path

		if (args[2].equals("b")) {
			BedrockRApp.init(args.length >= 4 ? args[3] : "");

			SpringApplication BedrockRApp = new SpringApplication(BedrockRApp.class);
			Map<String, Object> defaults1 = new HashMap<String, Object>();
			defaults1.put("server.ssl.bundle", "bundle1");
			defaults1.put("server.port", "8081");
			defaults1.put("spring.ssl.bundle.pem.bundle1.keystore.certificate", args[0]);
			defaults1.put(
					"spring.ssl.bundle.pem.bundle1.keystore.private-key", args[1]);

			BedrockRApp.setDefaultProperties(defaults1);
			BedrockRApp.run(args);
		} else {
			XplateApp.init(args.length >= 5 ? args[4] : "");

			SpringApplication XplateApp = new SpringApplication(XplateApp.class);
			Map<String, Object> defaults2 = new HashMap<String, Object>();
			defaults2.put("server.ssl.bundle", "bundle2");
			defaults2.put("server.port", "8082");
			defaults2.put("spring.ssl.bundle.pem.bundle2.keystore.certificate", args[0]);
			defaults2.put(
					"spring.ssl.bundle.pem.bundle2.keystore.private-key", args[1]);

			XplateApp.setDefaultProperties(defaults2);
			XplateApp.run(args);
		}
	}
}
