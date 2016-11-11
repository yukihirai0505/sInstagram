package com.yukihirai0505.responses.locations

import com.yukihirai0505.responses.common.Location
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
