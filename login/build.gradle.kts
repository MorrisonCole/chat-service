plugins {
    application
    id("com.github.johnrengelman.shadow")
}

application {
    mainClassName = "com.morrisoncole.chat.login.Main"
}

dependencies {
    compile(project(":protos"))

    compile("com.google.cloud", "google-cloud-datastore", "1.87.0")

    compile("javax.annotation", "javax.annotation-api", "1.3.2")

    compile("io.grpc", "grpc-protobuf", "1.23.0")
    compile("io.grpc", "grpc-stub", "1.23.0")
    compile("io.grpc", "grpc-netty", "1.23.0")

    testCompile("org.junit.jupiter", "junit-jupiter-api", "5.5.1")
    testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine", "5.5.1")
    testRuntimeOnly("org.slf4j", "slf4j-simple", "1.7.28")
    testCompile("org.testcontainers", "testcontainers", "1.12.0")
    testCompile("org.testcontainers", "junit-jupiter", "1.12.0")
}

tasks {
    test {
        dependsOn(":login:shadowJar")

        useJUnitPlatform()
    }
}