package com.yukihirai0505.sInstagram.responses.likes

case class LikerUserInfo(
  username: String,
  firstName: String,
  lastName: String,
  `type`: String,
  id: String
)

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json
object LikerUserInfo {
  implicit val LikerUserInfoFormat = JsonNaming.snakecase(Json.format[LikerUserInfo])
}

