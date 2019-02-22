package com.github.timrs2998.pdfbuilder.style

data class Padding(
  val top: Float = 0f,
  val right: Float = 0f,
  val bottom: Float = 0f,
  val left: Float = 0f) {

  companion object {
    @JvmStatic
    val ZERO = Padding(0f, 0f, 0f, 0f)
  }

}
