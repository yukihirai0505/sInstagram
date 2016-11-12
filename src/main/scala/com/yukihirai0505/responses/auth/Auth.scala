package com.yukihirai0505.responses.auth

sealed trait Auth
case class AccessToken(token: String) extends Auth
case class SignedAccessToken(token: String, clientSecret: String) extends Auth
