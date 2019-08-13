import com.google.protobuf.gradle.*

plugins {
    base
    java
    id("com.google.protobuf") version("0.8.10")
}

allprojects {
    group = "com.morrisoncole"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("com.google.protobuf", "protobuf-gradle-plugin", "0.8.10")
    }
}

dependencies {
    implementation("com.google.protobuf", "protobuf-java", "3.9.1")

    subprojects.forEach { project ->
        archives(project)
    }
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

sourceSets {
    main {
        proto {
            srcDir("proto")
        }
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.9.1"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.22.1"
        }
    }
}
