package com.yukihirai0505.sInstagram.responses.auth

import com.yukihirai0505.sInstagram.utils.Configurations.clientSecret

sealed trait Auth

case class AccessToken(token: String) extends Auth

case class SignedAccessToken(token: String, clientSecret: String = clientSecret) extends Auth
