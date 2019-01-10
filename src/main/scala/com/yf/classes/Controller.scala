package com.yf.classes

import java.io.{File, FileFilter}

import com.itextpdf.io.font.PdfEncodings
import com.itextpdf.kernel.colors.DeviceRgb
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.pdf.canvas.PdfCanvas
import com.itextpdf.kernel.pdf.{PdfDocument, PdfWriter}
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.property.TextAlignment
import org.apache.commons.io.FileUtils

import scala.collection.JavaConverters._
import scala.util.control.Breaks.{break, breakable}

object Controller {


  val dull_red = new DeviceRgb(143, 41, 54) //暗红色
  val dull_red_light = new DeviceRgb(146, 41, 55) //淡红色
  val dull_red_shade = new DeviceRgb(228, 201, 205) //淡红色
  val navy_blue = new DeviceRgb(13, 21, 48) //藏青色
  val black = new DeviceRgb(34, 24, 21) //黑色

  val width = 481.5f
  val left = 57.0625f


  def createPdf(input: String, output: String): Unit = {
    val path = this.getClass.getProtectionDomain.getCodeSource.getLocation.toURI.getPath
    val p = path.split("/").map(_.trim).dropRight(1).mkString("/")

/*    val resPath = {
      if (new File("C:/").exists()) p.drop(1) else p
    }*/

      val resPath = "D:\\英莱盾祖源报告资料\\36STR_ZY_Report12.25\\36STR_ZY_report\\bin"

    val buffer = FileUtils.readLines(new File(resPath + "/resource/configure/YToCode.txt"), "UTF-8").asScala
    val codeMap = buffer.map(_.split("\t")).map(x => (x.head, x.last)).toMap

    if(!new File(output).exists()){new File(output).mkdirs()}

    val data = FileUtils.readLines(new File(input)).asScala

    data.foreach{x=>
      val datas = x.split("\t")
      val filename = datas.head + ".pdf"
      val tmp = resPath + "/resource/tmp/" + Utils.random
      new File(tmp).mkdir()

      try {

        val code = codeMap(datas(3))

        val information = Array(datas(1), datas(2), datas(3))


        FileUtils.copyDirectory(new File(resPath + "/resource/model"), new File(tmp))

        val width = addPage6ToPdf(resPath + "/resource/model/6.pdf", tmp + "/6.pdf", resPath, code, information)

        val text = addPage7ToPdf(resPath + "/resource/model/7.pdf", tmp + "/7.pdf", resPath, code, width)

        addPage8ToPdf(resPath + "/resource/model/8.pdf", tmp + "/8.pdf", resPath, code, text)

        addPage9ToPdf(resPath + "/resource/model/9.pdf", tmp + "/9.pdf", resPath, datas.drop(4))

        Itext7.mergePdfFiles(new File(tmp).listFiles().sortBy(_.getName.split('.').head.toInt), output + "/" + datas(0) + ".pdf")

        println(filename + " 36STR祖源报告生成成功！")
      }catch {
        case e : Exception => println("[error] " + datas.head+".pdf 36STR祖源报告生成失败！")
      }
      FileUtils.deleteDirectory(new File(tmp))
    }


  }


