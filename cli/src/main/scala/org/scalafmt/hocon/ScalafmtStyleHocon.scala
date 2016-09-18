package org.scalafmt.hocon

import com.typesafe.config.Config
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

  val reader =
    new HoconConfigReader[ScalafmtStyle](ScalafmtStyle.default.validKeys)({
      case (ops, init) =>
        val s = readStyle(init, ops.config)
        s.copy(
          maxColumn = ops.read[Int]("maxColumn", s.maxColumn)
        )
    })
}
