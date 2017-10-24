package com.yukihirai0505.sInstagram.responses

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json

case class Counts(
                   follows: Int,
                   followedBy: Int,
                   media: Int
                 )

object Counts {
  implicit val CountsFormat = JsonNaming.snakecase(Json.format[Counts])
}

case class UserInfo(
                     bio: String,
                     counts: Counts,
                     id: String,
                     profilePicture: String,
                     username: String,
                     fullName: String,
                     website: String,
                     isBusiness: Boolean
                   )

object UserInfo {
  implicit val UserInfoDataFormat = JsonNaming.snakecase(Json.format[UserInfo])
}
