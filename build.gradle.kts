plugins {
    base
    java
    id("com.gradle.build-scan") version ("2.4")
}

allprojects {
    group = "com.morrisoncole"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")
}

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("com.github.jengelman.gradle.plugins", "shadow", "5.1.0")
    }
}

dependencies {
    subprojects.forEach { project ->
        archives(project)
    }
}

buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"
}
