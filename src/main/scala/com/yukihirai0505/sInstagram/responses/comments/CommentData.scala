package com.yukihirai0505.sInstagram.responses.comments

import com.yukihirai0505.sInstagram.responses.common.User
/**
  * author Yuki Hirai on 2016/11/09.
  */
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
