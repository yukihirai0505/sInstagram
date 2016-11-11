package com.yukihirai0505.responses.common

/**
  * author Yuki Hirai on 2016/11/09.
  */
case class FromTagData(
  fullName: Option[String],
  id: Option[String],
  profilePicture: Option[String],
  username: Option[String]
)

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json
object FromTagData {
  implicit val FromTagDataFormat = JsonNaming.snakecase(Json.format[FromTagData])
}