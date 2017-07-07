package com.github.timrs2998.pdfbuilder

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import java.io.ByteArrayOutputStream

@RunWith(JUnitPlatform::class)
class TableElementSpec: Spek({

    given("document with empty table") {
        val pdDocument = document { table { } }

        it("should save pdf") {
            ByteArrayOutputStream().use { os ->
                pdDocument.save(os)
            }
        }
    }

})
