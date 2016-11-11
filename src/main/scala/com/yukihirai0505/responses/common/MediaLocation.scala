package com.yukihirai0505.responses.common

/**
  * Created by yukihirai on 2016/11/10.
  */

case class MediaLocation(
  id: Option[Int],
  latitude: Option[Double],
  longitude: Option[Double],
  name: Option[String]
)

import play.api.libs.json.Json
object MediaLocation {
  implicit val MediaLocationFormat = Json.format[MediaLocation]
}