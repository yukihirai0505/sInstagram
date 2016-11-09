package org.sInstagram.responses.users.basicinfo

/**
	* author Yuki Hirai on 2016/11/09.
	*/
case class Counts(
  follows: Option[Int],
	followedBy: Option[Int],
  media: Option[Int]
)

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json
object Counts {
	implicit val CountsFormat = JsonNaming.snakecase(Json.format[Counts])
}
