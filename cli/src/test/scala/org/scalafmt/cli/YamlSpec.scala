package org.scalafmt.cli

import org.scalafmt.AlignToken
import org.scalafmt.IndentOperator
import org.scalafmt.ScalafmtOptimizer
import org.scalafmt.ScalafmtRunner
import org.scalafmt.ScalafmtStyle
import org.scalatest.FunSuite

class YamlSpec extends FunSuite {
  val input = """

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
  includeFilter: include
  excludeFilter: exclude
rewriteTokens:
  from: to
  to: from
alignTokens:
  - owner: owner
    code: code
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
      indentOperator = IndentOperator("include", "exclude"),
      alignTokens = Set(AlignToken("code", "owner")),
      rewriteTokens = Map("from" -> "to", "to" -> "from"),
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
}
