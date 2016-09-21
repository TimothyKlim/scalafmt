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
        |maxColumn = 555
      """.stripMargin
    val s = ConfigFactory.parseString(config)
    val result = Hocon2Class.gimmeClass(s, ScalafmtStyle.default.reader)
    logger.elem(result)

  }

}
