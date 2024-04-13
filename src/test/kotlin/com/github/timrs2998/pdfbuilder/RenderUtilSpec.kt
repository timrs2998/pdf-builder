package com.github.timrs2998.pdfbuilder

import io.kotest.core.spec.style.FunSpec

class RenderUtilSpec :
    FunSpec({
      test("page index") {
        // expect get index of page #y / #pageHeight
        assert(getPageIndex(100f, 0f) == 0)
        assert(getPageIndex(100f, 99f) == 0)
        assert(getPageIndex(100f, 100f) == 1)
        assert(getPageIndex(100f, 101f) == 1)
        assert(getPageIndex(100f, 199f) == 1)
        assert(getPageIndex(100f, 200f) == 2)
        assert(getPageIndex(100f, 201f) == 2)
      }
    })
