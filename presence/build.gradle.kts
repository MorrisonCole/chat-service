plugins {
    application
    id("com.github.johnrengelman.shadow")
}

application {
    mainClassName = "com.morrisoncole.chat.presence.Main"
}

dependencies {
    compile(project(":protos"))
}
