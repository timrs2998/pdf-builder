package com.github.timrs2998.pdfbuilder

import com.github.timrs2998.pdfbuilder.style.Alignment
import com.github.timrs2998.pdfbuilder.style.Border
import com.github.timrs2998.pdfbuilder.style.Margin
import com.github.timrs2998.pdfbuilder.style.Padding
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.font.PDType1Font
import java.awt.Color

/**
 * Everything belonging to a [Document] extends from [Element], including
 * [Document] itself. An [Element] knows its [height][Element.height] and how
 * to [render][Element.render] itself.
 *
 * Unless overridden, the default implementations of [render] and [height]
 * assume that the [element][Element] will not span multiple pages and must fit
 * on a single page. The [TableElement] is an example of an [element][Element]
 * that overrides these methods to span multiple pages.
 *
 * An [Element] can be styled similar to CSS, being assigned a [border],
 * [margin], [padding], [backgroundColor], [fontColor], [fontSize],
 * [horizontalAlignment], and [pdFont].
 *
 * Most styles will "cascade" or be inherited by children. The only styles
 * that are not inherited are [border], [margin], and [padding].
 */
abstract class Element(open val parent: Element?) {

  companion object {
    @JvmStatic
    private val FALLBACK_BACKGROUND_COLOR = Color.WHITE

    @JvmStatic
    private val FALLBACK_FONT_COLOR = Color.BLACK

    @JvmStatic
    private val FALLBACK_FONT_SIZE = 12f

    @JvmStatic
    private val FALLBACK_HORIZONTAL_ALIGNMENT = Alignment.LEFT

    @JvmStatic
    private val FALLBACK_PD_FONT: PDFont = PDType1Font.TIMES_ROMAN
  }

  // Properties that are not inherited
  var border = Border.ZERO
  var margin = Margin.ZERO
  var padding = Padding.ZERO

  // Properties that become inherited by children unless overridden or null
  var backgroundColor: Color? = null
  var fontColor: Color? = null
  var fontSize: Float? = null
  var horizontalAlignment: Alignment? = null
  var pdFont: PDFont? = null

  /******************************************************************************************************************/
  /** Properties lazily inherited during rendering ******************************************************************/
  /******************************************************************************************************************/

  protected val document: Document by lazy {
    if (this is Document) {
      this
    } else {
      parent!!.document
    }
  }
  internal val inheritedBackgroundColor: Color by lazy {
    backgroundColor ?: parent?.inheritedBackgroundColor ?: FALLBACK_BACKGROUND_COLOR
  }

  internal val inheritedFontColor: Color by lazy {
    fontColor ?: parent?.inheritedFontColor ?: FALLBACK_FONT_COLOR
  }

  internal val inheritedFontSize: Float by lazy {
    fontSize ?: parent?.inheritedFontSize ?: FALLBACK_FONT_SIZE
  }

  internal val inheritedHorizontalAlignment: Alignment by lazy {
    horizontalAlignment ?: parent?.inheritedHorizontalAlignment ?: FALLBACK_HORIZONTAL_ALIGNMENT
  }

  internal val inheritedPdFont: PDFont by lazy {
    pdFont ?: parent?.inheritedPdFont ?: FALLBACK_PD_FONT
  }

  /******************************************************************************************************************/
  /** Methods needed for rendering **********************************************************************************/
  /******************************************************************************************************************/

  private var cachedInstanceHeightStartY: Float? = null
  private var cachedInstanceHeight: Float? = null

  /**
   * Determines the height considered by this element, including margins and padding.
   */
  fun height(width: Float, startY: Float, minHeight: Float = 0f): Float {
    if (cachedInstanceHeight == null || cachedInstanceHeightStartY != startY) {
      cachedInstanceHeight = instanceHeight(
        width = width - margin.left - margin.right - padding.left - padding.right,
        startY = startY + margin.top + padding.top
      )
      cachedInstanceHeightStartY = startY
    }
    return minHeight.coerceAtLeast(cachedInstanceHeight!!) + margin.top + margin.bottom + padding.top + padding.bottom
  }

  /**
   * Determines the innermost height of the element, excluding margins and padding.
   */
  abstract fun instanceHeight(width: Float, startY: Float): Float

  /**
   * Renders the entire element including margins, padding, borders, and background. Unless
   * overridden, assumes the element being rendered will fit on the page. Multi-page elements
   * (ie: [TableElement]) must handle paging themselves.
   */
  open fun render(
    pdDocument: PDDocument,
    startX: Float,
    endX: Float,
    startY: Float,
    minHeight: Float = 0f) {
    renderInstance(
      pdDocument,
      startX = startX + margin.left + padding.left,
      endX = endX - margin.right - padding.right,
      startY = startY + margin.top + padding.top,
      minHeight = minHeight
    )

    val height = instanceHeight(
      width = endX - startX - margin.left - margin.right - padding.left - padding.right,
      startY = startY + margin.top + padding.top
    )

    border.drawBorder(
      document,
      pdDocument,
      startX = startX + margin.left,
      endX = endX - margin.right,
      startY = startY + margin.top,
      endY = startY + margin.top + minHeight.coerceAtLeast(padding.top + height + padding.bottom)
    )
    drawBox(
      document,
      pdDocument,
      startX = startX + margin.left,
      endX = endX - margin.right,
      startY = startY + margin.top,
      endY = startY + margin.top + minHeight.coerceAtLeast(padding.top + height + padding.bottom),
      color = inheritedBackgroundColor
    )
  }

  /**
   * Renders the innermost element, excluding margins, padding, borders, and background.
   */
  abstract fun renderInstance(
    pdDocument: PDDocument,
    startX: Float,
    endX: Float,
    startY: Float,
    minHeight: Float = 0f)

}
