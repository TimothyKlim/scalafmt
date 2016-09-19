package org.scalafmt

import org.scalatest.FunSuite

class ProtobufTest extends FunSuite {
  import org.scalafmt.util.LoggerOps._
  test("pb") {
    import tutorial.ScalafmtStyle
    ScalafmtStyle.Person()
    ScalafmtStyle.Person()
    val p = ScalafmtStyle.Person()
    logger.elem(ScalafmtStyle.AddressBook())
    println(p.name.getOrElse("Bar"))
    logger.elem(p)
  }

}
