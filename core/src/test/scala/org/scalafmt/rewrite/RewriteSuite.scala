package org.scalafmt.rewrite

import scala.meta.inputs.Input

import org.scalafmt.util.DiffAssertions
import org.scalafmt.util.LoggerOps
import org.scalatest.FunSuiteLike

abstract class RewriteSuite(rewrite: Rewrite)
    extends FunSuiteLike
    with DiffAssertions {
  def check(original: String, expected: String): Unit = {
    test(LoggerOps.reveal(original).take(20)) {
      val obtained = Rewrite(Input.String(original), Seq(rewrite))
      import LoggerOps.stripTrailingSpace
      assertNoDiff(
        stripTrailingSpace(obtained),
        stripTrailingSpace(expected)
      )
    }
  }
}
