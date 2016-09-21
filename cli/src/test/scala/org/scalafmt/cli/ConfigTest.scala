package org.scalafmt.cli

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigValue
import org.scalafmt.ScalafmtStyle
import org.scalafmt.hocon.Hocon2Class
import org.scalafmt.util.LoggerOps._
import org.scalatest.FunSuite

class ConfigTest extends FunSuite {

  test("hocon2class") {
    val config =
      """
        |maxColumn = 4000
        |reformatDocstrings = false
        |scalaDocs = false
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
    val s = ConfigFactory.parseString(config)
    val Right(obtained) =
      Hocon2Class.gimmeClass[ScalafmtStyle](s, ScalafmtStyle.default.reader)
    assert(obtained.maxColumn == 4000)
    assert(obtained.assumeStandardLibraryStripMargin)
    assert(obtained.reformatDocstrings == false)
    assert(obtained.scalaDocs == false)
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

}
