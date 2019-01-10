package com.yf.classes

import java.io.File
import javax.imageio.ImageIO

object Utils {


  //得到图片比例
  def getFold(path:String) : Float = {
    val image = ImageIO.read(new File(path))
    val imageWidth = image.getWidth.toFloat
    val imageHeight = image.getHeight.toFloat
    val fold = imageHeight/imageWidth
    fold
  }

  def getWidth(path : String) : Float = {
    val image = ImageIO.read(new File(path))
    val imageWidth = image.getWidth.toFloat
    imageWidth
  }

  
  def getLines(text:String) : Double = {
    var size = 0.0
    val pa = "^[a-zA-Z0-9-]*$".r
    text.foreach { z =>
      //计算每行的字符数
      if (pa.findFirstIn(z.toString).isDefined) {
        size = size + 0.5
      } else {
        size = size + 1.0
      }
    }
    size
  }

  def random: String = {
    val source = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890"
    var value = ""
    for (i <- 0 to 20) {
      val ran = Math.random() * 62
      val char = source.charAt(ran.toInt)
      value += char
    }
    value
  }
}
