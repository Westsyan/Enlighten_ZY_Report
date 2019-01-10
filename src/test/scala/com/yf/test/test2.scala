package com.yf.test

import java.io.File

import com.yf.classes.{Controller, Itext7}
import org.apache.commons.io.FileUtils

import scala.collection.JavaConverters._

object test2 {


  def main(args: Array[String]): Unit = {

    val data = FileUtils.readLines(new File("D:\\ENLIGHTEN\\ZY_report\\demo/data.txt")).asScala

    val codes = FileUtils.readLines(new File("D:\\ENLIGHTEN\\ZY_report\\bin\\resource\\configure/YToCode.txt")).asScala.
      map(_.split("\t")).map(x=>x.head -> x.last).toMap

    val resPath = "D:\\ENLIGHTEN\\ZY_report\\bin"

    data.foreach{x=>
      val datas = x.split("\t")

      val code = codes(datas(3))

      val information = Array(datas(1),datas(2),datas(3))
      new File("D:\\ENLIGHTEN\\tmp/"+data.indexOf(x)).mkdir()

      FileUtils.copyDirectory(new File("D:\\ENLIGHTEN\\model"),new File("D:\\ENLIGHTEN\\tmp/"+data.indexOf(x)))

      val width =  Controller.addPage6ToPdf("D:\\ENLIGHTEN\\model/6.pdf","D:\\ENLIGHTEN\\tmp/"+data.indexOf(x)+"/6.pdf",resPath,code,information)

      val text = Controller.addPage7ToPdf("D:\\ENLIGHTEN\\model/7.pdf","D:\\ENLIGHTEN\\tmp/"+data.indexOf(x)+"/7.pdf",resPath,code,width)

      Controller.addPage8ToPdf("D:\\ENLIGHTEN\\model/8.pdf","D:\\ENLIGHTEN\\tmp/"+data.indexOf(x)+"/8.pdf",resPath,code,text)

      Controller.addPage9ToPdf("D:\\ENLIGHTEN\\model/9.pdf","D:\\ENLIGHTEN\\tmp/"+data.indexOf(x)+"/9.pdf",resPath,datas.drop(4))

      Itext7.mergePdfFiles(new File("D:\\ENLIGHTEN\\tmp/"+data.indexOf(x)).listFiles().sortBy(_.getName.split('.').head.toInt),"D:/ENLIGHTEN\\tmp/" + datas(0) + ".pdf")
    }


  }
}
