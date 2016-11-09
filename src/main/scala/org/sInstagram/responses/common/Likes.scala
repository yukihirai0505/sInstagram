package org.sInstagram.responses.common

case class Likes(count: Int)

import play.api.libs.json.Json
object Likes {
  implicit val LikesReads = Json.reads[Likes]
  implicit val LikesWrites = Json.writes[Likes]
}
