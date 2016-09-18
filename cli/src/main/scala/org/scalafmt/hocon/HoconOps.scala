package org.scalafmt.config

import scala.util.control.NonFatal

import com.typesafe.config.Config
import org.scalafmt.Error

class HoconOps(config: Config) {
  def read[T](path: String, fallback: T)(implicit ev: HoconReader[T]): T = {
    try {
      if (config.hasPath(path)) ev.read(config, path)
      else fallback
    } catch {
      case NonFatal(e) => throw Error.FailedToParseOption(path, e)
    }
  }
}
