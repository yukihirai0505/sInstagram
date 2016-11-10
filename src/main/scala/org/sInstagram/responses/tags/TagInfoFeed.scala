package org.sInstagram.responses.tags

import org.sInstagram.instsagram.InstagramObject
import org.sInstagram.responses.common.Meta
import play.api.libs.json.Json

/**
  * author Yuki Hirai on 2016/11/09.
  */
case class TagInfoFeed(
  data: Option[TagInfoFeedData],
  meta: Option[Meta]
) extends InstagramObject
object TagInfoFeed {
  implicit val TagInfoFeedFormat = Json.format[TagInfoFeed]
}