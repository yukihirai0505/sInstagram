package org.sInstagram.instsagram

import java.net.URLEncoder

import com.netaporter.uri.Uri._
import dispatch._
import org.sInstagram.Authentication
import org.sInstagram.http.{Request, Response, Verbs}
import org.sInstagram.model.{Constants, Methods, QueryParam, Relationship}
import org.sInstagram.responses.auth.{Auth, SignedAccessToken}
import org.sInstagram.responses.comments.{MediaCommentResponse, MediaCommentsFeed}
import org.sInstagram.responses.common.Pagination
import org.sInstagram.responses.likes.LikesFeed
import org.sInstagram.responses.locations.{LocationInfo, LocationSearchFeed}
import org.sInstagram.responses.media.{MediaFeed, MediaInfoFeed}
import org.sInstagram.responses.relationships.RelationshipFeed
import org.sInstagram.responses.tags.{TagInfoFeed, TagSearchFeed}
import org.sInstagram.responses.users.basicinfo.UserInfo
import org.sInstagram.responses.users.feed.UserFeed
import org.sInstagram.utils.PaginationHelper
import play.api.libs.json.Reads

import scala.language.postfixOps


/**
  * author Yuki Hirai on 2016/11/09.
  */
class InstagramBase(accessToken: String) extends InstagramClient {

  protected def addSecureSigIfNeeded(auth: Auth, url: String, postData: Option[Map[String,String]] = None)
  : String = auth match {
    case SignedAccessToken(_, secret) =>
      val uri = parse(url)
      val params = uri.query.params
      val auth: Authentication = new Authentication
      val sig = auth.createSignedParam(secret, uri.pathRaw.replace("/v1", ""), concatMapOpt(postData, params.toMap))
      uri.addParam("sig", sig).toStringRaw
    case _ => url
  }

  protected def concatMapOpt(postData: Option[Map[String,String]], params: Map[String,Option[String]])
  : Map[String,Option[String]] = postData match {
    case Some(m) => params ++ m.mapValues(Some(_))
    case _ => params
  }

  def request[T](verb: Verbs, apiPath: String, params: Option[Map[String, String]] = None)(implicit r: Reads[T]): Future[Response[T]] = {
    val effectiveUrl = s"${Constants.API_URL}$apiPath?access_token=$accessToken"
    val parameters: Map[String, String] = params.getOrElse(Map())
    val request = url(effectiveUrl).setMethod(verb.label)
    val requestWithParams = if (verb.label == Verbs.GET.label) { request <<? parameters } else { request << parameters }
    println(requestWithParams.url)
    Request.send[T](requestWithParams)
  }

  override def getUserInfo(userId: String): Future[Response[UserInfo]] = {
    val apiPath: String = Methods.USERS_WITH_ID format userId
    request[UserInfo](Verbs.GET, apiPath)
  }

  override def getCurrentUserInfo: Future[Response[UserInfo]] = {
    request[UserInfo](Verbs.GET, Methods.USERS_SELF)
  }

  override def getUserRecentMedia(count: Option[Int] = None, minId: Option[String] = None, maxId: Option[String] = None): Future[Response[MediaFeed]] = {
    val params: Map[String, String] = Map(
      QueryParam.COUNT -> count.mkString,
      QueryParam.MIN_ID -> minId.mkString,
      QueryParam.MAX_ID -> maxId.mkString
    )
    request[MediaFeed](Verbs.GET, Methods.USERS_SELF_RECENT_MEDIA, Option(params))
  }

  override def getRecentMediaFeed(userId: String, count: Option[Int] = None, minId: Option[String] = None, maxId: Option[String] = None): Future[Response[MediaFeed]] = {
    val params: Map[String, String] = Map(
      QueryParam.COUNT -> count.mkString,
      QueryParam.MIN_ID -> minId.mkString,
      QueryParam.MAX_ID -> maxId.mkString
    )
    val apiPath: String = Methods.USERS_RECENT_MEDIA format userId
    request[MediaFeed](Verbs.GET, apiPath, Option(params))
  }

  override def getMediaComments(mediaId: String): Future[Response[MediaCommentsFeed]] = {
    val apiPath: String = Methods.MEDIA_COMMENTS format mediaId
    request[MediaCommentsFeed](Verbs.GET, apiPath)
  }
  override def getUserFollowList(userId: String): Future[Response[UserFeed]] = {
    getUserFollowListNextPage(userId)
  }

