package com.yukihirai0505.sInstagram.responses.common

case class Comments(count: Int)

import play.api.libs.json.Json
object Comments {
  implicit val CommentsFormat = Json.format[Comments]
}
