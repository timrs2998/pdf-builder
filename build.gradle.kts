@file:Suppress("UNUSED_VARIABLE")

import com.jfrog.bintray.gradle.BintrayExtension
import com.jfrog.bintray.gradle.tasks.BintrayUploadTask
import org.gradle.api.tasks.wrapper.Wrapper.DistributionType.ALL
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

plugins {
  kotlin("multiplatform")
  `maven-publish`
  id("com.jfrog.bintray")
  id("org.jetbrains.dokka")
}

val repoOwner = "timrs2998"
group = "com.github.$repoOwner"
val bintrayRepoName = "maven"
version = `TRAVIS-TAG` ?: "1.5.2"
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

    val jvmMain by getting {
      dependencies {
        implementation(kotlin("stdlib-jdk8"))
        api("org.apache.pdfbox:pdfbox:$apachePdfboxVersion")
      }
    }

    val jvmTest by getting {
      dependencies {
        implementation(kotlin("reflect"))
        implementation(spek("dsl-jvm"))
        implementation(spek("runner-junit5"))
        implementation(jupiter("api"))
        implementation(jupiter("engine"))
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
  wrapper {
    distributionType = ALL
    gradleVersion = "6.2.2"
  }
}

bintray {
  user = searchPropertyOrNull("bintrayUsername", "BINTRAY_USERNAME", "BINTRAY_USER")
  key = searchPropertyOrNull("bintrayApiKey", "BINTRAY_API_KEY", "BINTRAY_KEY")
  pkg {
    version {
      name = project.version as String
    }
    setLabels("pdf", "jvm", "java", "kotlin", "pdf-builder")
    publicDownloadNumbers = false

    repo = bintrayRepoName
    name = rootProject.name
    setLicenses("GPL-3.0")
    vcsUrl = "https://github.com/$repoOwner/${rootProject.name}"
    issueTrackerUrl = "https://github.com/$repoOwner/${rootProject.name}/issues"
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

@Suppress("unused")
fun KotlinDependencyHandler.spek(module: String, version: String = searchProperty("spekVersion")) =
  "org.spekframework.spek2:spek-$module:$version"

@Suppress("unused")
fun KotlinDependencyHandler.jupiter(module: String, version: String = searchProperty("junitJupiterVersion")) =
  "org.junit.jupiter:junit-jupiter-$module:$version"

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

fun searchProperty(name: String, vararg aliases: String) =
  searchPropertyOrNull(name, *aliases) ?: throw IllegalArgumentException(buildString {
    append("No property found with name $name")
    if (aliases.isNotEmpty())
      append(" or with alias: ${aliases.joinToString(", ")}")
  })

@Suppress("PropertyName")
val `TRAVIS-TAG`
  get() = with(System.getenv("TRAVIS_TAG")) {
    if (isNullOrBlank()) null else this
  }
