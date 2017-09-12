package com.yukihirai0505.sInstagram.responses.common

case class User(
                 fullName: String,
                 id: String,
                 profilePicture: String,
                 username: String
               )

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json

object User {
  implicit val UserFormat = JsonNaming.snakecase(Json.format[User])
}
