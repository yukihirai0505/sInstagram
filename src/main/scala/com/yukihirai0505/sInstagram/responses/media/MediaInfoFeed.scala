package com.yukihirai0505.sInstagram.responses.media

import com.yukihirai0505.sInstagram.responses.common.Meta

/**
  * author Yuki Hirai on 2016/11/09.
  */
case class MediaInfoFeed(
                          data: MediaFeedData,
                          meta: Option[Meta]
                        )

import play.api.libs.json.Json

object MediaInfoFeed {
  implicit val MediaInfoFeedFormat = Json.format[MediaInfoFeed]
}