  override def getUserFollowListNextPage(userId: String, cursor: Option[String] = None): Future[Response[UserFeed]] = {
    val params: Map[String, String] = Map(
      QueryParam.CURSOR -> cursor.mkString
    )
    val apiPath: String = Methods.USERS_ID_FOLLOWS format userId
    request[UserFeed](Verbs.GET, apiPath, Option(params))
  }

  override def getUserFollowListNextPageByPage(pagination: Option[Pagination]): Future[Response[UserFeed]] = {
    getUserFeedInfoNextPage(pagination.get)
  }

  override def getUserLikedMediaFeed(maxLikeId: Option[Long] = None, count: Option[Int] = None): Future[Response[MediaFeed]] = {
    val params: Map[String, String] = Map(
      QueryParam.MAX_LIKE_ID -> maxLikeId.mkString,
      QueryParam.COUNT -> count.mkString
    )
    request[MediaFeed](Verbs.GET, Methods.USERS_SELF_LIKED_MEDIA, Option(params))
  }

  override def searchUser(query: String, count: Option[Int] = None): Future[Response[UserFeed]] = {
    val params: Map[String, String] = Map(
      QueryParam.SEARCH_QUERY -> query,
      QueryParam.COUNT -> count.mkString
    )
    request[UserFeed](Verbs.GET, Methods.USERS_SEARCH, Option(params))
  }

  override def getLocationInfo(locationId: String): Future[Response[LocationInfo]] = {
    val apiPath: String = Methods.LOCATIONS_BY_ID format locationId
    request[LocationInfo](Verbs.GET, apiPath)
  }

  override def getTagInfo(tagName: String): Future[Response[TagInfoFeed]] = {
    val apiPath: String = Methods.TAGS_BY_NAME format URLEncoder.encode(tagName, "UTF-8")
    request[TagInfoFeed](Verbs.GET, apiPath)
  }

  override def searchLocation(latitude: Double, longitude: Double, distance: Option[Int] = None): Future[Response[LocationSearchFeed]] = {
    val params: Map[String, String] = Map(
      QueryParam.LATITUDE -> latitude.toString,
      QueryParam.LONGITUDE -> longitude.toString,
      QueryParam.DISTANCE -> distance.getOrElse(Constants.LOCATION_DEFAULT_DISTANCE).toString
    )
    request[LocationSearchFeed](Verbs.GET, Methods.LOCATIONS_SEARCH, Option(params))
  }

  override def getRecentMediaByLocation(locationId: String, minId: Option[String] = None, maxId: Option[String] = None): Future[Response[MediaFeed]] = {
    val params: Map[String, String] = Map(
      QueryParam.MIN_ID -> minId.mkString,
      QueryParam.MAX_ID -> maxId.mkString
    )
    val apiMethod: String = Methods.LOCATIONS_RECENT_MEDIA_BY_ID format locationId
    request[MediaFeed](Verbs.GET, apiMethod, Option(params))
  }

  override def setUserLike(mediaId: String): Future[Response[LikesFeed]] = {
    val apiMethod: String = Methods.LIKES_BY_MEDIA_ID format mediaId
    request[LikesFeed](Verbs.POST, apiMethod)
  }

  override def getMediaInfo(mediaId: String): Future[Response[MediaInfoFeed]] = {
    val apiPath = Methods.MEDIA_BY_ID format mediaId
    request[MediaInfoFeed](Verbs.GET, apiPath)
  }

  override def getRecentMediaNextPage(pagination: Pagination): Future[Response[MediaFeed]] = {
    val page: PaginationHelper.Page = PaginationHelper.parseNextUrl(pagination, Constants.API_URL)
    request[MediaFeed](Verbs.GET, page.apiPath, Option(page.queryStringParams))
  }

  override def getUserFeedInfoNextPage(pagination: Pagination): Future[Response[UserFeed]] = {
    val page: PaginationHelper.Page = PaginationHelper.parseNextUrl(pagination, Constants.API_URL)
    request[UserFeed](Verbs.GET, page.apiPath, Option(page.queryStringParams))
  }

  override def deleteUserLike(mediaId: String): Future[Response[LikesFeed]] = {
    val apiPath: String = Methods.LIKES_BY_MEDIA_ID format mediaId
    request[LikesFeed](Verbs.DELETE, apiPath)
  }

