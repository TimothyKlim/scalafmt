package org.scalafmt.config

import com.typesafe.config.Config

trait HoconReader[T] {
  def read(config: Config, path: String): T
}

object HoconReader {
  implicit object IntHoconReader extends HoconReader[Int] {
    override def read(config: Config, path: String): Int =
      config.getInt(path)
  }
  implicit object BoolHoconReader extends HoconReader[Boolean] {
    override def read(config: Config, path: String): Boolean =
      config.getBoolean(path)
  }
}
