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
