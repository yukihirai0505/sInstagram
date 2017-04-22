package com.yukihirai0505.sInstagram.responses.likes

case class LikerUserInfo(
  username: Option[String],
  firstName: Option[String],
  lastName: Option[String],
  `type`: Option[String],
  id: Option[String]
)

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json
object LikerUserInfo {
  implicit val LikerUserInfoFormat = JsonNaming.snakecase(Json.format[LikerUserInfo])
}

