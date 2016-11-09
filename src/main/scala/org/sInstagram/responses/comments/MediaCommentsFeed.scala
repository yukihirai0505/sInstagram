package org.sInstagram.responses.comments

import org.sInstagram.instsagram.InstagramObject
import org.sInstagram.responses.common.Meta

/**
	* author Yuki Hirai on 2016/11/09.
	*/
case class MediaCommentsFeed(
  data: Option[CommentData],
	meta: Option[Meta]
) extends InstagramObject

import play.api.libs.json.Json
object MediaCommentsFeed {
	implicit val MediaCommentsFeedFormat = Json.format[MediaCommentsFeed]
}


