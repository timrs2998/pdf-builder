import org.gradle.api.tasks.wrapper.Wrapper.DistributionType.ALL

plugins {
  `maven-publish`
  id("org.jetbrains.kotlin.jvm") version "1.9.10"
  id("org.jetbrains.dokka") version "1.9.10"
}

group = "com.github.timrs2998"
version = "2.0.0"
description = "PDF builder written in Kotlin with a statically typed DSL"

repositories {
  mavenCentral()
}

dependencies {
  implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  api("org.apache.pdfbox:pdfbox:3.0.0")

  val spek_version = "2.0.19"
  testImplementation("org.spekframework.spek2:spek-dsl-jvm:$spek_version")
  testRuntimeOnly("org.spekframework.spek2:spek-runner-junit5:$spek_version")
  testRuntimeOnly("org.jetbrains.kotlin:kotlin-reflect")
}

tasks {
  test {
    useJUnitPlatform {
      includeEngines("spek2")
    }
  }
  wrapper {
    distributionType = ALL
    gradleVersion = "8.4"
  }
}

publishing {
  publications {
    create<MavenPublication>("maven") {
      from(components["java"])
    }
  }
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
