package org.cmhh

import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.io.File

case class Image(data: Array[Byte], rows: Int, cols: Int, byRow: Boolean) {
  require(data.size == rows * cols, "Invalid arguments.")

  def apply(row: Int, col: Int): Int = {
    if (row < 0 | row >= rows | col < 0 | col >= cols) 
      sys.error("Index out of bounds.")
    
    if (byRow) 
      (data(rows * row + col) & 0xff).toInt
    else 
      (data(cols * col + row) & 0xff).toInt
  }

  def toImage: BufferedImage = {
    val im = new BufferedImage(cols, rows, BufferedImage.TYPE_BYTE_GRAY)
    for {
      i <- 0 until rows
      j <- 0 until cols
    } {
      im.getRaster().setPixel(j, i, Array(apply(i, j)))
    }
    im
  }

  def save(file: String): Unit = {
    ImageIO.write(toImage, "PNG", new File(file))
  }
}