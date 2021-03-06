package com.yukihirai0505.sInstagram.model

/**
  * project constant
  *
  * author Yuki Hirai on 2016/11/08.
  */
object Constants {
  private val BASE_URL: String = "https://api.instagram.com"
  private val OAUTH_BASE_URL: String = s"$BASE_URL/oauth"
  val VERSION: String = "/v1"

  val ACCESS_TOKEN_ENDPOINT: String = s"$OAUTH_BASE_URL/access_token"
  val AUTHORIZE_URL: String = s"$OAUTH_BASE_URL/authorize/?client_id=%s&redirect_uri=%s&response_type=%s"

  val API_URL: String = s"$BASE_URL$VERSION"

  val LOCATION_DEFAULT_DISTANCE = "500"
}
