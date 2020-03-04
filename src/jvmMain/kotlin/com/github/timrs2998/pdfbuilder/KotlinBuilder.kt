package com.github.timrs2998.pdfbuilder

import org.apache.pdfbox.pdmodel.PDDocument
import java.awt.image.BufferedImage

/**
 * A DSL for Kotlin, Groovy or Java 8 consumers of this API.
 */

@DslMarker
annotation class DocumentMarker

/**
 * Creates the outermost [Document] [element][Element] representing the pdf, and returns
 * the rendered [PDDocument] that can be [saved][PDDocument.save] to a
 * [java.io.File] or [java.io.OutputStream].
 *
 * @return The rendered [PDDocument].
 */
fun document(init: Document.() -> Unit): PDDocument {
  val document = Document()
  document.init()
  return document.render()
}

// Workaround for Groovy disliking kotlin default parameters
@DocumentMarker
fun Document.text(value: String) = this.text(value) {}

@DocumentMarker
fun Document.text(value: String, init: TextElement.() -> Unit = {}): TextElement {
  val textElement = TextElement(this, value)
  textElement.init()
  this.children.add(textElement)
  return textElement
}

@DocumentMarker
fun Document.image(imagePath: String) = this.image(imagePath) {}

@DocumentMarker
fun Document.image(imagePath: String, init: ImageElement.() -> Unit = {}): ImageElement {
  val imageElement = ImageElement(this, imagePath, null)
  imageElement.init()
  this.children.add(imageElement)
  return imageElement
}

@DocumentMarker
fun Document.image(bufferedImage: BufferedImage): ImageElement  = this.image(bufferedImage) {}

@DocumentMarker
fun Document.image(bufferedImage: BufferedImage, init: ImageElement.() -> Unit = {}): ImageElement {
  val imageElement = ImageElement(this, "", bufferedImage)
  imageElement.init()
  this.children.add(imageElement)
  return imageElement
}

@DocumentMarker
fun Document.table(init: TableElement.() -> Unit): TableElement {
  val tableElement = TableElement(this)
  tableElement.init()
  this.children.add(tableElement)
  return tableElement
}

@DslMarker
annotation class TableMarker

@TableMarker
fun TableElement.header(init: RowElement.() -> Unit): RowElement {
  val rowElement = RowElement(this)
  rowElement.init()
  this.header = rowElement
  return rowElement
}

@TableMarker
fun TableElement.row(init: RowElement.() -> Unit): RowElement {
  val rowElement = RowElement(this)
  rowElement.init()
  this.rows.add(rowElement)
  return rowElement
}

@DslMarker
annotation class RowMarker

// Workaround for Groovy disliking kotlin default parameters
@RowMarker
fun RowElement.text(value: String) = this.text(value) {}

@RowMarker
fun RowElement.text(value: String, init: TextElement.() -> Unit = {}): TextElement {
  val textElement = TextElement(this, value)
  textElement.init()
  this.columns.add(textElement)
  return textElement
}
