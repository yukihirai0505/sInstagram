package com.yukihirai0505.sInstagram.responses.common

/**
  * author Yuki Hirai on 2016/11/09.
  */
case class FromTagData(
  fullName: String,
  id: String,
  profilePicture: String,
  username: String
)

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json
object FromTagData {
  implicit val FromTagDataFormat = JsonNaming.snakecase(Json.format[FromTagData])
}