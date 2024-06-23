import org.gradle.api.tasks.wrapper.Wrapper.DistributionType.ALL

plugins {
  // ./gradlew dependencyUpdates -Drevision=release
  id("com.github.ben-manes.versions") version Versions.benManesVersionPlugin
  `maven-publish`
  id("org.jetbrains.kotlin.jvm") version Versions.kotlin
  id("org.jetbrains.dokka") version Versions.dokka
  id("com.diffplug.spotless") version Versions.spotlessGradle
}

group = "com.github.timrs2998"

version = "2.0.1"

description = "PDF builder written in Kotlin with a statically typed DSL"

repositories { mavenCentral() }

dependencies {
  implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  api("org.apache.pdfbox:pdfbox:${Versions.pdfbox}")

  testRuntimeOnly("org.jetbrains.kotlin:kotlin-reflect")

  testImplementation("io.kotest:kotest-runner-junit5:${Versions.kotest}")
  testImplementation("io.kotest:kotest-assertions-core:${Versions.kotest}")
  testImplementation("io.kotest:kotest-property:${Versions.kotest}")
}

spotless {
  kotlin { ktfmt(Versions.ktfmt) }
  kotlinGradle { ktfmt(Versions.ktfmt) }
}

tasks {
  test { useJUnitPlatform() }
  wrapper {
    distributionType = ALL
    gradleVersion = Versions.gradle
  }
}

publishing {
  publications { create<MavenPublication>("maven") { from(components["java"]) } }
  repositories {
    maven {
      name = "GitHubPackages"
      url = uri("https://maven.pkg.github.com/timrs2998/pdf-builder")
      credentials {
        username = System.getenv("GITHUB_ACTOR")
        password = System.getenv("GITHUB_TOKEN")
      }
    }
  }
}
