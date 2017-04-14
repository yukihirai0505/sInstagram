package com.yukihirai0505.sInstagram

import java.net.URLEncoder

import play.api.libs.json.Reads

import com.netaporter.uri.Uri._
import com.yukihirai0505.sInstagram.http.{Request, Verbs}
import com.yukihirai0505.sInstagram.model.{Constants, Methods, OAuthConstants, QueryParam, Relationship}
import com.yukihirai0505.sInstagram.responses.auth.{AccessToken, Auth, SignedAccessToken}
import com.yukihirai0505.sInstagram.responses.comments.{MediaCommentResponse, MediaCommentsFeed}
import com.yukihirai0505.sInstagram.responses.common.Pagination
import com.yukihirai0505.sInstagram.responses.likes.LikesFeed
import com.yukihirai0505.sInstagram.responses.locations.{LocationInfo, LocationSearchFeed}
import com.yukihirai0505.sInstagram.responses.media.{MediaFeed, MediaInfoFeed}
import com.yukihirai0505.sInstagram.responses.relationships.RelationshipFeed
import com.yukihirai0505.sInstagram.responses.tags.{TagInfoFeed, TagSearchFeed}
import com.yukihirai0505.sInstagram.responses.users.basicinfo.UserInfo
import com.yukihirai0505.sInstagram.responses.users.feed.UserFeed
import com.yukihirai0505.sInstagram.utils.PaginationHelper
import dispatch._

import scala.language.postfixOps


/**
  * author Yuki Hirai on 2016/11/09.
  */
class Instagram(auth: Auth) {

  /**
    * Transform an Authentication type to be used in a URL.
    *
    * @param a Authentication
    * @return  String
    */
  protected def authToGETParams(a: Auth): String = a match {
    case AccessToken(token) => s"${OAuthConstants.ACCESS_TOKEN}=$token"
    case SignedAccessToken(token, _) => s"${OAuthConstants.ACCESS_TOKEN}=$token"
  }

  /***
    * Add sign for secure request
    * @param url
    * @param postData
    * @return
    */
  protected def addSecureSigIfNeeded(url: String, postData: Option[Map[String,String]] = None)
  : String = auth match {
    case SignedAccessToken(_, secret) =>
      val uri = parse(url)
      val params = uri.query.params
      val auth: InstagramAuth = new InstagramAuth
      val sig = auth.createSignedParam(
        secret,
        uri.pathRaw.replace(Constants.VERSION, ""),
        concatMapOpt(postData, params.toMap)
      )
      uri.addParam(QueryParam.SIGNATURE, sig).toStringRaw
    case _ => url
  }

  /***
    * concat map option
    * @param postData
    * @param params
    * @return
    */
  protected def concatMapOpt(postData: Option[Map[String,String]], params: Map[String,Option[String]])
  : Map[String,Option[String]] = postData match {
    case Some(m) => params ++ m.mapValues(Some(_))
    case _ => params
  }

  /***
    * Request instagram api method
    *
    * @param verb
    * @param apiPath
    * @param params
    * @param r
    * @tparam T
    * @return
    */
  def request[T](verb: Verbs, apiPath: String, params: Option[Map[String, Option[String]]] = None)(implicit r: Reads[T]): Future[Option[T]] = {
    val parameters: Map[String, String] = params match {
      case Some(m) => m.filter(_._2.isDefined).mapValues(_.getOrElse("")).filter(!_._2.isEmpty)
      case None => Map.empty
    }
    val accessTokenUrl = s"${Constants.API_URL}$apiPath?${authToGETParams(auth)}"
    val effectiveUrl: String = verb match {
      case Verbs.GET => addSecureSigIfNeeded(accessTokenUrl)
      case _ => addSecureSigIfNeeded(accessTokenUrl, Some(parameters))
    }
    val request: Req = url(effectiveUrl).setMethod(verb.label)
    val requestWithParams = if (verb.label == Verbs.GET.label) { request <<? parameters } else { request << parameters }
    println(requestWithParams.url)
    Request.send[T](requestWithParams)
  }

