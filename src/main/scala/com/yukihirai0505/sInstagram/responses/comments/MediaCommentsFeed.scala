package com.yukihirai0505.sInstagram.responses.comments

import com.yukihirai0505.sInstagram.responses.common.Meta

/**
  * author Yuki Hirai on 2016/11/09.
  */
case class MediaCommentsFeed(
                              data: Seq[CommentData],
                              meta: Option[Meta]
                            )

import play.api.libs.json.Json

object MediaCommentsFeed {
  implicit val MediaCommentsFeedFormat = Json.format[MediaCommentsFeed]
}