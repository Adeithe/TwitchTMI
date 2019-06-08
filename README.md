# TwitchTMI [![](https://repo.adeithe.dev/jenkins/badge?job=TwitchTMI&style=flat-square)](https://ci.adeithe.dev/job/TwitchTMI)

SSL Implementation of the Twitch IRC and API Services in Java

Supports BetterTTV and FrankerFaceZ

## Getting Started
All examples of how to get started with TwitchTMI are available at [/src/test/java](https://github.com/Adeithe/TwitchTMI/tree/master/src/test/java)

## Documentation
Documentation for TwitchTMI is available [HERE](https://jitpack.io/com/github/Adeithe/TwitchTMI/master-SNAPSHOT/javadoc)

## Using TwitchTMI in your project
`@VERSION@` = The release version of TwitchTMI to use or check `pom.xml` for the dev version
##### With Maven
Add the following to your `pom.xml`
```xml
<project>
    <repositories>
        <repository>
            <id>Adeithe</id>
            <url>https://repo.adeithe.dev/maven/</url>
        </repository>
    </repositories>
    
    <dependencies>
        <dependency>
            <groupId>com.github.Adeithe</groupId>
            <artifactId>TwitchTMI</artifactId>
            <version>@VERSION@</version>
        </dependency>
    </dependencies>
</project>
```
##### With Gradle
Add the following to your `build.gradle`
```groovy
allprojects {
    repositories {
        maven { url  "https://repo.adeithe.dev/maven/" }
    }
}

dependencies {
  compile "com.github.Adeithe:TwitchTMI:@VERSION@"
}
```

## Development
TwitchTMI is still in development. Many features may be broken and methods may disappear/change at any point without warning.
