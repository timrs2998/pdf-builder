package com.github.timrs2998.pdfbuilder

import io.kotest.core.spec.style.FunSpec
import java.io.ByteArrayOutputStream

class RowElementSpec :
    FunSpec({
      test("document with empty row in table") {
        val pdDocument = document { table { row {} } }

        // then: should save pdf
        ByteArrayOutputStream().use { os -> pdDocument.save(os) }
      }
    })
