package org.scalafmt.hocon

import com.typesafe.config.Config
import org.scalafmt.AlignToken
import org.scalafmt.ScalafmtStyle
import org.scalafmt.Error

object ScalafmtStyleHocon {

  private def readStyle(init: ScalafmtStyle, config: Config): ScalafmtStyle = {
    if (config.hasPath("style")) {
      val style = config.getString("style")
      ScalafmtStyle.availableStyles.getOrElse(style.toLowerCase, {
        val expected = ScalafmtStyle.activeStyles.keys.mkString(", ")
        throw Error.FailedToParseOption(
          "style",
          new IllegalArgumentException(
            s"Unknown style name $style. Expected one of: $expected")
        )
      })
    } else init
  }

  implicit val alignTokenReader = new HoconFieldReader[Set[AlignToken]] {
    override def read(config: Config, path: String): Set[AlignToken] = {
      import scala.collection.JavaConverters._
      println(config.getAnyRefList(path))
      ???
    }
  }
  val docStringReader = new HoconFieldReader[Boolean] {
    override def read(config: Config, path: String): Boolean = {
      config.getString(path) match {
        case "java" => false
        case "scala" => true
        case els => throw new IllegalArgumentException(s"Invalid value $els")
      }
    }
  }

  val validKeys =
      Set(
        "assumeStandardLibraryStripMargin",
        "docstrings"
      )
  val reader =
    new HoconConfigReader[ScalafmtStyle](validKeys)({
      case (ops, init) =>
        val s = readStyle(init, ops.config)
        // format: off
        s.copy(
                                            maxColumn = ops.read[Int]             ("maxColumn"                                   , s.maxColumn)                                   ,
                                   reformatDocstrings = ops.read[Boolean]         ("reformatDocstrings"                          , s.reformatDocstrings)                          ,
                                            scalaDocs = ops.read[Boolean]         ("docstrings"                                  , s.scalaDocs)(docStringReader)                  ,
                              alignStripMarginStrings = ops.read[Boolean]         ("assumeStandardLibraryStripMargin"            , s.alignStripMarginStrings)                     ,
                                     binPackArguments = ops.read[Boolean]         ("binPackArguments"                            , s.binPackArguments)                            ,
                                    binPackParameters = ops.read[Boolean]         ("binPackParameters"                           , s.binPackParameters)                           ,
                                 configStyleArguments = ops.read[Boolean]         ("configStyleArguments"                        , s.configStyleArguments)                        ,
                                     binPackDotChains = ops.read[Boolean]         ("binPackDotChains"                            , s.binPackDotChains)                            ,
                             noNewlinesBeforeJsNative = ops.read[Boolean]         ("noNewlinesBeforeJsNative"                    , s.noNewlinesBeforeJsNative)                    ,
                                  danglingParentheses = ops.read[Boolean]         ("danglingParentheses"                         , s.danglingParentheses)                         ,
                             alignByOpenParenCallSite = ops.read[Boolean]         ("alignByOpenParenCallSite"                    , s.alignByOpenParenCallSite)                    ,
                             alignByOpenParenDefnSite = ops.read[Boolean]         ("alignByOpenParenDefnSite"                    , s.alignByOpenParenDefnSite)                    ,
                           continuationIndentCallSite = ops.read[Int]             ("continuationIndentCallSite"                  , s.continuationIndentCallSite)                  ,
                           continuationIndentDefnSite = ops.read[Int]             ("continuationIndentDefnSite"                  , s.continuationIndentDefnSite)                  ,
                                     alignMixedOwners = ops.read[Boolean]         ("alignMixedOwners"                            , s.alignMixedOwners)                            ,
                                          alignTokens = ops.read[Set[AlignToken]] ("alignTokens"                                 , s.alignTokens)                                 ,
                               binPackImportSelectors = ops.read[Boolean]         ("binPackImportSelectors"                      , s.binPackImportSelectors)                      ,
                            spacesInImportCurlyBraces = ops.read[Boolean]         ("spacesInImportCurlyBraces"                   , s.spacesInImportCurlyBraces)                   ,
                  poorMansTrailingCommasInConfigStyle = ops.read[Boolean]         ("poorMansTrailingCommasInConfigStyle"         , s.poorMansTrailingCommasInConfigStyle)         ,
          allowNewlineBeforeColonInMassiveReturnTypes = ops.read[Boolean]         ("allowNewlineBeforeColonInMassiveReturnTypes" , s.allowNewlineBeforeColonInMassiveReturnTypes) ,
                            binPackParentConstructors = ops.read[Boolean]         ("binPackParentConstructors"                   , s.binPackParentConstructors)                   ,
                               spaceAfterTripleEquals = ops.read[Boolean]         ("spaceAfterTripleEquals"                      , s.spaceAfterTripleEquals)                      ,
                            unindentTopLevelOperators = ops.read[Boolean]         ("unindentTopLevelOperators"                   , s.unindentTopLevelOperators)                   ,
                      alignByArrowEnumeratorGenerator = ops.read[Boolean]         ("alignByArrowEnumeratorGenerator"             , s.alignByArrowEnumeratorGenerator)             ,
                              alignByIfWhileOpenParen = ops.read[Boolean]         ("alignByIfWhileOpenParen"                     , s.alignByIfWhileOpenParen)                     ,
                         spaceBeforeContextBoundColon = ops.read[Boolean]         ("spaceBeforeContextBoundColon"                , s.spaceBeforeContextBoundColon)                ,
                            keepSelectChainLineBreaks = ops.read[Boolean]         ("keepSelectChainLineBreaks"                   , s.keepSelectChainLineBreaks)                   ,
                  alwaysNewlineBeforeLambdaParameters = ops.read[Boolean]         ("alwaysNewlineBeforeLambdaParameters"         , s.alwaysNewlineBeforeLambdaParameters)
        )
      // format: on
    })
}
