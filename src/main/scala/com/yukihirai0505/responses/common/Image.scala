package com.yukihirai0505.responses.common

case class Image(url: String, width: Int, height: Int)

import play.api.libs.json.Json
object Image {
  implicit val ImageFormat = Json.format[Image]
}
