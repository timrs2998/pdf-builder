package com.github.timrs2998.pdfbuilder

import com.github.timrs2998.pdfbuilder.style.Alignment
import com.github.timrs2998.pdfbuilder.style.Wrap
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
    when (inheritedWrap) {
      Wrap.NONE -> return listOf(value)

      // If wrap is WORD or CHARACTER split text by space
      else -> {
        val lines = mutableListOf<String>()
        value.splitToSequence(" ").forEach { word ->
          if (word.width() > width) {
            // If wrap is CHARACTER and text split into words is still too big, split text in half
            if (inheritedWrap == Wrap.CHARACTER) {
              lines.addAll(word.splitWord(width))
            }
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
    }


  }

  private fun String.splitWord(maxWidth: Float): List<String> {
    val splitWords = mutableListOf<String>()

    // split word in half
    this.chunked((this.length + 1) / 2).forEach{
      if (it.width() > maxWidth) {
        splitWords.addAll(it.splitWord(maxWidth))
      } else {
        splitWords.add(it)
      }
    }

    return splitWords
  }

  private fun String.width() = inheritedPdFont.getStringWidth(this) * inheritedFontSize / 1000

  private val fontHeight: Float
    get() = inheritedPdFont.boundingBox.height * inheritedFontSize / 1000f

}
