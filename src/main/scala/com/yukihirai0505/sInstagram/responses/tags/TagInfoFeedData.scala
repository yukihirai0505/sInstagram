package com.yukihirai0505.sInstagram.responses.tags

/**
  * author Yuki Hirai on 2016/11/09.
  */
case class TagInfoFeedData(
  mediaCount: Long,
  name: String
)

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json

object TagInfoFeedData {
  implicit val TagInfoFeedDataFormat = JsonNaming.snakecase(Json.format[TagInfoFeedData])
}
