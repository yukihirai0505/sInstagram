package com.yukihirai0505.sInstagram.responses.auth

case class Oauth(accessToken: String)

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.json.Json
object Oauth {
  implicit val OauthFormat = JsonNaming.snakecase(Json.format[Oauth])
}