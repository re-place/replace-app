import com.google.protobuf.gradle.id

plugins {
    id("kotlin-jvm.base-conventions")
    id("com.google.protobuf") version "0.9.1"
    java
}

protobuf {
    protoc {
        artifact = libs.protoc.get().toString()
    }
}

//val protoGeneratedDir = file("build/generated")
//
//protobuf {
//    protoc {
//        artifact = libs.protoc.get().toString()
//    }
//    plugins {
//        id("pbandk") {
//            artifact = "pro.streem.pbandk:protoc-gen-pbandk-jvm:${libs.versions.pbandk.get()}:jvm8@jar"
//        }
//    }
//    generateProtoTasks {
//        all().forEach { task ->
//            task.builtins.removeIf {
//                it.name == "java"
//            }
//            task.plugins {
//                id("pbandk")
//            }
//        }
//    }
//}
//
//kotlin {
//    sourceSets {
//        val commonMain by getting {
//            kotlin.srcDir(protoGeneratedDir)
//        }
//    }
//}
//
//dependencies {
//    commonMainImplementation(libs.pbandkRuntime)
//}
