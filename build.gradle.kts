import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    kotlin("jvm") version "1.4.21"
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "6.1.0"

}

group = "net.eduard"
version = "1.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://jitpack.io/")
}
dependencies {
    compileOnly("net.bukkitplugin:jhcash:6.1")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("org.bukkit:spigot:1.8.9")
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
    compileOnly("net.md-5:bungeecord-api:1.16-R0.2-SNAPSHOT")
    api(project(":eduardutils"))
    api(project(":sqlmanager"))
    api("net.eduard.abstraction:complete:1.0-SNAPSHOT")
    compileOnly(kotlin("stdlib"))
    testCompileOnly(kotlin("stdlib"))
    testCompileOnly("junit", "junit", "4.12")
}
tasks.withType<ShadowJar>{
    destinationDir = file("E:\\Tudo\\Minecraft - Server\\Servidor Teste\\plugins\\")
}
tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"

    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}