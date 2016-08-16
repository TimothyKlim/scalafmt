package org.scalafmt.cli

import cats.data.Xor
import io.circe.Decoder
import io.circe.Error
import org.scalafmt.ConfigurationOptions
import org.scalafmt.yaml.Parser

object OptionDecoder {
  import io.circe.generic.auto._
  val decoder: Decoder[ConfigurationOptions] =
    implicitly[Decoder[ConfigurationOptions]]
}

object ConfigurationOptionsParser {
  def parse(config: String): Xor[Error, ConfigurationOptions] = {
    for {
      x <- Parser.parse(config)
      y <- x.as[ConfigurationOptions](OptionDecoder.decoder)
    } yield y

  }

}
