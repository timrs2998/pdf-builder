package timrs2998.pdfbuilder

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import java.io.ByteArrayOutputStream

@RunWith(JUnitPlatform::class)
class TextElementSpec: Spek({

    given("document with text element") {
        val pdDocument = document { text("Hello, world!") }

        it("should save pdf") {
            ByteArrayOutputStream().use { os ->
                pdDocument.save(os)
            }
        }
    }

    it("should wrap text on word boundaries") {
        assert(TextElement(Document(), "Hello, world!").wrapText(100f) == listOf("Hello, world!"))
        assert(TextElement(Document(), "Hello, world!").wrapText(50f) == listOf("Hello,", "world!"))
    }

})
