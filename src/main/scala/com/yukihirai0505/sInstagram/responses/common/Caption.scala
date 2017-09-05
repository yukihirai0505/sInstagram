package com.yukihirai0505.sInstagram.responses.common

case class Caption(
  createdTime: String,
  from: FromTagData,
  id: String,
  text: String
)

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json
object Caption {
  implicit val CaptionFormat = JsonNaming.snakecase(Json.format[Caption])
}