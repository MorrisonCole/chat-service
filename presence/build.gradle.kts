plugins {
    application
    id("com.github.johnrengelman.shadow")
}

application {
    mainClassName = "com.morrisoncole.chat.presence.Main"
}

dependencies {
    compile(project(":protos"))
    compile(project(":common"))

    compile("io.grpc", "grpc-protobuf", "1.23.0")
    compile("io.grpc", "grpc-stub", "1.23.0")
    compile("io.grpc", "grpc-netty", "1.23.0")
}