  /**
    * Get basic information about a user.
    *
    * @param userId
    * user-id
    * @return a MediaFeed object.
    * if any error occurs.
    */
  def getUserInfo(userId: String): Future[Option[UserInfo]] = {
    val apiPath: String = Methods.USERS_WITH_ID format userId
    request[UserInfo](Verbs.GET, apiPath)
  }

  /**
    * Get basic information about a user.
    *
    * @return a UserInfo object.
    * if any error occurs.
    */
  def getCurrentUserInfo: Future[Option[UserInfo]] = {
    request[UserInfo](Verbs.GET, Methods.USERS_SELF)
  }

  /**
    * Get the most recent media published by a user.
    *
    * @param userId
    * @param count
    * @param minId
    * @param maxId
    * @return the mediaFeed object
    * if any error occurs
    */
  def getRecentMediaFeed(userId: Option[String] = None, count: Option[Int] = None, minId: Option[String] = None, maxId: Option[String] = None): Future[Option[MediaFeed]] = {
    val params: Map[String, Option[String]] = Map(
      QueryParam.COUNT -> Option(count.mkString),
      QueryParam.MIN_ID -> Option(minId.mkString),
      QueryParam.MAX_ID -> Option(maxId.mkString)
    )
    val apiPath: String = userId match {
      case Some(id) => Methods.USERS_RECENT_MEDIA format id
      case None => Methods.USERS_SELF_RECENT_MEDIA
    }
    request[MediaFeed](Verbs.GET, apiPath, Some(params))
  }

  /**
    * Get a full list of comments on a media.
    *
    * @param mediaId
    * a mediaId
    * @return a MediaCommentsFeed object.
    * if any error occurs.
    */
  def getMediaComments(mediaId: String): Future[Option[MediaCommentsFeed]] = {
    val apiPath: String = Methods.MEDIA_COMMENTS format mediaId
    request[MediaCommentsFeed](Verbs.GET, apiPath)
  }

  /**
    * Get the list of 'users' the authenticated user follows.
    *
    * @param userId
    * userId of the User.
    * @return a UserFeed object.
    * if any error occurs.
    */
  def getUserFollowList(userId: String): Future[Option[UserFeed]] = {
    getUserFollowListNextPage(userId)
  }

  /**
    * Get the next page for list of 'users' the authenticated user follows.
    *
    */
  def getUserFollowListNextPage(userId: String, cursor: Option[String] = None): Future[Option[UserFeed]] = {
    val params: Map[String, Option[String]] = Map(
      QueryParam.CURSOR -> Option(cursor.mkString)
    )
    val apiPath: String = Methods.USERS_ID_FOLLOWS format userId
    request[UserFeed](Verbs.GET, apiPath, Some(params))
  }

  /**
    * Get the next page for list of 'users' the authenticated user follows.
    *
    * @param pagination
    */
  def getUserFollowListNextPageByPage(pagination: Pagination): Future[Option[UserFeed]] = {
    getUserFeedInfoNextPage(pagination)
  }

  /**
    * Get the authenticated user's list of media they've liked.
    *
    * @return a MediaFeed object.
    * if any error occurs.
    */
  def getUserLikedMediaFeed(maxLikeId: Option[Long] = None, count: Option[Int] = None): Future[Option[MediaFeed]] = {
    val params: Map[String, Option[String]] = Map(
      QueryParam.MAX_LIKE_ID -> Option(maxLikeId.mkString),
      QueryParam.COUNT -> Option(count.mkString)
    )
    request[MediaFeed](Verbs.GET, Methods.USERS_SELF_LIKED_MEDIA, Some(params))
  }

  /**
    * Search for a user by name.
    *
    * @param query
    * A query string.
    * @return a UserFeed object.
    * if any error occurs.
    */
  def searchUser(query: String, count: Option[Int] = None): Future[Option[UserFeed]] = {
    val params: Map[String, Option[String]] = Map(
      QueryParam.SEARCH_QUERY -> Some(query),
      QueryParam.COUNT -> Option(count.mkString)
    )
    request[UserFeed](Verbs.GET, Methods.USERS_SEARCH, Some(params))
  }

