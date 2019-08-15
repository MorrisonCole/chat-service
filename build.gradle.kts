import com.google.protobuf.gradle.*

plugins {
    base
    java
    id("com.google.protobuf") version ("0.8.10")
    id("com.gradle.build-scan") version ("2.4")
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
        classpath("com.github.jengelman.gradle.plugins", "shadow", "5.1.0")
        classpath("com.google.protobuf", "protobuf-gradle-plugin", "0.8.10")
    }
}

dependencies {
    implementation("com.google.protobuf", "protobuf-java", "3.9.1")

    implementation("javax.annotation:javax.annotation-api:1.3.2")

    implementation("io.grpc:grpc-protobuf:1.23.0")
    implementation("io.grpc:grpc-stub:1.23.0")
    implementation("io.grpc:grpc-netty:1.23.0")

    subprojects.forEach { project ->
        archives(project)
    }
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

    plugins(delegateClosureOf<NamedDomainObjectContainer<ExecutableLocator>> {
        this {
            id("grpc") {
                artifact = "io.grpc:protoc-gen-grpc-java:1.23.0"
            }
        }
    })

    generateProtoTasks(delegateClosureOf<ProtobufConfigurator.GenerateProtoTaskCollection> {
        all().forEach {
            it.plugins(delegateClosureOf<NamedDomainObjectContainer<GenerateProtoTask.PluginOptions>> {
                this {
                    id("grpc")
                }
            })
        }
    })
}

buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"
}
