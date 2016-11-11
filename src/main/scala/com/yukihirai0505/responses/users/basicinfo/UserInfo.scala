package com.yukihirai0505.responses.users.basicinfo

import com.yukihirai0505.instsagram.InstagramObject
import com.yukihirai0505.responses.common.Meta

/**
  * author Yuki Hirai on 2016/11/09.
  */
case class UserInfo(
  data: Option[UserInfoData],
  meta: Option[Meta]) extends InstagramObject

import play.api.libs.json.Json
object UserInfo {
  implicit val UserInfoFormat = Json.format[UserInfo]
}