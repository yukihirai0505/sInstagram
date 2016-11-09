package org.sInstagram.responses.users.feed

import org.sInstagram.instsagram.InstagramObject
import org.sInstagram.responses.common.{Meta, Pagination, User}

/**
	* author Yuki Hirai on 2016/11/09.
	*/
case class UserFeed(
  data: Option[List[User]],
  pagination: Option[Pagination],
  meta: Option[Meta]
) extends InstagramObject

import play.api.libs.json.Json
object UserFeed {
	implicit val UserFeedFormat = Json.format[UserFeed]
}