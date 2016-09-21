package org.scalafmt

import scala.util.matching.Regex

import metaconfig.ConfigReader

@ConfigReader
case class Config(
    // Note: default style is right below
    maxColumn: Int,
    reformatDocstrings: Boolean,
    scalaDocs: Boolean,
    alignStripMarginStrings: Boolean,
    binPackArguments: Boolean,
    binPackParameters: Boolean,
    configStyleArguments: Boolean,
    binPackDotChains: Boolean,
    noNewlinesBeforeJsNative: Boolean,
    danglingParentheses: Boolean,
    alignByOpenParenCallSite: Boolean,
    alignByOpenParenDefnSite: Boolean,
    continuationIndentCallSite: Int,
    continuationIndentDefnSite: Int,
    alignMixedOwners: Boolean,
    alignTokens: Set[AlignToken],
    binPackImportSelectors: Boolean,
    spacesInImportCurlyBraces: Boolean,
    poorMansTrailingCommasInConfigStyle: Boolean,
    allowNewlineBeforeColonInMassiveReturnTypes: Boolean,
    binPackParentConstructors: Boolean,
    spaceAfterTripleEquals: Boolean,
    unindentTopLevelOperators: Boolean,
    indentOperatorsIncludeFilter: Regex,
    indentOperatorsExcludeFilter: Regex,
    rewriteTokens: Map[String, String],
    alignByArrowEnumeratorGenerator: Boolean,
    alignByIfWhileOpenParen: Boolean,
    spaceBeforeContextBoundColon: Boolean,
    keepSelectChainLineBreaks: Boolean,
    alwaysNewlineBeforeLambdaParameters: Boolean
)
