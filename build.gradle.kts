@file:Suppress("UNUSED_VARIABLE")

import com.jfrog.bintray.gradle.BintrayExtension
import com.jfrog.bintray.gradle.tasks.BintrayUploadTask

plugins {
  kotlin("multiplatform")
  `maven-publish`
  id("com.jfrog.bintray")
  id("org.jetbrains.dokka")
}

group = "com.github.lamba92"
version = `travis-tag` ?: "1.0"
description = "PDF builder written in Kotlin with a statically typed DSL"

repositories {
  jcenter()
  mavenCentral()
}

kotlin {

  jvm {
    compilations.all {
      kotlinOptions.jvmTarget = "1.8"
    }
  }

  sourceSets {

    val apachePdfboxVersion: String by project
    val spekVersion: String by project
    val junitJupiterVersion: String by project

    val jvmMain by getting {
      dependencies {
        implementation(kotlin("stdlib-jdk8"))
        api("org.apache.pdfbox:pdfbox:$apachePdfboxVersion")
      }
    }

    val jvmTest by getting {
      dependencies {
        implementation(kotlin("reflect"))
        implementation("org.spekframework.spek2:spek-dsl-jvm:$spekVersion")
        implementation("org.spekframework.spek2:spek-runner-junit5:$spekVersion")
        implementation("org.junit.jupiter:junit-jupiter-api:$junitJupiterVersion")
        implementation("org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion")
      }
    }
  }
}

tasks {
  dokka {
    outputFormat = "html"
    outputDirectory = "$buildDir/javadoc"
    multiplatform {
      val jvmMain by creating {
        targets = listOf("JVM")
        platform = "jvm"
        sourceRoot {
          path = kotlin.sourceSets.getByName("jvmMain").kotlin.srcDirs.first().toString()
        }
      }
    }
  }
  withType<Test> {
    useJUnitPlatform {
      includeEngines("spek2")
    }
  }
}

bintray {
  user = searchPropertyOrNull("bintrayUsername", "BINTRAY_USERNAME")
  key = searchPropertyOrNull("bintrayApiKey", "BINTRAY_API_KEY")
  pkg {
    version {
      name = project.version as String
    }
    repo = project.group as String
    name = rootProject.name
    setLicenses("GNU-GPL3")
    vcsUrl = "https://github.com/lamba92/${rootProject.name}"
    issueTrackerUrl = "https://github.com/lamba92/${rootProject.name}/issues"
  }
  publish = true
  setPublications(*publishing.publications.names.toTypedArray())
}

tasks.withType<BintrayUploadTask> {
  doFirst {
    publishing.publications.withType<MavenPublication> {
      buildDir.resolve("publications/$name/module.json").let {
        if (it.exists())
          artifact(object : org.gradle.api.publish.maven.internal.artifact.FileBasedMavenArtifact(it) {
            override fun getDefaultExtension() = "module"
          })
      }
    }
  }
}

fun BintrayExtension.pkg(action: BintrayExtension.PackageConfig.() -> Unit) {
  pkg(closureOf(action))
}

fun BintrayExtension.PackageConfig.version(action: BintrayExtension.VersionConfig.() -> Unit) {
  version(closureOf(action))
}

fun searchPropertyOrNull(name: String, vararg aliases: String): String? {

  fun searchEverywhere(name: String): String? =
          findProperty(name) as? String? ?: System.getenv(name)

  searchEverywhere(name)?.let { return it }

  with(aliases.iterator()) {
    while (hasNext()) {
      searchEverywhere(next())?.let { return it }
    }
  }

  return null
}

@Suppress("PropertyName")
val `travis-tag`
  get() = System.getenv("TRAVIS_TAG").run {
    if (isNullOrBlank()) null else this
  }
