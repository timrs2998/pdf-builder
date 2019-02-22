package com.github.timrs2998.pdfbuilder

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import java.io.ByteArrayOutputStream

object RowElementSpec: Spek({

  Feature("row") {
    Scenario("document with empty row in table") {
      val pdDocument = document { table { row {} } }

      Then("should save pdf") {
        ByteArrayOutputStream().use { os ->
          pdDocument.save(os)
        }
      }
    }
  }

})
