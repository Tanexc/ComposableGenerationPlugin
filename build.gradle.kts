import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.3.0"
    id("org.jetbrains.intellij.platform") version "2.18.1"
}

group = "com.tanexc"
version = "1.1.0"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        intellijIdea("2024.3.6")

        bundledPlugin("com.intellij.java")
        bundledPlugin("org.jetbrains.kotlin")
        bundledPlugin("org.intellij.plugins.markdown")
    }
}

intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            sinceBuild = "243"
        }
    }

    instrumentCode = false

    pluginVerification {
        ides {
            create(IntelliJPlatformType.AndroidStudio, "2024.3.1.13")
            create(IntelliJPlatformType.AndroidStudio, "2025.1.2.13")
            create(IntelliJPlatformType.AndroidStudio, "2025.2.2.8")
            create(IntelliJPlatformType.AndroidStudio, "2025.3.4.7")
            create(IntelliJPlatformType.AndroidStudio, "2026.1.2.10")
        }
    }

}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "21"
        targetCompatibility = "21"
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
    }
}
