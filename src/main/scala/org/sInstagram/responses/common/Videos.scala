package org.sInstagram.responses.common

case class Videos(
  lowBandwith: Option[Video],
  lowResolution: Video,
  standardResolution: Video
)

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json
object Videos {
  implicit val VideosFormat = JsonNaming.snakecase(Json.format[Videos])
}
