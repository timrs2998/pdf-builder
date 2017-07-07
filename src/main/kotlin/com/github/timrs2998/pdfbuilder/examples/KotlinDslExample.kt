package com.github.timrs2998.pdfbuilder.examples

import com.github.timrs2998.pdfbuilder.document
import com.github.timrs2998.pdfbuilder.row
import com.github.timrs2998.pdfbuilder.style.Border
import com.github.timrs2998.pdfbuilder.table
import com.github.timrs2998.pdfbuilder.text
import java.awt.Color

/**
 * Class containing main method to demonstrate creation of a sample "output.pdf"
 * using a Kotlin-specific DSL. This approach is more declarative, but is less
 * portable between languages.
 */
object KotlinDslExample {

    @JvmStatic
    fun main(args: Array<String>) {
        document {

            text("Hello")

            text("Hello, color is red!") {
                fontColor = Color(1f, .1f, .1f)
            }

            table {
                row {
                    text("r1 c1")
                    text("r1 c2")
                }
                row {
                    text("r2 c1")
                    text("r2 c2")
                }

                border = Border(1f, 2f, 3f, 4f, Color.GREEN, Color.RED, Color.BLUE, Color.BLACK)
            }

        }.use { pdDocument ->
            pdDocument.save("output.pdf")
        }
    }

}
