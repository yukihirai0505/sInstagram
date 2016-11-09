package org.sInstagram.responses.common

case class Pagination(
    next_url: Option[String],
    next_max_id: Option[String]
) {
  lazy val nextURL = next_url
  lazy val nextMaxId = next_max_id
}

import play.api.libs.json.Json
object Pagination {
  implicit val PaginationReads = Json.reads[Pagination]
  implicit val PaginationWrites = Json.writes[Pagination]
}
