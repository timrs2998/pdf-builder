package com.github.timrs2998.pdfbuilder

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import java.io.ByteArrayOutputStream

object TableElementSpec: Spek({

  Feature("table") {
    Scenario("document with empty table") {
      val pdDocument = document { table { } }

      Then("should save pdf") {
        ByteArrayOutputStream().use { os ->
          pdDocument.save(os)
        }
      }
    }
  }

})
