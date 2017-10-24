package com.yukihirai0505.sInstagram.responses

/**
  * author Yuki Hirai on 2016/11/09.
  */
case class RelationshipFeed(
                             incomingStatus: Option[String],
                             outgoingStatus: Option[String]
                           )

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json

object RelationshipFeed {
  implicit val RelationshipFeedDataFormat = JsonNaming.snakecase(Json.format[RelationshipFeed])
}