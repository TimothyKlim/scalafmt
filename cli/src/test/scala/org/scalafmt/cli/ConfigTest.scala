package org.scalafmt.cli

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigValue
import org.scalafmt.AlignToken
import org.scalafmt.IndentOperator
import org.scalafmt.ScalafmtStyle
import org.scalafmt.hocon.Hocon2Class
import org.scalafmt.util.LoggerOps._
import org.scalatest.FunSuite

class ConfigTest extends FunSuite {

  test("style = ...") {
    import org.scalafmt.Config
    val Left(err) = Config.fromHocon("style = foobar")
    assert(
      "Unknown style name foobar. Expected one of: Scala.js, IntelliJ, default, defaultWithAlign" == err.getMessage)

    val overrideOne = Config.fromHocon("""|style = defaultWithAlign
                                          |maxColumn = 100
                                          |""".stripMargin)
    assert(
      Right(ScalafmtStyle.defaultWithAlign.copy(maxColumn = 100)) == overrideOne)
    assert(
      Right(ScalafmtStyle.intellij) == Config.fromHocon("style = intellij"))
    assert(
      Right(ScalafmtStyle.scalaJs) == Config.fromHocon("style = Scala.js"))
    assert(
      Right(ScalafmtStyle.defaultWithAlign) == Config.fromHocon(
        "style = defaultWithAlign"))
  }

  test("hocon2class") {
    val config =
      """
        |style = intellij
        |maxColumn = 4000
        |reformatDocstrings = false
        |scalaDocs = false
        |assumeStandardLibraryStripMargin = true
        |binPack: {
        |  defnSite = true
        |  callSite = true
        |  parentConstructors = true
        |}
        |configStyleArguments = true
        |binPackDotChains = true
        |noNewlinesBeforeJsNative = true
        |danglingParentheses = true
        |continuationIndent: {
        |  callSite = 3
        |  defnSite = 5
        |}
        |binPackImportSelectors = true
        |spacesInImportCurlyBraces = true
        |poorMansTrailingCommasInConfigStyle = true
        |allowNewlineBeforeColonInMassiveReturnTypes = true
        |spaceAfterTripleEquals = true
        |unindentTopLevelOperators = true
        |align: {
        |  tokens = [
        |    {code: "=>", owner: "Function"},
        |    {code: "//"},
        |  ]
        |  arrowEnumeratorGenerator = true
        |  ifWhileOpenParen = true
        |  openParenCallSite = true
        |  openParenDefnSite = true
        |  mixedOwners = true
        |}
        |indentOperator: {
        |  "include" = inc
        |  exclude = exclude
        |}
        |spaceBeforeContextBoundColon = true
        |keepSelectChainLineBreaks = true
        |alwaysNewlineBeforeLambdaParameters = true
      """.stripMargin
    val Right(obtained) = org.scalafmt.Config.fromHocon(config)
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
    assert(obtained.align.openParenCallSite == true)
    assert(obtained.align.openParenDefnSite == true)
    assert(obtained.continuationIndentCallSite == 3)
    assert(obtained.continuationIndentDefnSite == 5)
    assert(obtained.align.mixedOwners == true)
    assert(obtained.binPackImportSelectors == true)
    assert(obtained.spacesInImportCurlyBraces == true)
    assert(obtained.poorMansTrailingCommasInConfigStyle == true)
    assert(obtained.allowNewlineBeforeColonInMassiveReturnTypes == true)
    assert(obtained.binPackParentConstructors == true)
    assert(obtained.spaceAfterTripleEquals == true)
    assert(obtained.unindentTopLevelOperators == true)
    assert(obtained.align.arrowEnumeratorGenerator == true)
    assert(obtained.align.ifWhileOpenParen == true)
    assert(obtained.spaceBeforeContextBoundColon == true)
    assert(obtained.keepSelectChainLineBreaks == true)
    assert(obtained.alwaysNewlineBeforeLambdaParameters == true)
    assert(
      obtained.align.tokens ==
        Set(
          AlignToken("//", ".*"),
          AlignToken("=>", "Function")
        ))
    assert(obtained.indentOperator == IndentOperator("inc", "exclude"))

    logger.elem(obtained.indentOperator)
  }

}
