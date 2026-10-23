import java.net.URI

plugins {
    application
	java
	id("org.springframework.boot") version "4.0.0"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "dev"
version = "3.0"
description = "Server handler for api.xplate.dev"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
	}
}

application {
    mainClass = "dev.xplate.api.XplateAPILauncher"
}

repositories {
	mavenCentral()
	maven { url = URI("https://jitpack.io") }
}

dependencies {
	// https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-webmvc
	implementation("org.springframework.boot:spring-boot-starter-webmvc:4.0.1")	
	// https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-actuator
	implementation("org.springframework.boot:spring-boot-starter-actuator:4.0.1")

	// https://mvnrepository.com/artifact/commons-io/commons-io
	implementation("commons-io:commons-io:2.21.0")

	// https://mvnrepository.com/artifact/com.google.code.gson/gson
	implementation("com.google.code.gson:gson:2.13.2")

	//https://github.com/Querz/NBT
	implementation("com.github.Querz:NBT:6.1")
}

tasks.test {
    failOnNoDiscoveredTests = false
}