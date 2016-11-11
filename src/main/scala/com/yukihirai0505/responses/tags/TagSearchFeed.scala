package com.yukihirai0505.responses.tags

import com.yukihirai0505.instsagram.InstagramObject
import com.yukihirai0505.responses.common.Meta
import play.api.libs.json.Json

/**
  * author Yuki Hirai on 2016/11/09.
  */
case class TagSearchFeed(
  data: Option[List[TagInfoFeedData]],
  meta: Option[Meta]
) extends InstagramObject

object TagSearchFeed {
  implicit val TagSearchFeedFormat = Json.format[TagSearchFeed]
}