  def addPage6ToPdf(inpath: String, outpath: String, resPath: String, code: String, info: Array[String]) = {
    val reader = new com.itextpdf.kernel.pdf.PdfReader(inpath)
    val writer = new PdfWriter(outpath)
    val pdfDoc = new PdfDocument(reader, writer)
    val canvas = new PdfCanvas(pdfDoc.getFirstPage)
    val document = new Document(pdfDoc)

    val font = PdfFontFactory.createFont(resPath + "/resource/fonts/SourceHanSansSC-Regular.otf", PdfEncodings.IDENTITY_H, true)
    val fontbd = PdfFontFactory.createFont(resPath + "/resource/fonts/SourceHanSansCN-Bold.ttf", PdfEncodings.IDENTITY_H, true)

    val titleSize = if (info(1).length >= 26) {
      17
    } else {
      20
    }

    val title = new Paragraph(info(1)).setFont(fontbd).setFontSize(titleSize).setFontColor(dull_red).setFixedLeading(16f).setTextAlignment(TextAlignment.LEFT).setFixedPosition(1, 208.6327f, 627.9647f, width)
    document.add(title)

    val name = new Paragraph(info.head + "先生您好！").setFont(font).setFontSize(10).setFontColor(navy_blue).setTextAlignment(TextAlignment.LEFT).setFixedPosition(56.8f,586.6f,width)
    document.add(name)


    val t = "单倍群，它属于" + info(2) + "单倍群的下游支系。"
    val l = 24 - info(1).length * 3 / 5

    val x1 = new Paragraph("过英莱盾的测试和分析，推测您的Y染色体属于").setFontSize(10).setFontColor(navy_blue)
    val x2 = new Paragraph(info(1)).setFontSize(12).setFontColor(dull_red)
    val x3 = new Paragraph(t.take(l)).setFontSize(10).setFontColor(navy_blue)
    val x4 = new Paragraph(t.drop(l)).setFontSize(10).setFontColor(navy_blue)

    val test = x1.add(x2).add(x3).setFont(font).setFixedLeading(16f).setTextAlignment(TextAlignment.JUSTIFIED_ALL).setFixedPosition(1, 57.0625f, 527f, width)
    document.add(test)

    document.add(x4.setFont(font).setFixedLeading(16f).setTextAlignment(TextAlignment.LEFT).setFixedPosition(1, 57.0625f, 516.5f, width))

    var yy = 490.7f + 14.2f


    val summarizeTxt = FileUtils.readLines(new File(resPath + "/resource/text/summarize/" + code + ".txt"), "UTF-8").asScala
    summarizeTxt.map { x =>
      yy = yy - 14.2f
      val text = if (x.length > 40) {
        new Paragraph(x).setFont(font).setFontSize(10).setFontColor(navy_blue).setFixedLeading(16f).setTextAlignment(TextAlignment.JUSTIFIED_ALL).setFixedPosition(1, left, yy, width)
      } else {
        new Paragraph(x).setFont(font).setFontSize(10).setFontColor(navy_blue).setFixedLeading(16f).setTextAlignment(TextAlignment.LEFT).setFixedPosition(1, left, yy, width)
      }
      document.add(text)
    }

    val migraImage = resPath + "/resource/picture/migrate/" + code + "_1.png"

    val migraFold = Utils.getFold(migraImage)
    val migraHeight = 397.7f - summarizeTxt.length * 14.2f
    val migraWidth = migraHeight / migraFold
    val migraLeft = (603.78f - migraWidth) / 2

    Itext7.addImageToPdf(canvas, migraImage, migraLeft, 93f, migraWidth)

    document.close()
    reader.close()
    writer.close()
    pdfDoc.close()

    migraWidth

  }

