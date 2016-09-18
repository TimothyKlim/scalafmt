package org.scalafmt.hocon

import com.typesafe.config.Config

trait HoconFieldReader[T] {
  def read(config: Config, path: String): T
}

object HoconFieldReader {
  implicit object IntHoconFieldReader extends HoconFieldReader[Int] {
    override def read(config: Config, path: String): Int =
      config.getInt(path)
  }
  implicit object BoolHoconFieldReader extends HoconFieldReader[Boolean] {
    override def read(config: Config, path: String): Boolean =
      config.getBoolean(path)
  }
}
