package org.scalafmt

import metaconfig.ConfigReader
import metaconfig.Reader

@ConfigReader
case class ContinuationIndent(callSite: Int, defnSite: Int)

@ConfigReader
case class BinPack(callSite: Boolean,
                   defnSite: Boolean,
                   parentConstructors: Boolean)

@ConfigReader
case class Align(
    openParenCallSite: Boolean,
    openParenDefnSite: Boolean,
    mixedOwners: Boolean,
    tokens: Set[AlignToken],
    arrowEnumeratorGenerator: Boolean,
    ifWhileOpenParen: Boolean
) {
  protected[scalafmt] val fallbackAlign = new AlignToken("<empty>", ".*")
  implicit val alignReader: Reader[AlignToken] = fallbackAlign.reader
}
