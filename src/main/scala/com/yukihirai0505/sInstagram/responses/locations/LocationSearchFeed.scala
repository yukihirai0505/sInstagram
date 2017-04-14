package com.yukihirai0505.sInstagram.responses.locations

import com.yukihirai0505.sInstagram.responses.common.Location
import play.api.libs.json.Json

/**
  * author Yuki Hirai on 2016/11/09.
  */
case class LocationSearchFeed(
  data: List[Location]
)
object LocationSearchFeed {
  implicit val LocationSearchFeedFormat = Json.format[LocationSearchFeed]
}
