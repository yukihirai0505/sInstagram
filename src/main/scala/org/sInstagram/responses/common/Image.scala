package org.sInstagram.responses.common

case class Image(url: String, width: Int, height: Int)

import play.api.libs.json.Json
object Image {
  implicit val ImageReads = Json.reads[Image]
  implicit val ImageWrites = Json.writes[Image]
}
