plugins {
    java
}

group = "com.morrisoncole"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
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