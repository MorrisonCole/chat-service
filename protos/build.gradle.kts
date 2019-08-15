import com.google.protobuf.gradle.*

plugins {
    id("com.google.protobuf") version ("0.8.10")
}

buildscript {
    dependencies {
        classpath("com.google.protobuf", "protobuf-gradle-plugin", "0.8.10")
    }
}

dependencies {
    implementation("com.google.protobuf", "protobuf-java", "3.9.1")

    implementation("javax.annotation:javax.annotation-api:1.3.2")

    implementation("io.grpc:grpc-protobuf:1.23.0")
    implementation("io.grpc:grpc-stub:1.23.0")
    implementation("io.grpc:grpc-netty:1.23.0")
}

sourceSets {
    main {
        proto {
            srcDir("proto")
        }
        java {
            srcDir("build/generated/source/proto/main")
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

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
