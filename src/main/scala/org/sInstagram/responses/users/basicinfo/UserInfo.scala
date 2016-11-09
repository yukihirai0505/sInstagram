package org.sInstagram.responses.users.basicinfo

import org.sInstagram.instsagram.InstagramObject
import org.sInstagram.responses.common.Meta

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