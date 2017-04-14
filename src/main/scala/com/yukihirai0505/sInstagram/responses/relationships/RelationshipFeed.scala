package com.yukihirai0505.sInstagram.responses.relationships

import com.yukihirai0505.sInstagram.responses.common.Meta

/**
  * author Yuki Hirai on 2016/11/09.
  */
case class RelationshipFeed(
  data: RelationshipFeedData,
  meta: Option[Meta]
)


import play.api.libs.json.Json
object RelationshipFeed {
  implicit val RelationshipFeedFormat = Json.format[RelationshipFeed]
}

