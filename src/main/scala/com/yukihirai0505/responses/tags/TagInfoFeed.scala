package com.yukihirai0505.responses.tags

import com.yukihirai0505.instsagram.InstagramObject
import com.yukihirai0505.responses.common.Meta
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