package com.yukihirai0505.sInstagram.responses.auth

sealed trait Auth
case class AccessToken(token: String) extends Auth
case class SignedAccessToken(token: String, clientSecret: String) extends Auth
