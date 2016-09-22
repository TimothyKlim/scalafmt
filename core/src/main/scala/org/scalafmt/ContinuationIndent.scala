package org.scalafmt

import metaconfig.ConfigReader

@ConfigReader
case class ContinuationIndent(callSite: Int, defnSite: Int)

@ConfigReader
case class BinPack(callSite: Boolean, defnSite: Boolean, parentConstructors: Boolean)

