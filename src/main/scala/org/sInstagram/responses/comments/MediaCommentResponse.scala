package org.sInstagram.responses.comments

import org.sInstagram.instsagram.InstagramObject
import org.sInstagram.responses.common.Meta
import play.api.libs.json.Json

/**
	* author Yuki Hirai on 2016/11/09.
	*/
case class MediaCommentResponse(
  commentData: Option[CommentData],
  meta: Option[Meta]
) extends InstagramObject

object MediaCommentResponse {
	implicit val MediaCommentResponseFormat = Json.format[MediaCommentResponse]
}
