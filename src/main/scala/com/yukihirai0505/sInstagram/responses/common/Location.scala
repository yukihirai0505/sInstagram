package com.yukihirai0505.sInstagram.responses.common

case class Location(
  id: String,
  latitude: Double,
  longitude: Double,
  name: String
)

import play.api.libs.json.Json
object Location {
  implicit val LocationFormat = Json.format[Location]
}
