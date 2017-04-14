package com.yukihirai0505.sInstagram.responses.tags

import com.yukihirai0505.sInstagram.responses.common.Meta
import play.api.libs.json.Json

/**
  * author Yuki Hirai on 2016/11/09.
  */
case class TagSearchFeed(
  data: List[TagInfoFeedData],
  meta: Option[Meta]
)

object TagSearchFeed {
  implicit val TagSearchFeedFormat = Json.format[TagSearchFeed]
}