package dev.xplate.api;

import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class XplateAPILauncher {

	public static void main(String[] args) {
		// arg 0 is cert, 1 is private key
		SpringApplication APIApp = new SpringApplication(XplateAPILauncher.class);
		Map<String, Object> defaults1 = new HashMap<String, Object>();
		if (args.length >= 2) {
			defaults1.put("server.ssl.enabled", "true");
			defaults1.put("server.ssl.bundle", "bundle1");
			defaults1.put("server.port", "8081");
			defaults1.put("spring.ssl.bundle.pem.bundle1.keystore.certificate", args[0]);
			defaults1.put(
					"spring.ssl.bundle.pem.bundle1.keystore.private-key", args[1]);
		}
		APIApp.setDefaultProperties(defaults1);
		APIApp.run(args);
	}
}
