package com.yukihirai0505.responses.common

case class Pagination(
  deprecationWarning: Option[String],
  minTagId: Option[String],
  nextMaxId: Option[String],
  nextMaxTagId: Option[String],
  nextUrl: Option[String],
  nextCursor: Option[String]
)

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json
object Pagination {
  implicit val PaginationFormat = JsonNaming.snakecase(Json.format[Pagination])
}
