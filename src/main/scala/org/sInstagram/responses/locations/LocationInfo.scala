package org.sInstagram.responses.locations

import org.sInstagram.responses.common.Location

/**
	* author Yuki Hirai on 2016/11/09.
	*/
case class LocationInfo(
  data: Option[Location]
)


import play.api.libs.json.Json
object LocationInfo {
	implicit val LocationInfoFormat = Json.format[LocationInfo]
}

