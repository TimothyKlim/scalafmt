package org.scalafmt.cli

import io.circe.Decoder
import org.scalafmt.AlignToken
import org.scalafmt.IndentOperator
import org.scalafmt.ScalafmtOptimizer
import org.scalafmt.ScalafmtRunner
import org.scalafmt.ScalafmtStyle
import org.scalafmt.util.debug
import org.scalatest.FunSuite

class YamlSpec extends FunSuite {
  val input =
    """
# Runner settings.
formatSbtFiles: true

# Optimizer settings.
bestEffortInDeeplyNestedCode: true

# Style settings.
baseStyle: intellij
maxColumn: 66
reformatDocstrings: false
scalaDocs: false
alignStripMarginStrings: true
binPackArguments: true
binPackParameters: true
configStyleArguments: false
binPackDotChains: true
noNewlinesBeforeJsNative: false
danglingParentheses: true
alignByOpenParenCallSite: false
alignByOpenParenDefnSite: false
continuationIndentCallSite: 3
continuationIndentDefnSite: 3
alignMixedOwners: false
binPackImportSelectors: true
spacesInImportCurlyBraces: true
allowNewlineBeforeColonInMassiveReturnTypes: false
binPackParentConstructors: true
spaceAfterTripleEquals: true
unindentTopLevelOperators: false
indentOperator:
  include: .*
  exclude: "&&"
rewriteTokens:
  =>: ⇒
  <-: ←
alignTokens:
- extends
- code: //
- code: =>
  owner: Function
alignByArrowEnumeratorGenerator: true
alignByIfWhileOpenParen: false
spaceBeforeContextBoundColon: true
"""
  val parsed = ConfigurationOptionsParser.parse(input)

  test("style") {
    val expectedStyle = ScalafmtStyle.intellij.copy(
      maxColumn = 66,
      reformatDocstrings = false,
      scalaDocs = false,
      alignStripMarginStrings = true,
      binPackArguments = true,
      binPackParameters = true,
      configStyleArguments = false,
      binPackDotChains = true,
      noNewlinesBeforeJsNative = false,
      danglingParentheses = true,
      alignByOpenParenCallSite = false,
      alignByOpenParenDefnSite = false,
      continuationIndentCallSite = 3,
      continuationIndentDefnSite = 3,
      alignMixedOwners = false,
      binPackImportSelectors = true,
      spacesInImportCurlyBraces = true,
      allowNewlineBeforeColonInMassiveReturnTypes = false,
      binPackParentConstructors = true,
      spaceAfterTripleEquals = true,
      unindentTopLevelOperators = false,
      indentOperator = IndentOperator(".*", "&&"),
      alignTokens = Set(
        AlignToken("extends", ".*"),
        AlignToken("//", ".*"),
        AlignToken("=>", "Function")
      ),
      rewriteTokens = Map(
        "=>" -> "⇒",
        "<-" -> "←"
      ),
      alignByArrowEnumeratorGenerator = true,
      alignByIfWhileOpenParen = false,
      spaceBeforeContextBoundColon = true
    )

    val obtainedStyle =
      parsed.map(_.toStyle(ScalafmtStyle.default)).valueOr(x => throw x)
    assert(obtainedStyle === expectedStyle)
  }

  test("runner") {
    // Runner
    val obtainedRunner =
      parsed.map(_.toRunner(ScalafmtRunner.default)).valueOr(x => throw x)
    val expectedRunner = ScalafmtRunner.default.copy(
      optimizer = ScalafmtOptimizer.default.copy(
        bestEffortEscape = true
      )
    )
    assert(obtainedRunner === expectedRunner)
  }
  test("inline syntax") {
    val config =
      """
        |scalafmt: { maxColumn: 40 }
      """.stripMargin
    val nested = ConfigurationOptionsParser.parse(config).valueOr(x => throw x)
    assert(nested.maxColumn.contains(40))
  }

  test("error messages") {

    import io.circe.generic.semiauto._
    import io.circe.parser._

    case class Foo(col: Option[Int])

    implicit val d: Decoder[Foo] = deriveDecoder[Foo].validate(x => {
      println(x.fields)
      val valid = Set("col")
      x.fields.exists(x => x.forall(valid.contains))
    }, "error")
    for {
      json <- parse("""|
                       |{
                       |  "cl": 2,
                       |  "nested": {
                       |    "k": 66
                       |  }
                       |}
                       |""".stripMargin)
    } yield {
      debug.elem(json)
    }

  }
}
