package com.yukihirai0505.responses.likes

import com.yukihirai0505.instsagram.InstagramObject
import com.yukihirai0505.responses.common.{Meta, User}

/**
  * author Yuki Hirai on 2016/11/09.
  */
case class LikesFeed(
  data: Option[List[User]],
  meta: Option[Meta]
) extends InstagramObject

import play.api.libs.json.Json
object LikesFeed {
  implicit val LikesFeedFormat = Json.format[LikesFeed]
}