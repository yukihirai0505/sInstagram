package com.yukihirai0505.responses.common

case class Comment(
  createdTime: String,
  text: String,
  from: User,
  id: String
)

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json

object Comment {
  implicit val CommentFormat = JsonNaming.snakecase(Json.reads[Comment])
}
