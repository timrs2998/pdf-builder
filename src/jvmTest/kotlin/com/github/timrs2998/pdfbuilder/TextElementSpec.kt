package com.github.timrs2998.pdfbuilder

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import java.io.ByteArrayOutputStream

object TextElementSpec: Spek({

  Feature("text element") {

    Scenario("document with text element") {
      val pdDocument = document { text("Hello, world!") }

      Then("should save pdf") {
        ByteArrayOutputStream().use { os ->
          pdDocument.save(os)
        }
      }
    }

    Scenario("word boundaries") {
      Then("should wrap text on word boundaries") {
        assert(TextElement(Document(), "Hello, world!").wrapText(100f) == listOf("Hello, world!"))
        assert(TextElement(Document(), "Hello, world!").wrapText(50f) == listOf("Hello,", "world!"))
      }
    }
  }

})
