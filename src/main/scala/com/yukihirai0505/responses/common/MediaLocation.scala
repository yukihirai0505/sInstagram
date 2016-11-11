package com.yukihirai0505.responses.common

/**
  * Created by yukihirai on 2016/11/10.
  */
import play.api.libs.json.{JsValue, _}
import play.api.libs.functional.syntax._
case class MediaLocation(
  id: JsValue,
  latitude: Double,
  longitude: Double,
  name: String
)

object MediaLocation {
  implicit val mediaLocationReader: Reads[MediaLocation] = (
    (__ \ 'id).read[JsValue] ~
    (__ \ 'latitude).read[Double] ~
    (__ \ 'longitude).read[Double] ~
    (__ \ 'name).read[String]
    )(MediaLocation.apply _)
  implicit val mediaLocationFormat = Json.format[MediaLocation]
}