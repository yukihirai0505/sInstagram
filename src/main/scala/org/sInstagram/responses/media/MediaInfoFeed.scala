package org.sInstagram.responses.media

import org.sInstagram.instsagram.InstagramObject
import org.sInstagram.responses.common.Meta

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