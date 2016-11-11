package com.yukihirai0505.responses.common

case class Images(
  lowResolution: Image,
  thumbnail: Image,
  standardResolution: Image
)

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json
object Images {
  implicit val ImagesFormat = JsonNaming.snakecase(Json.format[Images])
}
