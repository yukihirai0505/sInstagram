package org.sInstagram.responses.relationships

import org.sInstagram.instsagram.InstagramObject
import org.sInstagram.responses.common.Meta

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

