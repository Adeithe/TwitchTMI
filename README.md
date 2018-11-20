# TwitchTMI [![](https://jitpack.io/v/Adeithe/TwitchTMI.svg?style=flat-square)](https://jitpack.io/#Adeithe/TwitchTMI)

SSL Implementation of the Twitch IRC and API Services in Java

Supports BetterTTV and FrankerFaceZ

## Getting Started
All examples of how to get started with TwitchTMI are available at [/src/test/java](https://github.com/Adeithe/TwitchTMI/tree/master/src/test/java)

## Documentation
Documentation for TwitchTMI is available [HERE](https://jitpack.io/com/github/Adeithe/TwitchTMI/master-SNAPSHOT/javadoc)

## Using TwitchTMI in your project
`@VERSION@` = The release version of TwitchTMI to use or `-SNAPSHOT` to use the dev version
##### With Maven
Add the following to your `pom.xml` (Without ellipses)
```xml
...
<repositories>
    ...
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
...
<dependencies>
    ...
    <dependency>
        <groupId>com.github.Adeithe</groupId>
        <artifactId>TwitchTMI</artifactId>
        <version>@VERSION@</version>
    </dependency>
</dependencies>
...
```
##### With Gradle
Add the following to your `build.gradle` (Without ellipses)
```groovy
allprojects {
    ...
    repositories {
        ...
        maven { url  "https://jitpack.io" }
    }
}
...
dependencies {
  ...
  compile "com.github.Adeithe:TwitchTMI:@VERSION@"
}
...
```

## Development
TwitchTMI is still in development. Many features may be broken and methods may disappear/change at any point without warning.