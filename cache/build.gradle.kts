plugins {
    application
    id("com.github.johnrengelman.shadow")
}

application {
    mainClassName = "com.morrisoncole.chat.cache.Main"
}

dependencies {
    compile(project(":protos"))
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
