package com.yukihirai0505.sInstagram.responses.common

import play.api.libs.json.Json

/**
  * author Yuki Hirai on 2016/11/09.
  */
case class NoDataResponse(
  meta: Option[Meta]
)



object NoDataResponse {
  implicit val NodataResponseFormat = Json.format[NoDataResponse]
}