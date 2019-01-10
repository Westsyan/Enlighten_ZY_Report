package com.yf.test

import java.io.File

import org.apache.commons.io.FileUtils

import scala.collection.JavaConverters._

object test3 {


  def main(args: Array[String]): Unit = {
    val buffer = FileUtils.readLines(new File("D:\\桉树数据库相关数据\\桉树数据库相关数据12.4\\桉树数据库相关数据\\数据库提供材料\\P1_Augustus_Result/P1.Augustus.gff")).asScala

    val buffer1 = buffer.filter(_.take(3) == "chr").filter(_.split("\t")(2) == "gene")

    FileUtils.writeLines(new File("D:\\桉树数据库相关数据\\桉树数据库相关数据12.4\\桉树数据库相关数据\\数据库提供材料\\P1_Augustus_Result/P1.gene.gff"),buffer1.asJava)

  }
}
