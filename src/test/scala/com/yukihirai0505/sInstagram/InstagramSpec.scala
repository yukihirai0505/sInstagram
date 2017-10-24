package com.yukihirai0505.sInstagram

import com.yukihirai0505.com.scala.model.Response
import com.yukihirai0505.sInstagram.model.Relationship
import com.yukihirai0505.sInstagram.responses.auth.AccessToken
import com.yukihirai0505.sInstagram.responses.{CommentData, LikeUserInfo, MediaFeed, RelationshipFeed, UserInfo, _}
import org.scalatest.matchers.{BePropertyMatchResult, BePropertyMatcher}
import org.scalatest.{FlatSpec, Matchers}
import play.api.libs.iteratee.Execution

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContextExecutor}
import scala.io.Source
import scala.language.postfixOps
import scala.util.Try

/**
  * author Yuki Hirai on 2017/04/14.
  */
class InstagramSpec extends FlatSpec with Matchers {

  private def anInstanceOf[T](implicit tag: reflect.ClassTag[T]) = {
    val clazz = tag.runtimeClass.asInstanceOf[Class[T]]
    new BePropertyMatcher[AnyRef] {
      def apply(left: AnyRef) =
        BePropertyMatchResult(left.getClass.isAssignableFrom(clazz), "an instance of " + clazz.getName)
    }
  }

  private def readAccessTokenFromConfig(): AccessToken = {
    sys.env.get("INSTAGRAM_TEST_ACCESS_TOKEN") match {
      case Some(v) => AccessToken(v)
      case _ =>
        Try {
          val tokenFile = Source.fromURL(getClass.getResource("/token.txt")).mkString
          AccessToken(tokenFile.split("=").toList(1))
        }.getOrElse(throw new Exception(
          "Please provide a valid access_token by making a token.txt in resources.\n" +
            "See token.txt.default for detail."
        ))
    }
  }

  val auth: AccessToken = readAccessTokenFromConfig()
  val instagram = new Instagram(auth)
  val wrongToken = AccessToken("this is a bullshit access token")
  val nonePage = new Pagination(None, None, None, None, None, None)
  implicit val executionContextExecutor: ExecutionContextExecutor = Execution.trampoline

  var userId: Option[String] = None
  var mediaId: Option[String] = None
  var locationId: Option[String] = None
  var latitude: Option[Double] = None
  var longitude: Option[Double] = None
  var mediaCommentId: Option[String] = None

  "getCurrentUserInfo" should "return a Some[UserInfo]" in {
    val request = Await.result(instagram.getUserInfo(), 10 seconds)
    userId = request.data.map(_.data.id)
    request should be(anInstanceOf[Response[Data[UserInfo]]])
  }


  "A failed request" should "return a failed promise" in {
    an[Exception] should be thrownBy Await.result(new Instagram(wrongToken).getUserInfo(userId), 10 seconds)
  }

  "getRecentMediaFeed" should "return a Some[MediaFeed]" in {
    val request = Await.result(instagram.getRecentMediaFeed(), 10 seconds)
    mediaId = request.data.flatMap(_.data.lastOption.flatMap(x => Some(x.id)))
    locationId = request.data.flatMap(_.data.lastOption.flatMap(_.location.flatMap(x => Some(x.id.getOrElse("").toString)))
    )
    request should be(anInstanceOf[Response[DataWithPage[MediaFeed]]])
  }

  "getUserFollowList" should "return a Some[UserFeed]" in {
    val request = Await.result(instagram.getFollowList(), 10 seconds)
    request should be(anInstanceOf[Response[Data[User]]])
  }

  "getUserAllFollowsList" should "return Seq[User]" in {
    val request = Await.result(instagram.getAllFollowsList, 10 seconds)
    if (request.isEmpty) request.isEmpty should be(true)
    else request should be(anInstanceOf[Seq[User]])
  }

  "getUserLikedMediaFeed" should "return a Some[MediaFeed]" in {
    val request = Await.result(instagram.getUserLikedMediaFeed(), 10 seconds)
    request should be(anInstanceOf[Response[DataWithPage[MediaFeed]]])
  }

  "searchUser" should "return a Some[UserFeed]" in {
    val request = Await.result(instagram.searchUser("i_do_not_like_fashion"), 10 seconds)
    request should be(anInstanceOf[Response[Data[Seq[User]]]])
  }

  "getLocationInfo" should "return a Some[LocationInfo]" in {
    val request = Await.result(instagram.getLocationInfo(locationId.getOrElse("")), 10 seconds)
    latitude = request.data.flatMap(_.data.latitude)
    longitude = request.data.flatMap(_.data.longitude)
    request should be(anInstanceOf[Response[Data[Location]]])
  }

  "getTagInfo200" should "return a Some[TagInfoFeed]" in {
    val request = Await.result(instagram.getTagInfo("test"), 10 seconds)
    request should be(anInstanceOf[Response[Data[TagInfoFeed]]])
  }

  "getTagInfo400" should "return a Some[TagInfoFeed]" in {
    // "lolita" will be 400 This tag cannot be viewed
    an[Exception] should be thrownBy Await.result(instagram.getTagInfo("lolita"), 10 seconds)
  }

