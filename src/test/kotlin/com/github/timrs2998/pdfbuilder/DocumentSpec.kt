package com.github.timrs2998.pdfbuilder

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import java.io.ByteArrayOutputStream

@RunWith(JUnitPlatform::class)
class DocumentSpec: Spek({

    given("new document") {
        val document = com.github.timrs2998.pdfbuilder.Document()

        it("should be empty") {
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

    given("empty document") {
        val document = com.github.timrs2998.pdfbuilder.Document()

        on("render") {
            val pdDocument = document.render()

            it("should save to pdf") {
                ByteArrayOutputStream().use { os ->
                    pdDocument.save(os)
                }
            }
        }
    }

})
