package com.yukihirai0505.sInstagram.responses

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json

/**
  * author Yuki Hirai on 2016/11/09.
  */
case class MediaFeed(
                      caption: Option[Caption],
                      comments: Comments,
                      createdTime: String,
                      id: String,
                      filter: Option[String],
                      images: Option[Images],
                      videos: Option[Videos],
                      carouselMedia: Option[Seq[CarouselMedia]],
                      likes: Likes,
                      link: String,
                      location: Option[MediaLocation],
                      tags: Seq[String],
                      `type`: String,
                      userHasLiked: Boolean,
                      usersInPhoto: Seq[UsersInPhoto],
                      user: User
                    )

object MediaFeed {
  implicit val MediaFeedFormat = JsonNaming.snakecase(Json.format[MediaFeed])
}
