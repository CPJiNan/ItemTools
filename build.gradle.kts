import io.izzel.taboolib.gradle.*
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
    `maven-publish`
    id("io.izzel.taboolib") version "2.0.23"
    kotlin("jvm") version "2.1.20"
}

taboolib {
    env {
        install(
            Metrics,
            CommandHelper,
            Bukkit,
            BukkitHook,
            BukkitUtil,
            BukkitNMS,
            BukkitNMSUtil,
            BukkitNMSItemTag
        )
    }
    description {
        contributors { name("CPJiNan") }
    }
    version { taboolib = "6.2.3-8cc2f66" }
    relocate("top.maplex.arim", "com.github.cpjinan.plugin.itemtools.arim")

}

repositories {
    mavenCentral()
}

dependencies {
    // 插件依赖
    compileOnly("ink.ptms.core:v11200:11200")
    compileOnly(kotlin("stdlib"))
    compileOnly(fileTree("libs"))
    taboo("top.maplex.arim:Arim:1.2.14")
}

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_1_8)
        freeCompilerArgs.set(listOf("-Xjvm-default=all", "-Xextended-compiler-checks"))
    }
}