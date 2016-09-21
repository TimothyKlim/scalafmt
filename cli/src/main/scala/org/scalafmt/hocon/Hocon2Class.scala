package org.scalafmt.hocon

import com.typesafe.config.Config

object Hocon2Class {
  private def config2map(config: Config): Map[String, Any] = {
    import scala.collection.JavaConverters._
    def loop(obj: Any): Any = obj match {
      case map: java.util.Map[_, _] =>
        map.asScala.map {
          case (key, value) => key -> loop(value)
        }.toMap
      case map: java.util.List[_] =>
        map.asScala.map(loop).toList
      case e => e
    }
    loop(config.root().unwrapped()).asInstanceOf[Map[String, Any]]
  }

  def gimmeClass[T](config: Config,
                    reader: metaconfig.Reader[T]): metaconfig.Result[T] = {
    val map = config2map(config)
    println(map)
    reader.read(map)
  }

}
