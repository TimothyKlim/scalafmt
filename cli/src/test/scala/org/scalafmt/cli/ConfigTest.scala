package org.scalafmt.cli

import com.typesafe.config.ConfigFactory
import org.scalafmt.config.ConfigReader
import org.scalafmt.util.LoggerOps._
import org.scalatest.FunSuite

class ConfigTest extends FunSuite {

  class Foo(implicit val args: sourcecode.Args)
  case class Settings(a: Int, b: Boolean) extends Foo

  object Settings {
    val default = Settings(0, b = false)
    val validKeys = default.args.value.flatten.map(_.source).toSet
    val settingsReader = new ConfigReader[Settings](validKeys)({
      case (ops, init) =>
        init.copy(
          a = ops.read[Int]("a", init.a),
          b = ops.read[Boolean]("b", init.b)
        )
    })
  }

  test("basic") {
    val config = ConfigFactory.parseString(
      """
        |scalafmt: {
        |  a = 333,
        |  b = false
        |}
      """.stripMargin
    )

    logger.elem(config, Settings.default.args)
    val result =
      Settings.settingsReader
        .applyConfig(Settings.default, config.getConfig("scalafmt"))
    logger.elem(result)

  }

}
