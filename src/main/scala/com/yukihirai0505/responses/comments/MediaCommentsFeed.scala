package com.yukihirai0505.responses.comments

import com.yukihirai0505.instsagram.InstagramObject
import com.yukihirai0505.responses.common.Meta

/**
  * author Yuki Hirai on 2016/11/09.
  */
case class MediaCommentsFeed(
  data: Option[List[CommentData]],
  meta: Option[Meta]
) extends InstagramObject

import play.api.libs.json.Json
object MediaCommentsFeed {
  implicit val MediaCommentsFeedFormat = Json.format[MediaCommentsFeed]
}