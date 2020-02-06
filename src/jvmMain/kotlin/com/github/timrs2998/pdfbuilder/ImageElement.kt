package com.github.timrs2998.pdfbuilder

import com.github.timrs2998.pdfbuilder.style.Alignment
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode.APPEND
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import java.awt.image.BufferedImage

class ImageElement(parent: Element, private val imagePath: String = "", private val bufferedImage: BufferedImage? = null) :
  Element(parent) {

  var imgHeight: Int? = null
  var imgWidth: Int? = null

  override fun instanceHeight(width: Float, startY: Float): Float {
    return imgHeight?.toFloat() ?: 0F
  }

  override fun renderInstance(
    pdDocument: PDDocument,
    startX: Float,
    endX: Float,
    startY: Float,
    minHeight: Float
  ) {

    val pdImage = if (bufferedImage != null) JPEGFactory.createFromImage(
      pdDocument, bufferedImage )
    else PDImageXObject.createFromFile(imagePath, pdDocument)

    imgHeight = imgHeight ?: pdImage.height
    imgWidth = imgWidth ?: pdImage.width

    val pdPage = getPage(document, pdDocument, startY + imgHeight!!)

    val realStartX = when (inheritedHorizontalAlignment) {
      Alignment.LEFT -> startX
      Alignment.RIGHT -> endX - imgWidth!!
      Alignment.CENTER -> startX + (endX - startX - imgWidth!!) / 2f
    }
    val transformedY = transformY(document, startY) - imgHeight!!
    PDPageContentStream(pdDocument, pdPage, APPEND, true).use { stream ->
      stream.drawImage(
        pdImage,
        realStartX,
        transformedY,
        imgWidth!!.toFloat(),
        imgHeight!!.toFloat()
      )
    }
  }
}
