package org.scalafmt.rewrite

import scala.meta._
import scala.meta.inputs.Input

class NoCurliesTest extends RewriteSuite(NoCurliesForSingleStatementDefs) {
  check(
    """|
       |object a {
       |  def x(i: Int): Int = {
       |    2
       |  }
       |}
       |
    """.stripMargin,
    """
      |object a {
      |  def x(i: Int): Int = 2
      |}
    """.stripMargin
  )
  check(
    """| // multiple statements
       |object a {
       |  def x(i: Int) = {
       |    2
       |    2
       |  }
       |}
       |
    """.stripMargin,
    """| // multiple statements
       |object a {
       |  def x(i: Int) = {
       |    2
       |    2
       |  }
       |}
       |
    """.stripMargin
  )

  check(
    """| // has comment
       |object a {
       |  def x(i: Int): Int = { // comment
       |    2
       |  }
       |}
       |
    """.stripMargin,
    """| // has comment
       |object a {
       |  def x(i: Int): Int = // comment
       |    2
       |}
    """.stripMargin
  )

  check(
    """| // procedure syntax
       |object a {
       |  def main(args: Seq[Int]) {
       |    2
       |  }
       |}
       |
    """.stripMargin,
    """| // procedure syntax
       |object a {
       |  def main(args: Seq[Int]) {
       |    2
       |  }
       |}
    """.stripMargin
  )
  check(
    """| // 2 procedure syntax
       |object a {
       |  def main(args: Seq[Int] = 2) {
       |    2
       |  }
       |}
       |
    """.stripMargin,
    """| // 2 procedure syntax
       |object a {
       |  def main(args: Seq[Int] = 2) {
       |    2
       |  }
       |}
    """.stripMargin
  )

  // caveats
  check(
    """| // missing return type
       |object a {
       |  def x() = {
       |    magic(1)
       |  }
       |}
       |
    """.stripMargin,
    """| // missing return type
       |object a {
       |  def x() = {
       |    magic(1)
       |  }
       |}
    """.stripMargin
  )
  check(
    """| // Unit return type
       |object a {
       |  def x(): Unit = {
       |    println(1)
       |  }
       |}
       |
    """.stripMargin,
    """| // Unit return type
       |object a {
       |  def x(): Unit = {
       |    println(1)
       |  }
       |}
    """.stripMargin
  )
  check(
    // What is the spec here?
    """| // nested block
       |object a {
       |  def x: Int = {
       |    { // comment
       |      2
       |    }
       |  }
       |}
       |
    """.stripMargin,
    """| // nested block
       |object a {
       |  def x: Int = { // comment
       |      2
       |    }
       |}
    """.stripMargin
  )

}
