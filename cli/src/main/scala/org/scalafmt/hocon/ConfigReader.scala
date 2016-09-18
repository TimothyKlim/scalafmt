package org.scalafmt.config

import com.typesafe.config.Config
import org.scalafmt.Error

class ConfigReader[T](validKeys: Set[String])(f: (HoconOps, T) => T) {
  def validateConfig(config: Config): Option[Error] = {
    import scala.collection.JavaConverters._
    config.root().keySet().asScala.collectFirst {
      case x if !validKeys.contains(x) => Error.InvalidOption(x)
    }
  }
  def applyConfig(init: T, config: Config): Either[Error, T] = {
    val ops = new HoconOps(config)
    validateConfig(config) match {
      case Some(err) => Left(err)
      case _ =>
        try {
          Right(f(ops, init))
        } catch {
          // By catching the error here we avoid dealing with these exception
          // on every call to `.read[T]`
          case err: Error.FailedToParseOption =>
            Left(err)
        }
    }
  }
}
