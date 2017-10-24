package com.yukihirai0505.sInstagram.responses

case class LikeUserInfo(
                         username: String,
                         firstName: String,
                         lastName: String,
                         `type`: String,
                         id: String
                       )

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json

object LikeUserInfo {
  implicit val LikerUserInfoFormat = JsonNaming.snakecase(Json.format[LikeUserInfo])
}

