package com.yukihirai0505.sInstagram.responses

case class CommentData(
                        from: User,
                        createdTime: String,
                        id: String,
                        text: String
                      )

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json


object CommentData {
  implicit val CommentDataFormat = JsonNaming.snakecase(Json.format[CommentData])
}
