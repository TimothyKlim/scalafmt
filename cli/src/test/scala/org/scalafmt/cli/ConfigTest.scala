package org.scalafmt.cli

import com.typesafe.config.ConfigFactory
import org.scalafmt.ScalafmtStyle
import org.scalafmt.hocon.ScalafmtStyleHocon
import org.scalafmt.util.LoggerOps._
import org.scalatest.FunSuite

class ConfigTest extends FunSuite {

  test("basic") {
    val config = ConfigFactory.parseString(
      """
        |maxColumn = 4000
      """.stripMargin
    )

    val expectedStyle = ScalafmtStyle.default.copy(
      maxColumn = 4000
    )
    val Right(obtainedStyle) =
      ScalafmtStyleHocon.reader.applyConfig(ScalafmtStyle.default, config)
    logger.elem(obtainedStyle, expectedStyle)
    assert(obtainedStyle == expectedStyle)
  }

}
