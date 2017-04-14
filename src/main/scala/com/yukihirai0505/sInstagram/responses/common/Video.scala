package com.yukihirai0505.sInstagram.responses.common

case class Video(url: String, width: Int, height: Int)

import play.api.libs.json.Json
object Video {
  implicit val VideoFormat = Json.format[Video]
}
