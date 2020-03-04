package com.github.timrs2998.pdfbuilder

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

object RenderUtilSpec: Spek({

  Feature("render util") {
    Scenario("page index") {
      Then("get index of page #y / #pageHeight") {
        assert(getPageIndex(100f, 0f) == 0)
        assert(getPageIndex(100f, 99f) == 0)
        assert(getPageIndex(100f, 100f) == 1)
        assert(getPageIndex(100f, 101f) == 1)
        assert(getPageIndex(100f, 199f) == 1)
        assert(getPageIndex(100f, 200f) == 2)
        assert(getPageIndex(100f, 201f) == 2)
      }
    }
  }

})
