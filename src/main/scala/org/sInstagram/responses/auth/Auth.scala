package org.sInstagram.responses.auth

sealed trait Auth
case class ClientId(id: String) extends Auth
case class AccessToken(token: String) extends Auth
case class SignedAccessToken(token: String, clientSecret: String) extends Auth
