package org.scalafmt.rewrite

import scala.meta.Importee
import scala.meta.Tree
import scala.meta._
import scala.meta.tokens.Token.Equals
import scala.meta.tokens.Token.LeftBrace
import scala.meta.tokens.Token.RightBrace

import org.scalafmt.util.Whitespace

/**
  * Removes curly braces for method bodies with only a single statement.
  *
  * To minimize unwanted rewrites, we limit this rule to methods that explicitly
  * have a non-Unit return type.
  */
object NoCurliesForSingleStatementDefs extends Rewrite {
  override def rewrite(code: Tree): Seq[Patch] = {
    code.collect {
      case d: Defn.Def if (d.body match {
            case t: Term.Block if t.stats.length == 1 => true
            case _ => false
          }) && d.decltpe.exists(x => x.tokens.nonEmpty && x.syntax != "Unit") =>
        val open = d.body.tokens.head
        // The body could be a nested block.
        // def x = { /* comment */ { 1 } }
        // in which case we only want to remove the outermost layer.
        var firstLeftBrace = true
        val (blockToken, blockSyntax) = d.body.tokens.find {
          case LeftBrace() if firstLeftBrace =>
            firstLeftBrace = false
            false
          case Whitespace() => false
          case _ => true
        }.map(x => x -> x.syntax).getOrElse(open -> "")
        val close = d.body.tokens.last
        Seq(
          Patch(open, blockToken, blockSyntax),
          Patch(close, close, "")
        )
    }.flatten
  }
}
