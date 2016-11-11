package com.yukihirai0505.responses.users.feed

import com.yukihirai0505.instsagram.InstagramObject
import com.yukihirai0505.responses.common.{Meta, Pagination, User}

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