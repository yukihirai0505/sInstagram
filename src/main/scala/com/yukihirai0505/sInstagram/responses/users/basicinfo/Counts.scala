package com.yukihirai0505.sInstagram.responses.users.basicinfo

/**
  * author Yuki Hirai on 2016/11/09.
  */
case class Counts(
  follows: Int,
  followedBy: Int,
  media: Int
)

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json
object Counts {
  implicit val CountsFormat = JsonNaming.snakecase(Json.format[Counts])
}
