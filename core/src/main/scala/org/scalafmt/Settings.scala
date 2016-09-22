package org.scalafmt

import scala.util.matching.Regex
import scala.collection.immutable.Set
import scala.collection.immutable.Seq

import metaconfig.ConfigReader
import metaconfig.Reader
import metaconfig.String2AnyMap
import org.scalafmt.util.LoggerOps

trait Settings {

  val indentOperatorsIncludeAkka = "^.*=$"
  val indentOperatorsExcludeAkka = "^$"
  val indentOperatorsIncludeDefault = ".*"
  val indentOperatorsExcludeDefault = "^(&&|\\|\\|)$"

  val default = ScalafmtStyle(
    maxColumn = 80,
    reformatDocstrings = true,
    scalaDocs = true,
    assumeStandardLibraryStripMargin = false,
    binPack = BinPack(
      callSite = false,
      defnSite = false,
      parentConstructors = false
    ),
    configStyleArguments = true,
    danglingParentheses = false,
    align = Align(
      openParenCallSite = true,
      openParenDefnSite = true,
      mixedOwners = false,
      tokens = Set.empty[AlignToken],
      arrowEnumeratorGenerator = false,
      ifWhileOpenParen = true
    ),
    binPackDotChains = false,
    noNewlinesBeforeJsNative = false,
    continuationIndent = ContinuationIndent(callSite = 2, defnSite = 4),
    binPackImportSelectors = false,
    spacesInImportCurlyBraces = false,
    poorMansTrailingCommasInConfigStyle = false,
    allowNewlineBeforeColonInMassiveReturnTypes = true,
    spaceAfterTripleEquals = false,
    unindentTopLevelOperators = false,
    indentOperator = IndentOperator(
      include = indentOperatorsIncludeDefault,
      exclude = indentOperatorsExcludeDefault
    ),
    rewriteTokens = Map.empty[String, String],
    spaceBeforeContextBoundColon = false,
    keepSelectChainLineBreaks = false,
    alwaysNewlineBeforeLambdaParameters = false
  )

  val intellij = default.copy(
    continuationIndent = ContinuationIndent(2, 2),
    align = default.align.copy(openParenCallSite = false),
    configStyleArguments = false,
    danglingParentheses = true
  )

  def addAlign(style: ScalafmtStyle) = style.copy(
    align = style.align.copy(
      mixedOwners = true,
      tokens = AlignToken.default
    )
  )

  val defaultWithAlign = addAlign(default)

  val default40 = default.copy(maxColumn = 40)
  val default120 = default.copy(maxColumn = 120)

  /**
    * Experimental implementation of:
    * https://github.com/scala-js/scala-js/blob/master/CODINGSTYLE.md
    */
  val scalaJs = default.copy(
    noNewlinesBeforeJsNative = true,
    binPack = BinPack(
      defnSite = true,
      callSite = true,
      parentConstructors = true
    ),
    continuationIndent = ContinuationIndent(4, 4),
    binPackImportSelectors = true,
    allowNewlineBeforeColonInMassiveReturnTypes = false,
    scalaDocs = false,
    align = default.align.copy(
      arrowEnumeratorGenerator = false,
      tokens = Set(AlignToken.caseArrow),
      ifWhileOpenParen = false
    )
  )

  /**
    * Ready styles provided by scalafmt.
    */
  val activeStyles =
    Map(
      "Scala.js" -> scalaJs,
      "IntelliJ" -> intellij
    ) ++ LoggerOps.name2style(
      default,
      defaultWithAlign
    )

  val availableStyles = {
    activeStyles ++ LoggerOps.name2style(
      scalaJs
    )
  }.map { case (k, v) => k.toLowerCase -> v }

  // TODO(olafur) move these elsewhere.
  val testing = default.copy(assumeStandardLibraryStripMargin = false)
  val unitTest80 = testing.copy(
    maxColumn = 80,
    continuationIndent = ContinuationIndent(4, 4)
  )

  val unitTest40 = unitTest80.copy(maxColumn = 40)

  val configReader: Reader[ScalafmtStyle] = Reader.instance[ScalafmtStyle] {
    case String2AnyMap(map) =>
      map.get("style") match {
        case Some(baseStyle) =>
          val noStyle = map.-("style")
          ScalafmtStyle.availableStyles.get(baseStyle.toString.toLowerCase) match {
            case Some(s) => s.reader.read(noStyle)
            case None =>
              val alternatives = ScalafmtStyle.activeStyles.keys.mkString(", ")
              Left(new IllegalArgumentException(
                s"Unknown style name $baseStyle. Expected one of: $alternatives"))
          }
        case None => ScalafmtStyle.default.reader.read(map)
      }
  }
}
