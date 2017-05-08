package com.yukihirai0505.sInstagram

import com.yukihirai0505.sInstagram.model.Scope
import org.scalatest.{FlatSpec, Matchers}

import scala.language.postfixOps

/**
  * author Yuki Hirai on 2017/05/08.
  */
class InstagramAuthSpec extends FlatSpec with Matchers {

  val instagramAuth = new InstagramAuth

  "authUrl" should "return auth url" in {
    val scopes: Seq[Scope] = Seq(Scope.BASIC)
    val authUrl = instagramAuth.authURL(scopes = scopes)
    assert(authUrl.nonEmpty)
  }
}
