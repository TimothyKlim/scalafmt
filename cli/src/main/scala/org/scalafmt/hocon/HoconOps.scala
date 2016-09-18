package org.scalafmt.hocon

import scala.util.control.NonFatal

import com.typesafe.config.Config
import org.scalafmt.Error

class HoconOps(val config: Config) {
  def read[T](path: String, fallback: T)(implicit ev: HoconFieldReader[T]): T = {
    try {
      if (config.hasPath(path)) ev.read(config, path)
      else fallback
    } catch {
      case NonFatal(e) => throw Error.FailedToParseOption(path, e)
    }
  }
}
