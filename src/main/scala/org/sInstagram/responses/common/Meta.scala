package org.sInstagram.responses.common

case class Meta(
  errorType: Option[String],
	code: Int,
	errorMessage: Option[String]
)

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json
object Meta {
  implicit val MetaFormat = JsonNaming.snakecase(Json.format[Meta])
}
