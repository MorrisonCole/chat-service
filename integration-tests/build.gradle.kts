dependencies {
    testCompile(project(":protos"))

    testCompile("javax.annotation", "javax.annotation-api", "1.3.2")

    testCompile("io.grpc", "grpc-protobuf", "1.23.0")
    testCompile("io.grpc", "grpc-stub", "1.23.0")
    testCompile("io.grpc", "grpc-netty", "1.23.0")

    testCompile("org.junit.jupiter", "junit-jupiter-api", "5.5.1")
    testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine", "5.5.1")
    testRuntimeOnly("org.slf4j", "slf4j-simple", "1.7.28")
    testCompile("org.testcontainers", "testcontainers", "1.12.0")
    testCompile("org.testcontainers", "junit-jupiter", "1.12.0")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks {
    test {
        dependsOn(":cache:shadowJar")
        dependsOn(":login:shadowJar")
        dependsOn(":presence:shadowJar")

        useJUnitPlatform()
    }
}