plugins {
    application
	java
	id("org.springframework.boot") version "4.0.0"
	id("io.spring.dependency-management") version "1.1.7"
    id("com.gradleup.shadow") version "9.2.2"
}

group = "dev"
version = "1.0"
description = "Server handler for most Xplate.dev sub-domains"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
	}
}

application {
    mainClass = "dev.xplate.XplateWebsiteServerApplication"
}

repositories {
	mavenCentral()
}

dependencies {
	// https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-webmvc
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	implementation("org.springframework.boot:spring-boot-starter-actuator")

	// https://mvnrepository.com/artifact/commons-io/commons-io
	implementation("commons-io:commons-io:2.21.0")
}

tasks.test {
    failOnNoDiscoveredTests = false
}

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
  archiveBaseName = "XplateServer"
  archiveVersion = "1.0"
  destinationDirectory = layout.buildDirectory.dir("builtJars")
  archiveClassifier = ""
}