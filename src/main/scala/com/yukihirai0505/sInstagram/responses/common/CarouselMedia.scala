package com.yukihirai0505.sInstagram.responses.common

/**
  * author Yuki Hirai on 2017/09/12.
  */
case class CarouselMedia(
                          images: Option[Images],
                          videos: Option[Videos],
                          usersInPhoto: Seq[UsersInPhoto],
                          `type`: String
                        )

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json

object CarouselMedia {
  implicit val CarouselMediaFormat = JsonNaming.snakecase(Json.format[CarouselMedia])
}
