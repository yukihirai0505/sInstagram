package com.yukihirai0505.sInstagram.responses.locations

import com.yukihirai0505.sInstagram.responses.common.{Location, Meta}
import play.api.libs.json.Json

/**
  * author Yuki Hirai on 2016/11/09.
  */
case class LocationSearchFeed(
  data: Option[Seq[Location]],
  meta: Option[Meta]
)
object LocationSearchFeed {
  implicit val LocationSearchFeedFormat = Json.format[LocationSearchFeed]
}
