package org.sInstagram.responses.common

case class Caption(
  createdTime: Option[String],
  from: Option[FromTagData],
  id: Option[String],
  text: Option[String]
)

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json
object Caption {
  implicit val CaptionFormat = JsonNaming.snakecase(Json.format[Caption])
}