package org.scalafmt.cli

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigValue
import org.scalafmt.ScalafmtStyle
import org.scalafmt.hocon.ScalafmtStyleHocon
import org.scalafmt.util.LoggerOps._
import org.scalatest.FunSuite

class ConfigTest extends FunSuite {

  ignore("basic") {
    val config = ConfigFactory.parseString(
      """
        |maxColumn = 4000
        |alignTokens: [
        |  {
        |    code = "=>"
        |    owner = "Function"
        |  }
        |]
        |reformatDocstrings = false
        |docstrings = java
        |assumeStandardLibraryStripMargin = true
        |binPackArguments = true
        |binPackParameters = true
        |configStyleArguments = true
        |binPackDotChains = true
        |noNewlinesBeforeJsNative = true
        |danglingParentheses = true
        |alignByOpenParenCallSite = true
        |alignByOpenParenDefnSite = true
        |continuationIndentCallSite = 3
        |continuationIndentDefnSite = 3
        |alignMixedOwners = true
        |binPackImportSelectors = true
        |spacesInImportCurlyBraces = true
        |poorMansTrailingCommasInConfigStyle = true
        |allowNewlineBeforeColonInMassiveReturnTypes = true
        |binPackParentConstructors = true
        |spaceAfterTripleEquals = true
        |unindentTopLevelOperators = true
        |alignByArrowEnumeratorGenerator = true
        |alignByIfWhileOpenParen = true
        |spaceBeforeContextBoundColon = true
        |keepSelectChainLineBreaks = true
        |alwaysNewlineBeforeLambdaParameters = true
      """.stripMargin
    )

    val Right(obtained) =
      ScalafmtStyleHocon.reader.applyConfig(ScalafmtStyle.default, config)
    assert(obtained.maxColumn == 4000)
    assert(obtained.alignStripMarginStrings)
    assert(obtained.reformatDocstrings == false)
    assert(obtained.scalaDocs == false)
    assert(obtained.alignStripMarginStrings == true)
    assert(obtained.binPackArguments == true)
    assert(obtained.binPackParameters == true)
    assert(obtained.configStyleArguments == true)
    assert(obtained.binPackDotChains == true)
    assert(obtained.noNewlinesBeforeJsNative == true)
    assert(obtained.danglingParentheses == true)
    assert(obtained.alignByOpenParenCallSite == true)
    assert(obtained.alignByOpenParenDefnSite == true)
    assert(obtained.continuationIndentCallSite == 3)
    assert(obtained.continuationIndentDefnSite == 3)
    assert(obtained.alignMixedOwners == true)
    assert(obtained.binPackImportSelectors == true)
    assert(obtained.spacesInImportCurlyBraces == true)
    assert(obtained.poorMansTrailingCommasInConfigStyle == true)
    assert(obtained.allowNewlineBeforeColonInMassiveReturnTypes == true)
    assert(obtained.binPackParentConstructors == true)
    assert(obtained.spaceAfterTripleEquals == true)
    assert(obtained.unindentTopLevelOperators == true)
    assert(obtained.alignByArrowEnumeratorGenerator == true)
    assert(obtained.alignByIfWhileOpenParen == true)
    assert(obtained.spaceBeforeContextBoundColon == true)
    assert(obtained.keepSelectChainLineBreaks == true)
    assert(obtained.alwaysNewlineBeforeLambdaParameters == true)
  }

  test("map[string, any]") {
    val config =
      """
        |a: {
        | b = 2
        | hoobar: {
        |   k = 1
        | }
        | d = 3
        |}
        |c = true
        |k = [
        |  { i: 2 },
        |  { i: 2, y: 3}
        |]
        |
      """.stripMargin
    val s = ConfigFactory.parseString(config)
    println(s)
    val it = s.entrySet().iterator()
    import scala.collection.JavaConverters._
    def config2map(config: Config): Map[String, Any] = {
      def loop(obj: Any): Any = obj match {
        case map: java.util.Map[_, _] =>
          map.asScala.map {
            case (key, value) => key -> loop(value)
          }.toMap
        case map: java.util.List[_] =>
          map.asScala.map(loop).toList
        case e => e
      }
      loop(config.root().unwrapped()).asInstanceOf[Map[String, Any]]
    }
    logger.elem(s.getValue("a"))
    logger.elem(config2map(s))
  }

}
