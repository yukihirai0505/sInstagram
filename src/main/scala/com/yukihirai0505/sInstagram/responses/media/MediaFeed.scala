package com.yukihirai0505.sInstagram.responses.media

import com.yukihirai0505.sInstagram.responses.common._

/**
  * author Yuki Hirai on 2016/11/09.
  */
case class MediaFeed(
                      data: Seq[MediaFeedData],
                      pagination: Option[Pagination],
                      meta: Option[Meta]
                    )

import play.api.libs.json.Json

object MediaFeed {
  implicit val MediaFeedFormat = Json.format[MediaFeed]
}