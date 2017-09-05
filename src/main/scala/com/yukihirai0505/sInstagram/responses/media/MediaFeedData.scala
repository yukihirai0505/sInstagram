package com.yukihirai0505.sInstagram.responses.media

import com.github.tototoshi.play.json.JsonNaming
import com.yukihirai0505.sInstagram.responses.common._
import play.api.libs.json.Json

/**
  * author Yuki Hirai on 2016/11/09.
  */
case class MediaFeedData(
  caption: Option[Caption],
  comments: Option[Comments],
  createdTime: Option[String],
  id: Option[String],
  filter: Option[String],
  images: Option[Images],
  videos: Option[Videos],
  likes: Option[Likes],
  link: Option[String],
  location: Option[MediaLocation],
  tags: Option[List[String]],
  user: Option[User]
)
object MediaFeedData {
  implicit val MediaFeedFormat = JsonNaming.snakecase(Json.format[MediaFeedData])
}