  "searchLocation" should "return a Some[LocationSearchFeed]" in {
    val request = Await.result(instagram.searchLocation(latitude.getOrElse(0), longitude.getOrElse(0)), 10 seconds)
    request should be(anInstanceOf[Response[Data[Seq[Location]]]])
  }

  "getRecentMediaByLocation" should "return a Some[MediaFeed]" in {
    val request = Await.result(instagram.getLocationRecentMedia(locationId.getOrElse("")), 10 seconds)
    request should be(anInstanceOf[Response[DataWithPage[MediaFeed]]])
  }

  "setUserLike" should "return a Some[LikesFeed]" in {
    val request = Await.result(instagram.setUserLike(mediaId.getOrElse("")), 10 seconds)
    request should be(anInstanceOf[Response[Data[LikeUserInfo]]])
  }

  "getMediaInfo" should "return a Some[MediaInfoFeed]" in {
    val request = Await.result(instagram.getMediaInfo(mediaId.getOrElse("")), 10 seconds)
    request should be(anInstanceOf[Response[Data[MediaFeed]]])
  }

  "deleteUserLike" should "return a Some[NoDataResponse]" in {
    val request = Await.result(instagram.deleteUserLike(mediaId.getOrElse("")), 10 seconds)
    request should be(anInstanceOf[Response[NoDataResponse]])
  }

  "setMediaComments" should "return a Some[NoDataResponse]" in {
    val request = Await.result(instagram.setMediaComments(mediaId.getOrElse(""), "test"), 10 seconds)
    request should be(anInstanceOf[Response[NoDataResponse]])
  }

  "getMediaComments" should "return a Some[MediaCommentsFeed]" in {
    val request = Await.result(instagram.getMediaComments(mediaId.getOrElse("")), 10 seconds)
    mediaCommentId = request.data.flatMap(_.data.lastOption).flatMap(x => Some(x.id))
    request should be(anInstanceOf[Response[Data[Seq[CommentData]]]])
  }

  "deleteMediaCommentById" should "return a Some[NoDataResponse]" in {
    val request = Await.result(instagram.deleteMediaCommentById(mediaId.getOrElse(""), mediaCommentId.getOrElse("")), 10 seconds)
    request should be(anInstanceOf[Response[NoDataResponse]])
  }

  "getUserFollowedByList" should "return a Some[UserFeed]" in {
    val request = Await.result(instagram.getFollowedByList(), 10 seconds)
    request should be(anInstanceOf[Response[DataWithPage[User]]])
  }

  "getUserAllFollowersList" should "return Seq[User]]" in {
    val request = Await.result(instagram.getAllFollowersList, 10 seconds)
    if (request.isEmpty) request.isEmpty should be(true)
    else request should be(anInstanceOf[Seq[User]])
  }

  "getMediaInfoByShortCode" should "return a Some[MediaInfoFeed]" in {
    val request = Await.result(instagram.getMediaInfoByShortcode("BZfmV4RFklR"), 10 seconds)
    request should be(anInstanceOf[Response[Data[MediaFeed]]])
  }

  "searchTags" should "return a Some[TagSearchFeed]" in {
    val request = Await.result(instagram.searchTags("test"), 10 seconds)
    request should be(anInstanceOf[Response[Data[Seq[TagInfoFeed]]]])
  }

  "getRecentMediaFeedTags" should "return a Some[MediaFeed]" in {
    val request = Await.result(instagram.getTagsRecentMedia("test"), 10 seconds)
    request should be(anInstanceOf[Response[DataWithPage[MediaFeed]]])
  }

  "setUserRelationship" should "return a Some[RelationshipFeed]" in {
    val request = Await.result(instagram.setUserRelationship(userId.getOrElse(""), Relationship.FOLLOW), 10 seconds)
    request should be(anInstanceOf[Response[Data[RelationshipFeed]]])
  }

  "getUserRequestedBy" should "return a Some[UserFeed]" in {
    val request = Await.result(instagram.getUserRequestedBy, 10 seconds)
    request should be(anInstanceOf[Response[DataWithPage[User]]])
  }

  "getUserRelationship" should "return a Some[RelationshipFeed]" in {
    val request = Await.result(instagram.getUserRelationship(userId.getOrElse("")), 10 seconds)
    request should be(anInstanceOf[Response[Data[RelationshipFeed]]])
  }

  "searchFacebookPlace" should "return a Some[LocationSearchFeed]" in {
    val request = Await.result(instagram.searchFacebookPlace(locationId.getOrElse("")), 10 seconds)
    request should be(anInstanceOf[Response[Data[Seq[Location]]]])
  }

  "searchMedia" should "return a Some[MediaFeed]" in {
    val request = Await.result(instagram.searchMedia(latitude.getOrElse(0), longitude.getOrElse(0)), 10 seconds)
    request should be(anInstanceOf[Response[Data[Seq[MediaFeed]]]])
  }

  "getUserLikes" should "return a Some[LikesFeed]" in {
    val request = Await.result(instagram.getUserLikes(mediaId.getOrElse("")), 10 seconds)
    request should be(anInstanceOf[Response[Data[Seq[LikeUserInfo]]]])
  }

}
