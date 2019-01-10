package com.yf.classes

import java.io.File

import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.colors.DeviceRgb
import com.itextpdf.kernel.geom.Rectangle
import com.itextpdf.kernel.pdf.canvas.PdfCanvas
import com.itextpdf.kernel.pdf.{PdfDocument, PdfReader, PdfWriter}
import com.itextpdf.kernel.utils.PdfMerger

object Itext7 {


  //合并pdf
  def mergePdfFiles(files: Array[File], savepath: String): Unit = {
    try{
      val pdfDoc = new PdfDocument(new PdfWriter(savepath))
      val merger = new PdfMerger(pdfDoc)

      for(i <- files.indices){
        val pdf = new PdfDocument(new PdfReader(files(i)))
        merger.merge(pdf,1,pdf.getNumberOfPages)
        pdf.close()
      }

      merger.close()
      pdfDoc.close()
    }catch {
      case e: Exception =>
        e.printStackTrace()
    }
  }

  //无背景色矩形框
  def addRectangleWithoutBackgroundColor(canvas: PdfCanvas, x: Float, y: Float, w: Float, h: Float, width: Float, color: DeviceRgb) = {
    canvas.setStrokeColor(color)
    canvas.setLineDash(1f)
    canvas.setLineWidth(width)
    canvas.rectangle(x, y, w, h)
    canvas.stroke()
  }

  //有背景色圆角矩形框
  def addRoundRectangleWithBackgroundColor(canvas: PdfCanvas, x: Float, y: Float, w: Float, h: Float,r: Float, color: DeviceRgb) = {
    canvas.roundRectangle(x,y,w,h,r)
    canvas.setFillColor(color)
    canvas.setStrokeColor(color)
    canvas.fillStroke()
  }

  //虚线
  def addDashLine(canvas: PdfCanvas, headX: Float, lastX: Float, y: Float, color: DeviceRgb): Unit = {
    canvas.setStrokeColor(color)
    canvas.setLineDash(2f, 0.1f)
    canvas.setLineWidth(0.3f)
    canvas.moveTo(headX, y)
    canvas.lineTo(lastX, y)
    canvas.stroke()
  }

  //有背景色矩形
  def addRectangleWithBackgroundColor(canvas: PdfCanvas, x: Float, y: Float, w: Float, h: Float, color: DeviceRgb) = {
    canvas.rectangle(x, y, w, h)
    canvas.setFillColor(color)
    canvas.setStrokeColor(color)
    canvas.fillStroke()
  }

  //图片
  def addImageToPdf(canvas: PdfCanvas, picturePath: String, x: Float, y: Float, width: Float): Unit = {
    val image = ImageDataFactory.create(picturePath)
    canvas.addImage(image,x,y,width,true)
    canvas.stroke()
  }

  //图片
  def addImageByRectToPdf(canvas: PdfCanvas, picturePath: String, x: Float, y: Float, width: Float,height:Float): Unit = {
    val image = ImageDataFactory.create(picturePath)
    val rect= new Rectangle(x,y,width,height)
    canvas.addImage(image,rect,true)
    canvas.stroke()
  }

}
