package com.yukihirai0505.responses.relationships

/**
  * author Yuki Hirai on 2016/11/09.
  */
case class RelationshipFeedData(
  incomingStatus: Option[String],
  outgoingStatus: Option[String],
  targetUserIsPrivate: Option[Boolean]
)

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json

object RelationshipFeedData {
  implicit val RelationshipFeedDataFormat = JsonNaming.snakecase(Json.format[RelationshipFeedData])
}