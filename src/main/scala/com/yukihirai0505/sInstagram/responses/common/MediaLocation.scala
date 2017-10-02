package com.yukihirai0505.sInstagram.responses.common

/**
  * Created by yukihirai on 2016/11/10.
  */

import play.api.libs.json._

case class MediaLocation(
                          id: Option[Int],
                          latitude: Option[Double],
                          longitude: Option[Double],
                          name: Option[String]
                        )

object MediaLocation {
  implicit val mediaLocationFormat = Json.format[MediaLocation]
}