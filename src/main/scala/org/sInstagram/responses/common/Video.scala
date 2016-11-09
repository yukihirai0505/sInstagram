package org.sInstagram.responses.common

case class Video(url: String, width: Int, height: Int)

import play.api.libs.json.Json
object Video {
  implicit val VideoReads = Json.reads[Video]
  implicit val VideoWrites = Json.writes[Video]
}