  def addPage7ToPdf(inpath: String, outpath: String, resPath: String, code: String, firstMigrateWidth: Float) = {
    val reader = new com.itextpdf.kernel.pdf.PdfReader(inpath)
    val writer = new PdfWriter(outpath)
    val pdfDoc = new PdfDocument(reader, writer)
    val canvas = new PdfCanvas(pdfDoc.getFirstPage)
    val document = new Document(pdfDoc)

    val font = PdfFontFactory.createFont(resPath + "/resource/fonts/SourceHanSansSC-Regular.otf", PdfEncodings.IDENTITY_H, true)

    val diffFold = Utils.getFold(resPath + "/resource/picture/migrate/" + code + "_2.png")
    val diffWidth = firstMigrateWidth
    val diffHeight = diffWidth * diffFold
    val diffLeft = (603.78f - diffWidth) / 2

    Itext7.addImageToPdf(canvas, resPath + "/resource/picture/migrate/" + code + "_2.png", diffLeft, 769f - diffHeight, diffWidth)


    val leftLegendHeight = 769f - diffHeight - 20f

    val leftLegend = new Paragraph("您的父系祖先中主要人群在近 1 万年以来的大致扩散路线").setFont(font).setFontSize(11).setFontColor(navy_blue).setTextAlignment(TextAlignment.LEFT).setFixedPosition(1, 263f, leftLegendHeight, width)
    document.add(leftLegend)

    val titleHeight = leftLegendHeight - 50f

    Itext7.addImageToPdf(canvas, resPath + "/resource/picture/title.jpg", 55.5f, titleHeight - 1f, 29.5f)

    val title = new Paragraph("迁徙历史").setFont(font).setFontSize(15).setFontColor(black).setTextAlignment(TextAlignment.LEFT).setFixedPosition(1, 87f, titleHeight, width)
    document.add(title)


    val migraTxt = FileUtils.readFileToString(new File(resPath + "/resource/text/migrate/" + code + ".txt"), "UTF-8")
    var nextPage = new Array[String](0)
    val migraArray = migraTxt.split("bold")

    var textHead = titleHeight - 43f
    breakable(
      migraArray.foreach { x =>
        val head = new Paragraph(x.split("\n").head).setFont(font).setFontSize(12).setFontColor(dull_red_light).setTextAlignment(TextAlignment.LEFT).setFixedPosition(1, left, textHead, width)

        val histroy = x.split("\n").tail
        var textlines = textHead

        if (textlines - histroy.length * 16f < 45f) {
          nextPage = migraArray.drop(migraArray.indexOf(x))
          break()
        }

        histroy.map { y =>
          textlines = textlines - 16f
          val body = if (y.length >= 40) {
            new Paragraph(y).setFont(font).setFontSize(10).setFontColor(navy_blue).setTextAlignment(TextAlignment.JUSTIFIED_ALL).setFixedPosition(1, left, textlines, width)
          } else {
            new Paragraph(y).setFont(font).setFontSize(10).setFontColor(navy_blue).setTextAlignment(TextAlignment.LEFT).setFixedPosition(1, left, textlines, width)
          }
          document.add(body)
        }

        document.add(head)

        textHead = textHead - 16f * histroy.length - 25f

      })


    document.close()
    reader.close()
    writer.close()
    pdfDoc.close()

    nextPage
  }

