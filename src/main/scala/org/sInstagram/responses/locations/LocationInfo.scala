package org.sInstagram.responses.locations

import org.sInstagram.responses.common.{Location, Meta}

/**
  * author Yuki Hirai on 2016/11/09.
  */
case class LocationInfo(
  data: Option[Location],
  meta: Option[Meta]
)


import play.api.libs.json.Json
object LocationInfo {
  implicit val LocationInfoFormat = Json.format[LocationInfo]
}

