package org.scalafmt

import org.scalafmt.Error.UnknownStyle

// TODO(olafur) experiment with shapeless to avoid this cruft :/
case class ConfigurationOptions(
    // Cli options
    formatSbtFiles: Option[Boolean],
    // Runner options
    bestEffortInDeeplyNestedCode: Option[Boolean],
    // Style options
    baseStyle: Option[String],
    maxColumn: Option[Int],
    reformatDocstrings: Option[Boolean],
    scalaDocs: Option[Boolean],
    alignStripMarginStrings: Option[Boolean],
    binPackArguments: Option[Boolean],
    binPackParameters: Option[Boolean],
    configStyleArguments: Option[Boolean],
    binPackDotChains: Option[Boolean],
    noNewlinesBeforeJsNative: Option[Boolean],
    danglingParentheses: Option[Boolean],
    alignByOpenParenCallSite: Option[Boolean],
    alignByOpenParenDefnSite: Option[Boolean],
    continuationIndentCallSite: Option[Int],
    continuationIndentDefnSite: Option[Int],
    alignMixedOwners: Option[Boolean],
    alignTokens: Option[Set[AlignToken]],
    binPackImportSelectors: Option[Boolean],
    spacesInImportCurlyBraces: Option[Boolean],
    allowNewlineBeforeColonInMassiveReturnTypes: Option[Boolean],
    binPackParentConstructors: Option[Boolean],
    spaceAfterTripleEquals: Option[Boolean],
    unindentTopLevelOperators: Option[Boolean],
    indentOperator: Option[IndentOperator],
    rewriteTokens: Option[Map[String, String]],
    alignByArrowEnumeratorGenerator: Option[Boolean],
    alignByIfWhileOpenParen: Option[Boolean],
    spaceBeforeContextBoundColon: Option[Boolean]
) {
  def toStyle(initStyle: ScalafmtStyle): ScalafmtStyle = {
    val style = baseStyle.map { x =>
      ScalafmtStyle.availableStyles.getOrElse(x, {
        throw UnknownStyle(x)
      })
    }.getOrElse(initStyle)
    // format: off
    style.copy(
      maxColumn                                   = maxColumn.getOrElse                                  (style.maxColumn),
      reformatDocstrings                          = reformatDocstrings.getOrElse                         (style.reformatDocstrings),
      scalaDocs                                   = scalaDocs.getOrElse                                  (style.scalaDocs),
      alignStripMarginStrings                     = alignStripMarginStrings.getOrElse                    (style.alignStripMarginStrings),
      binPackArguments                            = binPackArguments.getOrElse                           (style.binPackArguments),
      binPackParameters                           = binPackParameters.getOrElse                          (style.binPackParameters),
      configStyleArguments                        = configStyleArguments.getOrElse                       (style.configStyleArguments),
      binPackDotChains                            = binPackDotChains.getOrElse                           (style.binPackDotChains),
      noNewlinesBeforeJsNative                    = noNewlinesBeforeJsNative.getOrElse                   (style.noNewlinesBeforeJsNative),
      danglingParentheses                         = danglingParentheses.getOrElse                        (style.danglingParentheses),
      alignByOpenParenCallSite                    = alignByOpenParenCallSite.getOrElse                   (style.alignByOpenParenCallSite),
      alignByOpenParenDefnSite                    = alignByOpenParenDefnSite.getOrElse                   (style.alignByOpenParenDefnSite),
      continuationIndentCallSite                  = continuationIndentCallSite.getOrElse                 (style.continuationIndentCallSite),
      continuationIndentDefnSite                  = continuationIndentDefnSite.getOrElse                 (style.continuationIndentDefnSite),
      alignMixedOwners                            = alignMixedOwners.getOrElse                           (style.alignMixedOwners),
      alignTokens                                 = alignTokens.getOrElse                                (style.alignTokens),
      binPackImportSelectors                      = binPackImportSelectors.getOrElse                     (style.binPackImportSelectors),
      spacesInImportCurlyBraces                   = spacesInImportCurlyBraces.getOrElse                  (style.spacesInImportCurlyBraces),
      allowNewlineBeforeColonInMassiveReturnTypes = allowNewlineBeforeColonInMassiveReturnTypes.getOrElse(style.allowNewlineBeforeColonInMassiveReturnTypes),
      binPackParentConstructors                   = binPackParentConstructors.getOrElse                  (style.binPackParentConstructors),
      spaceAfterTripleEquals                      = spaceAfterTripleEquals.getOrElse                     (style.spaceAfterTripleEquals),
      unindentTopLevelOperators                   = unindentTopLevelOperators.getOrElse                  (style.unindentTopLevelOperators),
      indentOperator                              = indentOperator.getOrElse                             (style.indentOperator),
      rewriteTokens                               = rewriteTokens.getOrElse                              (style.rewriteTokens),
      alignByArrowEnumeratorGenerator             = alignByArrowEnumeratorGenerator.getOrElse            (style.alignByArrowEnumeratorGenerator),
      alignByIfWhileOpenParen                     = alignByIfWhileOpenParen.getOrElse                    (style.alignByIfWhileOpenParen),
      spaceBeforeContextBoundColon                = spaceBeforeContextBoundColon.getOrElse               (style.spaceBeforeContextBoundColon)
    )
    // format: on
  }

  def toRunner(initRunner: ScalafmtRunner): ScalafmtRunner = {
    initRunner.copy(
      optimizer = initRunner.optimizer.copy(
        bestEffortEscape = bestEffortInDeeplyNestedCode.getOrElse(
          initRunner.optimizer.bestEffortEscape))
    )
  }
}
