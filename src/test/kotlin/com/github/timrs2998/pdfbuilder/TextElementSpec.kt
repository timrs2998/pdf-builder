package com.github.timrs2998.pdfbuilder

import com.github.timrs2998.pdfbuilder.style.Wrap
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import java.io.ByteArrayOutputStream

object TextElementSpec: Spek({
  val wrapCharacterDocument = Document()
  wrapCharacterDocument.wrap = Wrap.CHARACTER

  val wrapNoneDocument = Document()
  wrapNoneDocument.wrap = Wrap.NONE

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
      Then("should wrap text on word boundaries on Wrap.WORD") {
        assert(TextElement(Document(), "Hello, world!").wrapText(100f) == listOf("Hello, world!"))
        assert(TextElement(Document(), "Hello, world!").wrapText(50f) == listOf("Hello,", "world!"))
        assert(TextElement(Document(), "Hello,world!").wrapText(50f) == emptyList<String>())
      }
      Then("should wrap text on word boundaries still normally on Wrap.CHARACTER") {
        assert(TextElement(wrapCharacterDocument, "Hello, world!").wrapText(100f) == listOf("Hello, world!"))
        assert(TextElement(wrapCharacterDocument, "Hello, world!").wrapText(50f) == listOf("Hello,", "world!"))
      }
      Then("should wrap text on word boundaries and character boundaries on Wrap.CHARACTER") {
        assert(TextElement(wrapCharacterDocument, "Hello,world!").wrapText(50f) == listOf("Hello,", "world!"))
        assert(
          TextElement(
            wrapCharacterDocument,
            "Hello, world! Hello, world! HelloHello HelloHello HelloHelloHello TestTestTestTestTestTestTest Hello HelloWorld Hello"
          ).wrapText(100f) == listOf(
            "Hello, world! Hello,",
            "world! HelloHello",
            "HelloHello",
            "HelloHelloHello",
            "TestTestTestTe",
            "stTestTestTest Hello",
            "HelloWorld Hello"
          )
        )
        val testString = "Hello".repeat(200)
        val textElements = TextElement(wrapCharacterDocument, testString).wrapText(100f)
        assert(textElements.size == 64)
        assert(textElements.joinToString("").contentEquals(testString))
      }
      Then("should not wrap text on Wrap.NONE") {
        assert(TextElement(wrapNoneDocument, "Hello, world!").wrapText(100f) == listOf("Hello, world!"))
        assert(TextElement(wrapNoneDocument, "Hello, world!").wrapText(50f) == listOf("Hello, world!"))
        assert(TextElement(wrapNoneDocument, "Hello,world!").wrapText(50f) == listOf("Hello,world!"))
      }
    }
  }

})
