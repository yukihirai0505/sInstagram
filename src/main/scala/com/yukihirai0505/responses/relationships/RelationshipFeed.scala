package com.yukihirai0505.responses.relationships

import com.yukihirai0505.instsagram.InstagramObject
import com.yukihirai0505.responses.common.Meta

/**
  * author Yuki Hirai on 2016/11/09.
  */
case class RelationshipFeed(
  data: Option[RelationshipFeedData],
  meta: Option[Meta]
) extends InstagramObject


import play.api.libs.json.Json
object RelationshipFeed {
  implicit val RelationshipFeedFormat = Json.format[RelationshipFeed]
}
