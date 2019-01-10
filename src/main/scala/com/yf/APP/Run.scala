package com.yf.APP

import java.io.File
import com.yf.classes._
object Run {


  def main(args: Array[String]): Unit = {

    val usage =
      """
        |用法：  java -jar  jarfiles  -i xx -o xx
        |注意：
        |   请按顺序输入，搞错顺序程序将不能正确运行
        |
        |其中选项包括：
        |
        |   -i      inputPath:输入源文件路径
        |   -o      outputPath:输出报告路径
        |   -c      查看输入数据结构
      """.stripMargin
    val log =
      """
        | 数据结构：
        |
        |   IndexID	姓名	  下游单倍群	20大类  	DYS19	DYS389I	DYS389II	DYS390	DYS391	DYS392	DYS393	DYS437	DYS438	DYS439	DYS448	DYS456	DYS458	DYS635	GATA-H4	DYS385a	DYS385b	DYS449	DYS460	DYS481	DYS518	DYS533	DYS570	DYS576	DYS627	DYS387S1a	DYS387S1b	DYS388	DYS444	DYS549	DYS643	DYS722	DYS404S1a	DYS404S1b	DYS527a	DYS527b
        |
        |   样例：
        |
        |     ELT50067	XXX C2b1a3a1c2d-F23945	C2b-M504	17	13	29	25	10	11	13	14	10	10	22	15	17	21	11	12	13	27	10	25	39	12	16	17	21	38	41	14	14	12	10	20	14	16	19	19
        |     ELT50199	XXX	C2a1-F2613/Z1338	C2a1-F2613	16	14	32	23	10	11	14	14	10	11	21	15	15	21	11	11	16	32	11	24	38	12	18	19	23	38	38	12	13	12	9	22	15	17	19	20
        |
        |     以上数据以分隔符分开
        |
        |   注意：若数据缺失，程序将不能正常运行！
      """.stripMargin
    val symbol = Array[String]("-i", "-o", "-c")
    if (args.length == 1 && args(0) == "-c") {
      println(log)
    } else if (args.length != 4) {
      println("输入参数缺失，请查看相关帮助：" + usage)
    } else {
      val (inPath, outPath) = (new File(args(1)), new File(args(3)))
      val a = Array(args(0), args(2))

      if (symbol.diff(a).length != 1) {
        println("输入选项有误，请查看相关帮助：" + usage)
      } else {
        Controller.createPdf(inPath.getPath, outPath.getPath)
      }
    }
  }


}
