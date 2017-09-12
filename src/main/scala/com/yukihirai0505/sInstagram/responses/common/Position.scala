package com.yukihirai0505.sInstagram.responses.common

/**
  * author Yuki Hirai on 2017/09/12.
  */
case class Position(
                     x: Double,
                     y: Double
                   )

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json

object Position {
  implicit val PositionFormat = JsonNaming.snakecase(Json.format[Position])
}