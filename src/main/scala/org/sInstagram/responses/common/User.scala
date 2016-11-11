package org.sInstagram.responses.common

case class User(
  id: Option[String],
  username: Option[String],
  fullName: Option[String],
  profilePicture: Option[String]
)

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json
object User {
  implicit val UserFormat = JsonNaming.snakecase(Json.format[User])
}
