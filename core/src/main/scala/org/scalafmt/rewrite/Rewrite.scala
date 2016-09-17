package org.scalafmt.rewrite

import scala.meta._
import org.scalafmt.util.LoggerOps._

abstract class Rewrite {
  def rewrite(code: Tree): Seq[Patch]
}

object Rewrite {

  private def nameMap[T](t: sourcecode.Text[T]*): Map[String, T] = {
    t.map(x => x.source -> x.value)(scala.collection.breakOut)
  }

  val name2rewrite: Map[String, Rewrite] = nameMap[Rewrite](
    NoCurliesForSingleStatementDefs,
    SortImportSelectors
  )
  val available = Rewrite.name2rewrite.keys.mkString(", ")

  val default: Seq[Rewrite] = name2rewrite.values.toSeq

  def apply(input: Input, rewrites: Seq[Rewrite]): String = {
    def noop = new String(input.chars)
    if (rewrites.isEmpty) {
      noop
    } else {
      input.parse[Source] match {
        case Parsed.Success(ast) =>
          val patches: Seq[Patch] = rewrites.flatMap(_.rewrite(ast))
          Patch(ast.tokens, patches)
        case _ => noop
      }
    }
  }
}
