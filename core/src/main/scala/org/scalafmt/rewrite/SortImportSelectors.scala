package org.scalafmt.rewrite

import scala.meta.Importee
import scala.meta.Tree
import scala.meta._

/**
  * Sorts imports inside curly braces.
  *
  * For example
  *
  * import a.{c, b}
  *
  * into
  *
  * import a.{b, c}
  */
object SortImportSelectors extends Rewrite {
  override def rewrite(code: Tree): Seq[Patch] = {
    code.collect {
      case q"import ..$imports" =>
        imports.flatMap { `import` =>
          if (`import`.importees.exists(!_.is[Importee.Name])) {
            // Do nothing if an importee has for example rename
            // import a.{d, b => c}
            // I think we are safe to sort these, just want to convince myself
            // it's 100% safe first.
            Nil
          } else {
            val sortedImporteesByIndex: Map[Int, Importee] =
              `import`.importees
                .sortBy(x => x.syntax)
                .zipWithIndex
                .map(_.swap)
                .toMap
            `import`.importees.zipWithIndex.collect {
              case (importee, i) =>
                Patch(importee.tokens.head,
                      importee.tokens.last,
                      sortedImporteesByIndex(i).syntax)
            }
          }
        }
    }.flatten
  }
}
