package com.github.timrs2998.pdfbuilder

import org.apache.pdfbox.pdmodel.PDDocument
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import java.io.ByteArrayOutputStream

object DocumentSpec: Spek({

  Feature("Document") {

    Scenario("new document") {
      val document = Document()

      Then("should be empty") {
        assert(document.children.isEmpty())
        assert(document.backgroundColor == null)
        assert(document.fontColor == null)
        assert(document.fontSize == null)
        assert(document.horizontalAlignment == null)
        assert(document.pdFont == null)
        document.inheritedBackgroundColor
        document.inheritedFontColor
        document.inheritedFontSize
        document.inheritedHorizontalAlignment
        document.inheritedPdFont
      }
    }

    Scenario("empty document") {
      val document = Document()
      lateinit var pdDocument: PDDocument

      When("render") {
        pdDocument = document.render()
      }

      Then("should save to pdf") {
        ByteArrayOutputStream().use { os ->
          pdDocument.save(os)
        }
      }
    }
  }

})
