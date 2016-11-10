package org.sInstagram.responses.users.basicinfo

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json

/**
  * author Yuki Hirai on 2016/11/09.
  */
case class UserInfoData(
  bio: Option[String],
  counts: Option[Counts],
  id: Option[String],
  profilePicture: Option[String],
  username: Option[String],
  fullName: Option[String],
  website: Option[String]
)

object UserInfoData {
  implicit val UserInfoDataFormat = JsonNaming.snakecase(Json.format[UserInfoData])
}
