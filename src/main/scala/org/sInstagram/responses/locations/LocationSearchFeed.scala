package org.sInstagram.responses.locations

import org.sInstagram.responses.common.Location
import play.api.libs.json.Json

/**
	* author Yuki Hirai on 2016/11/09.
	*/
case class LocationSearchFeed(
  data: Option[List[Location]]
)
object LocationSearchFeed {
	implicit val LocationSearchFeedFormat = Json.format[LocationSearchFeed]
}