  /**
    * Get information about a location.
    *
    * @param locationId
    * an id of the Location
    * @return a LocationInfo object.
    * if any error occurs.
    */
  def getLocationInfo(locationId: String): Future[Option[LocationInfo]] = {
    val apiPath: String = Methods.LOCATIONS_BY_ID format locationId
    request[LocationInfo](Verbs.GET, apiPath)
  }

  /**
    * Get information about a tag object.
    *
    * @param tagName
    * name of the tag.
    * @return a TagInfoFeed object.
    * if any error occurs.
    */
  def getTagInfo(tagName: String): Future[Option[TagInfoFeed]] = {
    val apiPath: String = Methods.TAGS_BY_NAME format URLEncoder.encode(tagName, "UTF-8")
    request[TagInfoFeed](Verbs.GET, apiPath)
  }

  /**
    * Search for a location by geographic coordinate.
    *
    * @param latitude
    * Latitude of the center search coordinate.
    * @param longitude
    * Longitude of the center search coordinate.
    * @param distance
    * Default is 1000m (distance=1000), max distance is 5000.
    * @return a LocationSearchFeed object.
    * if any error occurs.
    */
  def searchLocation(latitude: Double, longitude: Double, distance: Option[Int] = None): Future[Option[LocationSearchFeed]] = {
    val params: Map[String, Option[String]] = Map(
      QueryParam.LATITUDE -> Some(latitude.toString),
      QueryParam.LONGITUDE -> Some(longitude.toString),
      QueryParam.DISTANCE -> Some(distance.getOrElse(Constants.LOCATION_DEFAULT_DISTANCE).toString)
    )
    request[LocationSearchFeed](Verbs.GET, Methods.LOCATIONS_SEARCH, Some(params))
  }

  /**
    * Get a list of recent media objects from a given location.
    *
    * @param locationId
    * a id of the Media.
    * @param minId
    * Return media before this min_id. May be null.
    * @param maxId
    * Return media before this max_id. May be null.
    * @return a MediaFeed object.
    * if any error occurs.
    */
  def getRecentMediaByLocation(locationId: String, minId: Option[String] = None, maxId: Option[String] = None): Future[Option[MediaFeed]] = {
    val params: Map[String, Option[String]] = Map(
      QueryParam.MIN_ID -> Option(minId.mkString),
      QueryParam.MAX_ID -> Option(maxId.mkString)
    )
    val apiMethod: String = Methods.LOCATIONS_RECENT_MEDIA_BY_ID format locationId
    request[MediaFeed](Verbs.GET, apiMethod, Some(params))
  }

  /**
    * Set a like on this media by the currently authenticated user.
    *
    * @param mediaId
    * a mediaId of the Media
    * @return a LikesFeed object.
    * if any error occurs.
    */
  def setUserLike(mediaId: String): Future[Option[LikesFeed]] = {
    val apiMethod: String = Methods.LIKES_BY_MEDIA_ID format mediaId
    request[LikesFeed](Verbs.POST, apiMethod)
  }

  /**
    * Get information about a media object.
    *
    * @param mediaId
    * mediaId of the Media object.
    * @return a mediaFeed object.
    * if any error occurs.
    */
  def getMediaInfo(mediaId: String): Future[Option[MediaInfoFeed]] = {
    val apiPath = Methods.MEDIA_BY_ID format mediaId
    request[MediaInfoFeed](Verbs.GET, apiPath)
  }

  /**
    * Get the next page of recent media objects from a previously executed
    * request
    *
    * @param pagination
    */
  def getRecentMediaNextPage(pagination: Pagination): Future[Option[MediaFeed]] = {
    val page: PaginationHelper.Page = PaginationHelper.parseNextUrl(pagination, Constants.API_URL)
    request[MediaFeed](Verbs.GET, page.apiPath, Some(page.queryStringParams))
  }

