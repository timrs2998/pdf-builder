package com.github.timrs2998.pdfbuilder.style

import com.github.timrs2998.pdfbuilder.Document
import com.github.timrs2998.pdfbuilder.drawLine
import org.apache.pdfbox.pdmodel.PDDocument
import java.awt.Color

data class Border(
  val top: Float = 0f,
  val right: Float = 0f,
  val bottom: Float = 0f,
  val left: Float = 0f,
  val topColor: Color = Color.BLACK,
  val rightColor: Color = Color.BLACK,
  val bottomColor: Color = Color.BLACK,
  val leftColor: Color = Color.BLACK) {

  companion object {
    @JvmStatic
    val ZERO = Border()

    @JvmStatic
    val HALF = Border(.5f, .5f, .5f, .5f)

    @JvmStatic
    val ONE = Border(1f, 1f, 1f, 1f)
  }

  constructor(
    top: Float = 0f,
    right: Float = 0f,
    bottom: Float = 0f,
    left: Float = 0f,
    color: Color = Color.BLACK) : this(top, right, bottom, left, color, color, color, color)

  constructor(
    width: Float = 0f,
    color: Color = Color.BLACK) : this(width, width, width, width, color)

  /**
   * Given a box defined by the opposite corners (startX, startY) and (endX, endY), draws
   * the surrounding border.
   */
  fun drawBorder(
    document: Document,
    pdDocument: PDDocument,
    startX: Float,
    endX: Float,
    startY: Float,
    endY: Float) {
    drawLine(document, pdDocument, startX, startY, endX, startY, top, topColor)
    drawLine(document, pdDocument, endX, startY, endX, endY, right, rightColor)
    drawLine(document, pdDocument, endX, endY, startX, endY, bottom, bottomColor)
    drawLine(document, pdDocument, startX, endY, startX, startY, left, leftColor)
  }

}
