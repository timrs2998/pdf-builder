package com.github.timrs2998.pdfbuilder

import com.github.timrs2998.pdfbuilder.style.Orientation
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.common.PDRectangle

/**
 * Represents a pdf document. The document is the outer container for all
 * elements belonging to a pdf. Once rendered, it cannot be modified.
 */
class Document : Element(null) {

  val children = mutableListOf<Element>()

  var footerFactory: (page: Int, totalPages: Int) -> Element? = { page, totalPages -> null }

  var orientation: Orientation = Orientation.PORTRAIT

  var pdRectangle: PDRectangle = PDRectangle.A4

  val pageWidth: Float
    get() = when (orientation) {
      Orientation.LANDSCAPE -> pdRectangle.height
      Orientation.PORTRAIT -> pdRectangle.width
    }

  val pageHeight: Float
    get() = when (orientation) {
      Orientation.LANDSCAPE -> pdRectangle.width
      Orientation.PORTRAIT -> pdRectangle.height
    }


  override fun instanceHeight(width: Float, startY: Float): Float {
    return children.fold(0.0f) { sum, row -> sum + row.height(width, startY) }
  }

  /**
   * Forces one-time evaluation of lazy properties and renders object model to
   * a [PDDocument]. Once called, document can no longer be modified.
   */
  fun render(): PDDocument {
    val pdDocument = PDDocument()
    render(pdDocument, 0f, pageWidth, 0f, 0f)
    return pdDocument
  }

  override fun renderInstance(
    pdDocument: PDDocument,
    startX: Float,
    endX: Float,
    startY: Float,
    minHeight: Float) {
    children.fold(startY) { startY, child ->
      // Handle paging unless child is table (tables handle page conflicts by row)
      val adjustedStartY = when (child) {
        is TableElement -> startY
        else -> adjustStartYForPaging(startY, startY + child.height(endX - startX, startY))
      }
      child.render(pdDocument, startX, endX, adjustedStartY)
      adjustedStartY + child.height(endX - startX, adjustedStartY)
    }

    /*
     * TODO: render footer on each page
     *  - use footerFactory(page, totalPages) to construct footers
     *  - footers shouldn't impact number of pages
     *    - need to consume bottom of page or render inside margins?
     */
  }

  /**
   * If startY and endY cross a page (or page margin) boundary, returns a new startY for rendering. Otherwise
   * returns the original startY.
   *
   * TODO: also consider padding
   */
  fun adjustStartYForPaging(startY: Float, endY: Float): Float {
    fun getYOffsetForPage(y: Float): Float {
      var y = y
      while (y >= document.pageHeight) {
        y -= document.pageHeight
      }
      return y
    }

    fun Float.insideBottomMargin() = getYOffsetForPage(this) > document.pageHeight - margin.top

    if (startY.insideBottomMargin()
      || endY.insideBottomMargin()
      || getPageIndex(pageHeight, startY) != getPageIndex(pageHeight, endY)) {
      return (getPageIndex(pageHeight, startY) +1) * pageHeight + margin.top
    }
    return startY
  }

}
