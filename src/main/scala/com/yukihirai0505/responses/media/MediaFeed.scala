package com.yukihirai0505.responses.media

import com.yukihirai0505.instsagram.InstagramObject
import com.yukihirai0505.responses.common._

/**
  * author Yuki Hirai on 2016/11/09.
  */
case class MediaFeed(
  data: Option[List[MediaFeedData]],
  pagination: Option[Pagination],
  meta: Option[Meta]) extends InstagramObject {
}

import play.api.libs.json.Json
object MediaFeed {
  implicit val MediaFeedFormat = Json.format[MediaFeed]
}