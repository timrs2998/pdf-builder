[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](http://www.gnu.org/licenses/gpl-3.0) [![Build Status](https://travis-ci.org/lamba92/pdf-builder.svg?branch=master)](https://travis-ci.org/lamba92/pdf-builder) [ ![Download](https://api.bintray.com/packages/lamba92/com.github.lamba92/pdf-builder/images/download.svg) ](https://bintray.com/lamba92/com.github.lamba92/pdf-builder/_latestVersion)

# pdf-builder

PDF builder written in [Kotlin](https://kotlinlang.org/) with a [statically typed](https://en.wikipedia.org/wiki/Type_system#Static_type_checking) [DSL](https://en.wikipedia.org/wiki/Domain-specific_language). Inspired by Craig's [document-builder](https://github.com/craigburke/document-builder) library.

## Usage

Include the following in your [build.gradle.kts](https://docs.gradle.org/current/userguide/userguide_single.html):

```kotlin
repositories {
  mavne("https://dl.bintray.com/lamba92/com.github.lamba92")
}

dependencies {
  implementation("com.github.lamba92:pdf-builder:<latest version>")
}
```

**NB**: Be sure to be using Gradle 5 with `enableFeaturePreview("GRADLE_METADATA")` in `settings.gradle.kts` or just use Gradle 6! If you are using a gradle 4 or lower import `com.github.lamba92:pdf-builder-jvm:<latest version>` instead. 

and you can use the library in Kotlin with its DSL:

```kotlin
val pdDocument = document {
  text("Hello")
  text("Hello, color is red!") {
    fontColor = Color(1f, .1f, .1f)
  }
  table {
    row {
      text("r1 c1")
      text("r1 c2")
    }
    row {
      text("r2 c1")
      text("r2 c2")
    }
    border = Border(1f, 2f, 3f, 4f, Color.GREEN, Color.RED, Color.BLUE, Color.BLACK)
  }
}

pdDocument.use { pdDocument ->
  pdDocument.save("output.pdf")
}
```

or Java without a DSL:

```java
Document document = new Document();
TextElement t1 = new TextElement("Hello");
TextElement t2 = new TextElement("Hello, color is red!");
t2.setFontColor(new Color(1f, .1f, .1f));
document.getChildren().add(t1);
document.getChildren().add(t2);
```

## Development Notes

To build from source:

```bash
git clone git@github.com:lamba92/pdf-builder.git
cd pdf-builder/
./gradlew build
```
