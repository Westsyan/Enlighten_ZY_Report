package com.yf.test

import java.io.File

import com.yf.classes.Controller
import org.apache.commons.io.FileUtils

import scala.collection.JavaConverters._

object test {

  def main(args: Array[String]): Unit = {

    val x = FileUtils.readLines(new File("D:\\ENLIGHTEN\\ZY_report\\demo/datas.txt")).asScala

    Controller.addPage6ToPdf("D:\\ENLIGHTEN\\model/6.pdf","D:\\ENLIGHTEN\\tmp/6.pdf","D:\\ENLIGHTEN\\ZY_report\\bin","1",x.head.split("\t").take(4).tail)

 //   Controller.addPage9ToPdf("D:\\ENLIGHTEN\\model/9.pdf","D:\\ENLIGHTEN\\tmp/9.pdf","D:\\ENLIGHTEN\\ZY_report\\bin",x.head.split("\t").drop(4))
  }
}
