package com.yukihirai0505.sInstagram.responses.users.basicinfo

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json

/**
  * author Yuki Hirai on 2016/11/09.
  */
case class UserInfoData(
  bio: String,
  counts: Counts,
  id: String,
  profilePicture: String,
  username: String,
  fullName: String,
  website: String,
  isBusiness: Boolean
)

object UserInfoData {
  implicit val UserInfoDataFormat = JsonNaming.snakecase(Json.format[UserInfoData])
}
