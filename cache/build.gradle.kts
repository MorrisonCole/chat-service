plugins {
    java
    application
    id("com.github.johnrengelman.shadow")
}

application {
    mainClassName = "com.morrisoncole.chat.cache.Main"
}

dependencies {
    testCompile("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
