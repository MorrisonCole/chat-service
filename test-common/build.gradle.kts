plugins {
    id("com.github.johnrengelman.shadow")
}

dependencies {
    compile(project(":protos"))
    compile(project(":login"))

    compile("com.google.cloud", "google-cloud-datastore", "1.87.0")

    compile("javax.annotation", "javax.annotation-api", "1.3.2")

    compile("io.grpc", "grpc-protobuf", "1.23.0")
    compile("io.grpc", "grpc-stub", "1.23.0")
    compile("io.grpc", "grpc-netty", "1.23.0")

    compile("org.junit.jupiter", "junit-jupiter-api", "5.5.1")
    runtimeOnly("org.junit.jupiter", "junit-jupiter-engine", "5.5.1")
    runtimeOnly("org.slf4j", "slf4j-simple", "1.7.28")
    compile("org.testcontainers", "testcontainers", "1.12.0")
    compile("org.testcontainers", "junit-jupiter", "1.12.0")
}
