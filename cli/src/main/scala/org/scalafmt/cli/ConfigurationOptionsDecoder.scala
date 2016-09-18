package org.scalafmt.cli

import cats.data.Xor
import io.circe.Decoder
import io.circe.Error
import io.circe.Json
import io.circe.generic.auto._
import org.scalafmt.ConfigurationOptions
import org.scalafmt.yaml.Parser

object ConfigurationOptionsDecoder {
  import AlignTokenDecoder.decodeAlignToken

  val decoder: Decoder[ConfigurationOptions] =
    implicitly[Decoder[ConfigurationOptions]]
}

object ConfigurationOptionsParser {
  def parse(config: String): Xor[Error, ConfigurationOptions] = {
    for {
      x <- Parser.parse(config)
      json = {
        x.hcursor.downField("scalafmt").as[Json].getOrElse(x)
      }
      y <- json.as[ConfigurationOptions](ConfigurationOptionsDecoder.decoder)
    } yield y

  }

}
