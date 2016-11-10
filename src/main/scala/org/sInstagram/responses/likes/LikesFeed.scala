package org.sInstagram.responses.likes

import org.sInstagram.instsagram.InstagramObject
import org.sInstagram.responses.common.{Meta, User}

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