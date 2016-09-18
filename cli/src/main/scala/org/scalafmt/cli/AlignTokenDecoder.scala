package org.scalafmt.cli

import io.circe.Decoder
import io.circe.HCursor
import org.scalafmt.AlignToken

/**
  * Allows parsing an AlignToken like this
  *
  * "foo" -> AlignToken("foo", ".*")
  * code: "foo" -> AlignToken("foo", ".*")
  *
  * code: "foo"
  * owner: "bar"
  *   -> AlignToken("foo", "bar")
  */
object AlignTokenDecoder {
  implicit val decodeAlignToken: Decoder[AlignToken] = new Decoder[AlignToken] {
    final def apply(c: HCursor): Decoder.Result[AlignToken] = {
      val owner = c.get[String]("owner").getOrElse(".*")
      c.as[String].orElse(c.get[String]("code")).map(x => AlignToken(x, owner))
    }
  }
}