  /**
    * Get the next page of user feed objects from a previously executed request
    *
    * @param pagination
    */
  def getUserFeedInfoNextPage(pagination: Pagination): Future[Option[UserFeed]] = {
    val page: PaginationHelper.Page = PaginationHelper.parseNextUrl(pagination, Constants.API_URL)
    request[UserFeed](Verbs.GET, page.apiPath, Option(page.queryStringParams))
  }

  /**
    * Remove a like on this media by the currently authenticated user.
    *
    * @param mediaId
    * a mediaId of the Media
    * @return a LikesFeed object.
    * if any error occurs.
    */
  def deleteUserLike(mediaId: String): Future[Option[LikesFeed]] = {
    val apiPath: String = Methods.LIKES_BY_MEDIA_ID format mediaId
    request[LikesFeed](Verbs.DELETE, apiPath)
  }

  /**
    * Remove a comment either on the authenticated user's media or authored by
    * the authenticated user.
    *
    * @param mediaId
    * a mediaId of the Media
    * @param commentId
    * a commentId of the Comment
    * @return a MediaCommentResponse feed.
    * if any error occurs.
    */
  def deleteMediaCommentById(mediaId: String, commentId: String): Future[Option[MediaCommentResponse]] = {
    val apiPath: String = Methods.DELETE_MEDIA_COMMENTS format (mediaId, commentId)
    request[MediaCommentResponse](Verbs.DELETE, apiPath)
  }

  /**
    * Get the next page for list of 'users' the authenticated is followed by.
    *
    * @param userId
    * @param cursor
    */
  def getUserFollowedByList(userId: String, cursor: Option[String] = None): Future[Option[UserFeed]] = {
    val params: Map[String, Option[String]] = Map(
      QueryParam.CURSOR -> Option(cursor.mkString)
    )
    val apiPath: String = Methods.USERS_ID_FOLLOWED_BY format userId
    request[UserFeed](Verbs.GET, apiPath, Some(params))
  }

  /**
    * Get the next page for list of 'users' the authenticated is followed by.
    *
    * @param pagination
    */
  def getUserFollowedByListNextPage(pagination: Pagination): Future[Option[UserFeed]] = {
    getUserFeedInfoNextPage(pagination)
  }

  /**
    * Create a comment on a media.
    *
    * @param mediaId
    * a mediaId
    * @param text
    * Text to post as a comment on the media as specified in
    * media-id.
    * @return a MediaCommentResponse feed.
    * if any error occurs.
    */
  def setMediaComments(mediaId: String, text: String): Future[Option[MediaCommentResponse]] = {
    val params: Map[String, Option[String]] = Map(
      QueryParam.TEXT -> Some(text)
    )
    val apiPath: String = Methods.MEDIA_COMMENTS format mediaId
    request[MediaCommentResponse](Verbs.POST, apiPath, Some(params))
  }

  /**
    * Get information about a media object.
    *
    * @param shortCode
    * shortcode of the Media object.
    * @return a mediaFeed object.
    * if any error occurs.
    */
  def getMediaInfoByShortCode(shortCode: String): Future[Option[MediaInfoFeed]] = {
    val apiPath: String = Methods.MEDIA_BY_SHORT_CODE format shortCode
    request[MediaInfoFeed](Verbs.GET, apiPath)
  }

  /**
    * Search for tags by name - results are ordered first as an exact match,
    * then by popularity.
    *
    * @param tagName
    * name of the tag
    * @return a TagSearchFeed object.
    * if any error occurs.
    */
  def searchTags(tagName: String): Future[Option[TagSearchFeed]] = {
    val params: Map[String, Option[String]] = Map(
      QueryParam.SEARCH_QUERY -> Some(tagName)
    )
    request[TagSearchFeed](Verbs.GET, Methods.TAGS_SEARCH, Some(params))
  }

