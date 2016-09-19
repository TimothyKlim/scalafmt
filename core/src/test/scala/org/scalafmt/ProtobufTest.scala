package org.scalafmt

import org.scalatest.FunSuite

class ProtobufTest extends FunSuite {
  import org.scalafmt.util.LoggerOps._
  test("pb") {
    val p = tutorial.example.Person.fromAscii(
      """
        |name: "foo"
      """.stripMargin
    )

    logger.elem(p)
  }

}
