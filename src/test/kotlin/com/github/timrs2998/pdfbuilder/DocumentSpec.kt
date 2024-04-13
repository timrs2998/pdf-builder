package com.github.timrs2998.pdfbuilder

import io.kotest.core.spec.style.FunSpec
import java.io.ByteArrayOutputStream
import org.apache.pdfbox.pdmodel.PDDocument

class DocumentSpec :
    FunSpec({
      test("new document") {
        val document = Document()

        assert(document.children.isEmpty())
        assert(document.backgroundColor == null)
        assert(document.fontColor == null)
        assert(document.fontSize == null)
        assert(document.horizontalAlignment == null)
        assert(document.fontName == null)
        document.inheritedBackgroundColor
        document.inheritedFontColor
        document.inheritedFontSize
        document.inheritedHorizontalAlignment
        document.inheritedPdFont
      }

      test("empty document") {
        val document = Document()
        lateinit var pdDocument: PDDocument

        // when render
        pdDocument = document.render()

        // then should save to pdf
        ByteArrayOutputStream().use { os -> pdDocument.save(os) }
      }
    })
