package com.yukihirai0505.responses.media

import com.yukihirai0505.instsagram.InstagramObject
import com.yukihirai0505.responses.common.Meta

/**
  * author Yuki Hirai on 2016/11/09.
  */
case class MediaInfoFeed(
  data: Option[MediaFeedData],
  meta: Option[Meta]
) extends InstagramObject

import play.api.libs.json.Json
object MediaInfoFeed {
  implicit val MediaInfoFeedFormat = Json.format[MediaInfoFeed]
}