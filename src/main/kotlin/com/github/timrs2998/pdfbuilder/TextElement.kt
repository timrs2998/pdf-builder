package com.github.timrs2998.pdfbuilder

import com.github.timrs2998.pdfbuilder.style.Alignment
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode.APPEND

class TextElement(parent: Element, val value: String = "Hello, world!") : Element(parent) {

  override fun instanceHeight(width: Float, startY: Float): Float {
    return fontHeight * wrapText(width).size
  }

  override fun renderInstance(
    pdDocument: PDDocument,
    startX: Float,
    endX: Float,
    startY: Float,
    minHeight: Float) {
    val pdPage = getPage(document, pdDocument, startY)
    wrapText(endX - startX).forEachIndexed { i, line ->
      val startX = when (inheritedHorizontalAlignment) {
        Alignment.LEFT -> startX
        Alignment.RIGHT -> endX - line.width()
        Alignment.CENTER -> startX + (endX - startX - line.width()) / 2f
      }

      PDPageContentStream(pdDocument, pdPage, APPEND, true).use { stream ->
        stream.beginText()
        stream.setFont(inheritedPdFont, inheritedFontSize)
        stream.setNonStrokingColor(inheritedFontColor)
        stream.newLineAtOffset(
          startX,
          (transformY(document, startY) - fontHeight) - fontHeight * i
        )
        stream.showText(line)
        stream.endText()
      }
    }
  }

  fun wrapText(width: Float): List<String> {
    val lines = mutableListOf<String>()
    value.splitToSequence(" ").forEach { word ->
      if (word.width() > width) {
        // skip word for now
//                throw UnsupportedOperationException("TODO: wrap word by character for this case")
      } else if (lines.isEmpty()) {
        lines.add(word)
      } else if ((lines.last() + ' ' + word).width() > width) {
        lines.add(word)
      } else {
        lines[lines.size-1] = lines.last() + ' ' + word
      }
    }
    return lines
  }

  private fun String.width() = inheritedPdFont.getStringWidth(this) * inheritedFontSize / 1000

  private val fontHeight: Float
    get() = inheritedPdFont.boundingBox.height * inheritedFontSize / 1000f

}
