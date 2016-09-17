package org.scalafmt.rewrite

import scala.meta._
import scala.meta.inputs.Input

import org.scalafmt.Scalafmt

class SortImportSelectorsTest extends RewriteSuite(SortImportSelectors) {
  check(
    """
      |// basic
      |import a.b.{
      |  c,
      |  b,
      |  a
      |}, k.{
      |  g, f
      |}
      |import f.g.h.{
      |  j,
      |  i
      |}
      |
      |object A
    """.stripMargin,
    """
      |// basic
      |import a.b.{
      |  a,
      |  b,
      |  c
      |}, k.{
      |  f, g
      |}
      |import f.g.h.{
      |  i,
      |  j
      |}
      |
      |object A
    """.stripMargin
  )

  // Caveats
  check(
    """| // rename/wildcard/unimport
       |import a.{
       |  zzzz => _,
       |  bar
       |}
    """.stripMargin,
    """| // rename/wildcard/unimport
       |import a.{
       |  zzzz => _,
       |  bar
       |}
    """.stripMargin
  )
  check(
    """| // comments
       |import a.{
       |  c, // comment for c
       |  b
       |}
    """.stripMargin,
    """| // comments
       |import a.{
       |  b, // comment for c
       |  c
       |}
    """.stripMargin
  )

}
