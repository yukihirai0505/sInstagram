package org.sInstagram.responses.common

case class Location(
  id: Option[String],
  latitude: Option[Double],
  longitude: Option[Double],
  name: Option[String]
)

import play.api.libs.json.Json
object Location {
  implicit val LocationFormat = Json.format[Location]
}