  /**
    * Get a list of recently tagged media.
    *
    * @param tagName
    *              name of the tag.
    * @param minTagId
    *              (return media before this tag_id), can be null
    * @param maxTagId
    *              (return media before this tag_id), can be null
    * @param count ,
    *              set to 0 to use default
    * @return { @link MediaFeed}
    *                 the media feed for the first page
    * if any error occurs.
    */
  def getRecentMediaFeedTags(tagName: String, minTagId: Option[String] = None, maxTagId: Option[String] = None, count: Option[Long] = None): Future[Option[MediaFeed]] = {
    val apiPath: String = Methods.TAGS_RECENT_MEDIA format tagName
    val params: Map[String, Option[String]] = Map(
      QueryParam.MIN_TAG_ID -> Option(minTagId.mkString),
      QueryParam.MAX_TAG_ID -> Option(maxTagId.mkString),
      QueryParam.COUNT -> Option(count.mkString)
    )
    request[MediaFeed](Verbs.GET, apiPath, Some(params))
  }

  /**
    * Set the relationship between the current user and the target user
    *
    * @param userId
    * userId of the user.
    * @param relationship
    * Relationship status
    * @return a Relationship feed object
    * if any error occurs.
    */
  def setUserRelationship(userId: String, relationship: Relationship): Future[Option[RelationshipFeed]] = {
    val params: Map[String, Option[String]] = Map(
      QueryParam.ACTION -> Some(relationship.value)
    )
    val apiPath: String = Methods.USERS_ID_RELATIONSHIP format userId
    request[RelationshipFeed](Verbs.POST, apiPath, Some(params))
  }

  /**
    * Get a list of users who have requested this user's permission to follow
    *
    * @return a UserFeed object.
    * if any error occurs.
    */
  def getUserRequestedBy: Future[Option[UserFeed]] = {
    request[UserFeed](Verbs.GET, Methods.USERS_SELF_REQUESTED_BY)
  }

  /**
    * Get information about the current user's relationship
    * (follow/following/etc) to another user.
    *
    * @param userId
    * userId of the User.
    * @return a Relationship feed object.
    * if any error occurs.
    */
  def getUserRelationship(userId: String): Future[Option[RelationshipFeed]] = {
    val apiPath: String = Methods.USERS_ID_RELATIONSHIP format userId
    request[RelationshipFeed](Verbs.GET, apiPath)
  }

  /**
    * Search for a location by Facebook places id.
    *
    * @param facebookPlacesId
    * Facebook places id of the location
    * @return a LocationSearchFeed object.
    * if any error occurs.
    */
  def searchFacebookPlace(facebookPlacesId: String): Future[Option[LocationSearchFeed]] = {
    val params: Map[String, Option[String]] = Map(
      QueryParam.FACEBOOK_PLACES_ID -> Some(facebookPlacesId)
    )
    request[LocationSearchFeed](Verbs.GET, Methods.LOCATIONS_SEARCH, Some(params))
  }

  /**
    * Search for media in a given area.
    *
    * @param latitude
    * Latitude of the center search coordinate.
    * @param longitude
    * Longitude of the center search coordinate.
    * @param distance
    * Default is 1km (distance=1000), max distance is 5km.
    * @return a MediaFeed object.
    * if any error occurs
    */
  def searchMedia(latitude: Double, longitude:Double, distance: Option[Int] = None): Future[Option[MediaFeed]] = {
    val params: Map[String, Option[String]] = Map(
      QueryParam.LATITUDE -> Some(latitude.toString),
      QueryParam.LONGITUDE -> Some(longitude.toString),
      QueryParam.DISTANCE -> Option(distance.mkString)
    )
    request[MediaFeed](Verbs.GET, Methods.MEDIA_SEARCH, Some(params))
  }

  /**
    * Get a list of users who have liked this media.
    *
    * @param mediaId
    * a mediaId of the Media
    * @return a LikesFeed object.
    * if any error occurs.
    */
  def getUserLikes(mediaId: String): Future[Option[LikesFeed]] = {
    val apiPath: String = Methods.LIKES_BY_MEDIA_ID format mediaId
    request[LikesFeed](Verbs.GET, apiPath)
  }
}