  def addPage8ToPdf(inpath: String, outpath: String, resPath: String, code: String, text: Array[String]) = {
    val reader = new com.itextpdf.kernel.pdf.PdfReader(inpath)
    val writer = new PdfWriter(outpath)
    val pdfDoc = new PdfDocument(reader, writer)
    val canvas = new PdfCanvas(pdfDoc.getFirstPage)
    val document = new Document(pdfDoc)

    val font = PdfFontFactory.createFont(resPath + "/resource/fonts/SourceHanSansSC-Regular.otf", PdfEncodings.IDENTITY_H, true)
    val fontbd = PdfFontFactory.createFont(resPath + "/resource/fonts/SourceHanSansCN-Bold.ttf", PdfEncodings.IDENTITY_H, true)

    var textHead = 746f
    if (!text.isEmpty) {

      text.map { x =>
        val head = new Paragraph(x.split("\n").head).setFont(font).setFontSize(12).setFontColor(dull_red_light).setTextAlignment(TextAlignment.LEFT).setFixedPosition(1, left, textHead, width)
        val histroy = x.split("\n").drop(1)
        var textlines = textHead

        histroy.map { y =>
          textlines = textlines - 16f
          val body = if (y.length >= 40) {
            new Paragraph(y).setFont(font).setFontSize(10).setFontColor(navy_blue).setTextAlignment(TextAlignment.JUSTIFIED_ALL).setFixedPosition(1, left, textlines, width)
          } else {
            new Paragraph(y).setFont(font).setFontSize(10).setFontColor(navy_blue).setTextAlignment(TextAlignment.LEFT).setFixedPosition(1, left, textlines, width)
          }
          document.add(body)
        }

        textHead = textHead - 16f * histroy.length - 25f
        document.add(head)
      }
    }
    val titleHead = textHead + 25f - 54.3f + 22f

    Itext7.addImageToPdf(canvas, resPath + "/resource/picture/title2.jpg", 55.5f, titleHead - 1f, 25f)

    val title = new Paragraph("相关人物").setFont(font).setFontSize(15).setFontColor(black).setTextAlignment(TextAlignment.LEFT).setFixedPosition(1, 85f, titleHead, width)
    document.add(title)

    val histroy = FileUtils.readLines(new File(resPath + "/resource/text/stroy/" + code + ".txt"), "UTF-8").asScala
    var histroyLines = titleHead - 34f + 16f
    histroy.map { x =>
      histroyLines = histroyLines - 16f
      val histroyText = if (histroy.last == x) {
        new Paragraph(x).setFont(font).setFontSize(10).setFontColor(navy_blue).setFixedLeading(16f).setTextAlignment(TextAlignment.JUSTIFIED).setFixedPosition(1, left, histroyLines, width)
      } else {
        new Paragraph(x).setFont(font).setFontSize(10).setFontColor(navy_blue).setFixedLeading(16f).setTextAlignment(TextAlignment.JUSTIFIED_ALL).setFixedPosition(1, left, histroyLines, width)
      }
      document.add(histroyText)
    }

    val file = new File(resPath + "/resource/picture/famous").listFiles(new FileFilter {
      override def accept(pathname: File): Boolean = {
        pathname.getName.split("_").head == code.toString
      }
    })

    file.foreach { x =>
      val i = file.indexOf(x)
      Itext7.addImageByRectToPdf(canvas, x.getAbsolutePath, left + 134f * i, histroyLines - 174.5f, 118f,161.5f)
      Itext7.addRectangleWithBackgroundColor(canvas, left + 134f * i +0.5f, histroyLines - 174.5f , 117f, 18.75f, new DeviceRgb(62, 62, 63))

    }

    val files = new File(resPath + "/resource/text/famous/" + code).listFiles()

    files.map { x =>
      val famous = FileUtils.readLines(x, "UTF-8").asScala
      val index = files.indexOf(x)
      val famousName = new Paragraph(famous.head).setFont(font).setFontSize(8).setFontColor(new DeviceRgb(255, 255, 255)).setTextAlignment(TextAlignment.CENTER).setFixedPosition(1, left + 134f * index, histroyLines - 172f, 118f)
      document.add(famousName)
      var famousLines = histroyLines - 174.5f - 35.5f + 16f
      famous.drop(1).map { y =>
        val famousText = if (famous.last == y) {
          new Paragraph(y).setFont(font).setFontSize(8).setFontColor(black).setFixedLeading(16f).setTextAlignment(TextAlignment.LEFT).setFixedPosition(1, left + 134f * index, famousLines, 118f)
        } else {
          new Paragraph(y).setFont(font).setFontSize(8).setFontColor(black).setFixedLeading(16f).setTextAlignment(TextAlignment.JUSTIFIED_ALL).setFixedPosition(1, left + 134f * index, famousLines, 118f)
        }
        famousLines = famousLines - 16f

        document.add(famousText)
      }
    }
    document.close()
    reader.close()
    writer.close()
    pdfDoc.close()

  }

  def addPage9ToPdf(inpath: String, outpath: String, resPath: String, str : Array[String]) = {
    val reader = new com.itextpdf.kernel.pdf.PdfReader(inpath)
    val writer = new PdfWriter(outpath)
    val pdfDoc = new PdfDocument(reader, writer)

    val document = new Document(pdfDoc)

    val font = PdfFontFactory.createFont(resPath + "/resource/fonts/SourceHanSansSC-Regular.otf", PdfEncodings.IDENTITY_H, true)

    str.indices.foreach{i=>
      val text = new Paragraph(str(i)).setFont(font).setFontSize(9).setFontColor(black).setTextAlignment(TextAlignment.CENTER).setFixedPosition(1, 58.1f + 13.32f * i, 633.5f, 13f)
      document.add(text)
    }

    document.close()
    reader.close()
    writer.close()
    pdfDoc.close()

  }


}
