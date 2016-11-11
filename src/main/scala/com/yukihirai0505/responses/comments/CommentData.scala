package com.yukihirai0505.responses.comments

/**
  * author Yuki Hirai on 2016/11/09.
  */
case class CommentData(
  form: Option[String],
  createdTime: Option[String],
  id: Option[String],
  text: Option[String]
)

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json

object CommentData {
  implicit val CommentDataFormat = JsonNaming.snakecase(Json.format[CommentData])
}
