import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    id("java")
    id("java-library")
    id("com.github.johnrengelman.shadow") version("6.1.0")
    id("org.checkerframework") version("0.5.16")
}

group = "dev.kscott"
version = "1.0.0"

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_14
    targetCompatibility = sourceCompatibility
}

repositories {
    mavenCentral()
    maven {
        name = "papermc-repo"
        url = uri("https://papermc.io/repo/repository/maven-public/")
    }
    maven {
        name = "sonatype"
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
}

dependencies {
    compileOnlyApi("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")

    implementation("net.kyori:adventure-api:4.3.0")
    implementation("net.kyori:adventure-platform-bukkit:4.0.0-SNAPSHOT") {
        exclude("net.kyori", "adventure-api") // Duh
    }
    implementation("net.kyori:adventure-text-minimessage:4.0.0-SNAPSHOT") {
        exclude("net.kyori", "adventure-api") // Duh
    }

    implementation("com.google.inject:guice:5.0.0-BETA-1")
    implementation("com.google.inject.extensions:guice-assistedinject:5.0.0-BETA-1") {
        exclude("com.google.inject", "guice") // Duh
    }

}

tasks {
    build {
        dependsOn(shadowJar)
    }

    shadowJar {
        fun relocates(vararg dependencies: String) {
            dependencies.forEach {
                val split = it.split('.')
                val name = split.last()
                relocate(it, "${rootProject.group}.dependencies.$name")
            }
        }

        archiveFileName.set("Abridged-${archiveVersion.get()}.jar")

        dependencies {
            exclude(dependency("com.google.guava:"))
            exclude(dependency("com.google.errorprone:"))
        }

        relocates(
                "com.google.inject"
        )
    }

    processResources {
        filter<ReplaceTokens>("tokens" to mapOf("version" to project.version))
    }
}
