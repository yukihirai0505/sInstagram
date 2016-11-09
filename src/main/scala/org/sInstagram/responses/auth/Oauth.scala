package org.sInstagram.responses.auth

import org.sInstagram.responses.common.User

case class Oauth(access_token: String, user: User) {
  lazy val accessToken = access_token
}

import play.api.libs.json.Json
object Oauth {
  implicit val OauthReads = Json.reads[Oauth]
  implicit val OauthWrites = Json.writes[Oauth]
}