package com.yukihirai0505.sInstagram

import com.yukihirai0505.sInstagram.responses.auth.AccessToken
import com.yukihirai0505.sInstagram.responses.users.basicinfo.UserInfo
import org.scalatest.matchers.{BePropertyMatchResult, BePropertyMatcher}
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.io.Source
import scala.language.postfixOps
import scala.util.Try

/**
  * author Yuki Hirai on 2017/04/14.
  */
class InstagramSpec extends FlatSpec with Matchers {

  private def anInstanceOf[T](implicit tag : reflect.ClassTag[T]) = {
    val clazz = tag.runtimeClass.asInstanceOf[Class[T]]
    new BePropertyMatcher[AnyRef] { def apply(left: AnyRef) =
      BePropertyMatchResult(left.getClass.isAssignableFrom(clazz), "an instance of " + clazz.getName) }
  }

  private def readAccessTokenFromConfig(): AccessToken = {
    Try {
      val tokenFile = Source.fromURL(getClass.getResource("/token.txt")).mkString
      AccessToken(tokenFile.split("=").toList(1))
    }.getOrElse(throw new Exception(
      "Please provide a valid access_token by making a token.txt in resources.\n" +
        "See token.txt.default for detail."
    ))
  }

  val auth: AccessToken = readAccessTokenFromConfig()
  val instagram = new Instagram(auth)
  val instagramTestUserId = "4856871817"
  val wrongToken = AccessToken("this is a bullshit access token")

  "A failed request" should "return a failed promise" in {
    an [Exception] should be thrownBy Await.result(new Instagram(wrongToken).getUserInfo(instagramTestUserId), 10 seconds)
  }

  "userInfo" should "return a Some[UserInfo]" in {
    val request = Await.result(instagram.getCurrentUserInfo, 10 seconds)
    request should be (anInstanceOf[Some[UserInfo]])
    request.get.data.id.get should be (instagramTestUserId)
  }

}
