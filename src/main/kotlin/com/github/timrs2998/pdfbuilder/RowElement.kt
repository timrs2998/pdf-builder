package com.github.timrs2998.pdfbuilder

import org.apache.pdfbox.pdmodel.PDDocument

class RowElement(override val parent: TableElement) : Element(parent) {

  val columns = mutableListOf<TextElement>()

  var minHeight = 0f

  var weights = mutableListOf<Float>()

  override fun instanceHeight(width: Float, startY: Float): Float {
    val heights = columns.map { it.height(it.getWidth(width), startY) }
    return (heights.maxOrNull() ?: 0f).coerceAtLeast(minHeight)
  }

  override fun renderInstance(pdDocument: PDDocument, startX: Float, endX: Float, startY: Float, minHeight: Float) {
    val height = height(endX - startX, startY, minHeight)
    columns.fold(startX) { columnStartX, column ->
      val columnWidth = column.getWidth(endX - startX)
      column.render(pdDocument, columnStartX, columnStartX + columnWidth, startY, height)
      columnStartX + columnWidth
    }
  }

  private fun TextElement.getWidth(totalWidth: Float): Float {
    val multiplier = if (weights.isEmpty()) 1f / columns.size else weights[columns.indexOf(this)] / weights.sum()
    return totalWidth * multiplier
  }

}
