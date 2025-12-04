# Xplate Website Server
This is a sping boot java server, handling all the traffic for https://xplate.dev, and https://bedrockr.xplate.dev

This server includes 2 spring boot apps, with 1 controller each, that handles simple static web stuff.

## Building
_You need to have Java 25 before you start_

Run `./gradlew clean build shadowjar`, then you can find the jar in `/build/builtJars`
