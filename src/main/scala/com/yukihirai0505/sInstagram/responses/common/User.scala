package com.yukihirai0505.sInstagram.responses.common

case class User(
  id: String,
  username: String,
  fullName: String,
  profilePicture: String
)

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json
object User {
  implicit val UserFormat = JsonNaming.snakecase(Json.format[User])
}
