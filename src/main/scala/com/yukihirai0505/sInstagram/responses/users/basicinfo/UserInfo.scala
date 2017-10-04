package com.yukihirai0505.sInstagram.responses.users.basicinfo

import com.yukihirai0505.sInstagram.responses.common.Meta

/**
  * author Yuki Hirai on 2016/11/09.
  */
case class UserInfo(
  data: Option[UserInfoData],
  meta: Option[Meta])

import play.api.libs.json.Json
object UserInfo {
  implicit val UserInfoFormat = Json.format[UserInfo]
}