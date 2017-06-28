package timrs2998.pdfbuilder

import org.apache.pdfbox.pdmodel.PDDocument

/**
 * A DSL for Kotlin, Groovy or Java 8 consumers of this API.
 */

fun document(init: Document.() -> Unit): PDDocument {
    val document = Document()
    document.init()
    return document.render()
}

// Workaround for Groovy disliking kotlin default parameters
fun Document.text(value: String) = this.text(value, {})

fun Document.text(value: String, init: TextElement.() -> Unit = {}): TextElement {
    val textElement = TextElement(this, value)
    textElement.init()
    this.children.add(textElement)
    return textElement
}

fun Document.table(init: TableElement.() -> Unit): TableElement {
    val tableElement = TableElement(this)
    tableElement.init()
    this.children.add(tableElement)
    return tableElement
}

fun TableElement.header(init: RowElement.() -> Unit): RowElement {
    val rowElement = RowElement(this)
    rowElement.init()
    this.header = rowElement
    return rowElement
}

fun TableElement.row(init: RowElement.() -> Unit): RowElement {
    val rowElement = RowElement(this)
    rowElement.init()
    this.rows.add(rowElement)
    return rowElement
}

// Workaround for Groovy disliking kotlin default parameters
fun RowElement.text(value: String) = this.text(value, {})

fun RowElement.text(value: String, init: TextElement.() -> Unit = {}): TextElement {
    val textElement = TextElement(this, value)
    textElement.init()
    this.columns.add(textElement)
    return textElement
}
