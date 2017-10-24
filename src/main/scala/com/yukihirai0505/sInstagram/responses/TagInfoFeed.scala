package com.yukihirai0505.sInstagram.responses

/**
  * author Yuki Hirai on 2016/11/09.
  */
case class TagInfoFeed(
                        mediaCount: Long,
                        name: String
                      )

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json

object TagInfoFeed {
  implicit val TagInfoFeedDataFormat = JsonNaming.snakecase(Json.format[TagInfoFeed])
}
