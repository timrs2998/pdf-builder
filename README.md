[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](http://www.gnu.org/licenses/gpl-3.0)
[![Build Status](https://travis-ci.org/timrs2998/pdf-builder.svg?branch=master)](https://travis-ci.org/timrs2998/pdf-builder)
[![Download](https://api.bintray.com/packages/timrs2998/maven/pdf-builder/images/download.svg) ](https://bintray.com/timrs2998/maven/pdf-builder/_latestVersion)

# pdf-builder

PDF builder written in Kotlin with a statically typed DSL

## Usage

Include the following in your build.gradle:

```groovy
repositories {
    jcenter()
}

dependencies {
    compile 'com.github.timrs2998:pdf-builder:<latest version>'
}
```

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
git clone git@github.com:timrs2998/pdf-builder.git
cd pdf-builder/
./gradlew build
```

To release a new version, use GitHub to create release tags in "v1.0.0" format.
Travis will build and publish to Bintray provided that BINTRAY_USER and 
BINTRAY_KEY are set.