  override def deleteMediaCommentById(mediaId: String, commentId: String): Future[Response[MediaCommentResponse]] = {
    val apiPath: String = Methods.DELETE_MEDIA_COMMENTS format (mediaId, commentId)
    request[MediaCommentResponse](Verbs.DELETE, apiPath)
  }

  override def getUserFollowedByList(userId: String, cursor: Option[String] = None): Future[Response[UserFeed]] = {
    val params: Map[String, String] = Map(
      QueryParam.CURSOR -> cursor.mkString
    )
    val apiPath: String = Methods.USERS_ID_FOLLOWED_BY format userId
    request[UserFeed](Verbs.GET, apiPath, Option(params))
  }

  override def getUserFollowedByListNextPage(pagination: Pagination): Future[Response[UserFeed]] = {
    getUserFeedInfoNextPage(pagination)
  }

  override def setMediaComments(mediaId: String, text: String): Future[Response[MediaCommentResponse]] = {
    val params: Map[String, String] = Map(
      QueryParam.TEXT -> text
    )
    val apiPath: String = Methods.MEDIA_COMMENTS format mediaId
    request[MediaCommentResponse](Verbs.POST, apiPath, Option(params))
  }

  override def getMediaInfoByShortCode(shortCode: String): Future[Response[MediaInfoFeed]] = {
    val apiPath: String = Methods.MEDIA_BY_SHORT_CODE format shortCode
    request[MediaInfoFeed](Verbs.GET, apiPath)
  }

  override def searchTags(tagName: String): Future[Response[TagSearchFeed]] = {
    val params: Map[String, String] = Map(
      QueryParam.SEARCH_QUERY -> tagName
    )
    request[TagSearchFeed](Verbs.GET, Methods.TAGS_SEARCH, Option(params))
  }

  override def getRecentMediaFeedTags(tagName: String, minTagId: Option[String] = None, maxTagId: Option[String] = None, count: Option[Long] = None): Future[Response[MediaFeed]] = {
    val apiPath: String = Methods.TAGS_RECENT_MEDIA format tagName
    val params: Map[String, String] = Map(
      QueryParam.MIN_TAG_ID -> minTagId.mkString,
      QueryParam.MAX_TAG_ID -> maxTagId.mkString,
      QueryParam.COUNT -> count.mkString
    )
    request[MediaFeed](Verbs.GET, apiPath, Option(params))
  }

  override def setUserRelationship(userId: String, relationship: Relationship): Future[Response[RelationshipFeed]] = {
    val params: Map[String, String] = Map(
      QueryParam.ACTION -> relationship.value
    )
    val apiPath: String = Methods.USERS_ID_RELATIONSHIP format userId
    request[RelationshipFeed](Verbs.POST, apiPath, Option(params))
  }

  override def getUserRequestedBy: Future[Response[UserFeed]] = {
    request[UserFeed](Verbs.GET, Methods.USERS_SELF_REQUESTED_BY)
  }

  override def getUserRelationship(userId: String): Future[Response[RelationshipFeed]] = {
    val apiPath: String = Methods.USERS_ID_RELATIONSHIP format userId
    request[RelationshipFeed](Verbs.GET, apiPath)
  }

  override def searchFacebookPlace(facebookPlacesId: String): Future[Response[LocationSearchFeed]] = {
    val params: Map[String, String] = Map(
      QueryParam.FACEBOOK_PLACES_ID -> facebookPlacesId
    )
    request[LocationSearchFeed](Verbs.GET, Methods.LOCATIONS_SEARCH, Option(params))
  }

  override def searchMedia(latitude: Double, longitude:Double, distance: Option[Int] = None): Future[Response[MediaFeed]] = {
    val params: Map[String, String] = Map(
      QueryParam.LATITUDE -> latitude.toString,
      QueryParam.LONGITUDE -> longitude.toString,
      QueryParam.DISTANCE -> distance.mkString
    )
    request[MediaFeed](Verbs.GET, Methods.MEDIA_SEARCH, Option(params))
  }

  override def getUserLikes(mediaId: String): Future[Response[LikesFeed]] = {
    val apiPath: String = Methods.LIKES_BY_MEDIA_ID format mediaId
    request[LikesFeed](Verbs.GET, apiPath)
  }
}