package org.scalafmt

import metaconfig.ConfigReader

@ConfigReader
case class ContinuationIndent(callSite: Int, defnSite: Int)

