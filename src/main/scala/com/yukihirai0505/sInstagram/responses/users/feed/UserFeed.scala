package com.yukihirai0505.sInstagram.responses.users.feed

import com.yukihirai0505.sInstagram.responses.common.{Meta, Pagination, User}

/**
  * author Yuki Hirai on 2016/11/09.
  */
case class UserFeed(
  data: List[User],
  pagination: Option[Pagination],
  meta: Option[Meta]
)

import play.api.libs.json.Json
object UserFeed {
  implicit val UserFeedFormat = Json.format[UserFeed]
}