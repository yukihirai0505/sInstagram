package com.yukihirai0505.sInstagram

import java.net.URLEncoder

import com.netaporter.uri.Uri._
import com.yukihirai0505.com.scala.Request
import com.yukihirai0505.com.scala.constants.Verbs
import com.yukihirai0505.com.scala.model.Response
import com.yukihirai0505.sInstagram.model._
import com.yukihirai0505.sInstagram.responses.auth.{AccessToken, Auth, SignedAccessToken}
import com.yukihirai0505.sInstagram.responses.comments.MediaCommentsFeed
import com.yukihirai0505.sInstagram.responses.common.{NoDataResponse, Pagination, User}
import com.yukihirai0505.sInstagram.responses.likes.LikesFeed
import com.yukihirai0505.sInstagram.responses.locations.{LocationInfo, LocationSearchFeed}
import com.yukihirai0505.sInstagram.responses.media.{MediaFeed, MediaInfoFeed}
import com.yukihirai0505.sInstagram.responses.relationships.RelationshipFeed
import com.yukihirai0505.sInstagram.responses.tags.{TagInfoFeed, TagSearchFeed}
import com.yukihirai0505.sInstagram.responses.users.basicinfo.UserInfo
import com.yukihirai0505.sInstagram.responses.users.feed.UserFeed
import com.yukihirai0505.sInstagram.utils.PaginationHelper
import dispatch._
import play.api.libs.json.Reads

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.language.postfixOps


/**
  * author Yuki Hirai on 2016/11/09.
  */
class Instagram(auth: Auth) {

  /**
    * Transform an Authentication type to be used in a URL.
    *
    * @param a Authentication
    * @return String
    */
  protected def authToGETParams(a: Auth): String = a match {
    case AccessToken(token) => s"${OAuthConstants.ACCESS_TOKEN}=$token"
    case SignedAccessToken(token, _) => s"${OAuthConstants.ACCESS_TOKEN}=$token"
  }

  /** *
    * Add sign for secure request
    *
    * @param url
    * @param postData
    * @return
    */
  protected def addSecureSigIfNeeded(url: String, postData: Option[Map[String, String]] = None)
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

  /** *
    * concat map option
    *
    * @param postData
    * @param params
    * @return
    */
  protected def concatMapOpt(postData: Option[Map[String, String]], params: Map[String, Option[String]])
  : Map[String, Option[String]] = postData match {
    case Some(m) => params ++ m.mapValues(Some(_))
    case _ => params
  }

  /** *
    * Request instagram api method
    *
    * @param verb
    * @param apiPath
    * @param params
    * @param r
    * @tparam T
    * @return
    */
  def request[T](verb: Verbs, apiPath: String, params: Option[Map[String, Option[String]]] = None)(implicit r: Reads[T]): Future[Response[T]] = {
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
    val requestWithParams = if (verb.label == Verbs.GET.label) {
      request <<? parameters
    } else {
      request << parameters
    }
    //println(requestWithParams.url)
    Request.sendRequestJson[T](requestWithParams)
  }

  /**
    * Get basic information about a user.
    *
    * @param userId
    * user-id
    * @return a MediaFeed object.
    *         if any error occurs.
    */
  def getUserInfo(userId: String): Future[Response[UserInfo]] = {
    val apiPath: String = Methods.USERS_WITH_ID format userId
    request(Verbs.GET, apiPath)
  }

  /**
    * Get basic information about a user.
    *
    * @return a UserInfo object.
    *         if any error occurs.
    */
  def getCurrentUserInfo: Future[Response[UserInfo]] = {
    request(Verbs.GET, Methods.USERS_SELF)
  }

  /**
    * Get the most recent media published by a user.
    *
    * @param userId
    * @param count
    * @param minId
    * @param maxId
    * @return the mediaFeed object
    *         if any error occurs
    */
  def getRecentMediaFeed(userId: Option[String] = None, count: Option[Int] = None, minId: Option[String] = None, maxId: Option[String] = None): Future[Response[MediaFeed]] = {
    val params: Map[String, Option[String]] = Map(
      QueryParam.COUNT -> count.map(_.toString),
      QueryParam.MIN_ID -> minId,
      QueryParam.MAX_ID -> maxId
    )
    val apiPath: String = userId match {
      case Some(id) => Methods.USERS_RECENT_MEDIA format id
      case None => Methods.USERS_SELF_RECENT_MEDIA
    }
    request(Verbs.GET, apiPath, Some(params))
  }

  /**
    * Get the next page for list of 'users' the authenticated user follows.
    *
    */
  def getUserFollowList(cursor: Option[String] = None): Future[Response[UserFeed]] = {
    val params: Map[String, Option[String]] = Map(
      QueryParam.CURSOR -> cursor
    )
    val apiPath: String = Methods.USERS_FOLLOWS
    request(Verbs.GET, apiPath, Some(params))
  }

  /**
    * Get the next page for list of 'users' the authenticated user follows.
    *
    * @param pagination
    */
  def getUserFollowListNextPageByPage(pagination: Pagination): Future[Response[UserFeed]] = {
    val page: PaginationHelper.Page = PaginationHelper.parseNextUrl(pagination, Constants.API_URL)
    request(Verbs.GET, page.apiPath, Option(page.queryStringParams))
  }

  /**
    * Get all followers the authenticated
    *
    * @param executionContextExecutor
    * @return
    */
  def getUserAllFollowsList(implicit executionContextExecutor: ExecutionContextExecutor): Future[Seq[User]] = {
    def getFollowers(followers: Seq[User], pagination: Option[Pagination]): Future[Seq[User]] = {
      pagination.flatMap(_.nextUrl) match {
        case Some(_) =>
          getUserFollowListNextPageByPage(pagination.get).flatMap {
            case Response(data, _) =>
              val d = data.get
              getFollowers(d.data.getOrElse(Seq.empty[User]) ++ followers, d.pagination)
            case _ => Future successful followers
          }
        case None => Future successful followers
      }
    }

    getUserFollowList().flatMap { response =>
      response.data match {
        case Some(data) =>
          getFollowers(data.data.getOrElse(Seq.empty), data.pagination)
        case None => Future successful Seq.empty
      }
    }
  }

  /**
    * Get the authenticated user's list of media they've liked.
    *
    * @return a MediaFeed object.
    *         if any error occurs.
    */
  def getUserLikedMediaFeed(maxLikeId: Option[Long] = None, count: Option[Int] = None): Future[Response[MediaFeed]] = {
    val params: Map[String, Option[String]] = Map(
      QueryParam.MAX_LIKE_ID -> maxLikeId.map(_.toString),
      QueryParam.COUNT -> count.map(_.toString)
    )
    request(Verbs.GET, Methods.USERS_SELF_LIKED_MEDIA, Some(params))
  }

  /**
    * Search for a user by name.
    *
    * @param query
    * A query string.
    * @return a UserFeed object.
    *         if any error occurs.
    */
  def searchUser(query: String, count: Option[Int] = None): Future[Response[UserFeed]] = {
    val params: Map[String, Option[String]] = Map(
      QueryParam.SEARCH_QUERY -> Some(query),
      QueryParam.COUNT -> count.map(_.toString)
    )
    request(Verbs.GET, Methods.USERS_SEARCH, Some(params))
  }

  /**
    * Get information about a location.
    *
    * @param locationId
    * an id of the Location
    * @return a LocationInfo object.
    *         if any error occurs.
    */
  def getLocationInfo(locationId: String): Future[Response[LocationInfo]] = {
    val apiPath: String = Methods.LOCATIONS_BY_ID format locationId
    request(Verbs.GET, apiPath)
  }

  /**
    * Get information about a tag object.
    *
    * @param tagName
    * name of the tag.
    * @return a TagInfoFeed object.
    *         if any error occurs.
    */
  def getTagInfo(tagName: String): Future[Response[TagInfoFeed]] = {
    val apiPath: String = Methods.TAGS_BY_NAME format URLEncoder.encode(tagName, "UTF-8")
    request(Verbs.GET, apiPath)
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
    *         if any error occurs.
    */
  def searchLocation(latitude: Double, longitude: Double, distance: Option[Int] = None): Future[Response[LocationSearchFeed]] = {
    val params: Map[String, Option[String]] = Map(
      QueryParam.LATITUDE -> Some(latitude.toString),
      QueryParam.LONGITUDE -> Some(longitude.toString),
      QueryParam.DISTANCE -> Some(distance.getOrElse(Constants.LOCATION_DEFAULT_DISTANCE).toString)
    )
    request(Verbs.GET, Methods.LOCATIONS_SEARCH, Some(params))
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
    *         if any error occurs.
    */
  def getRecentMediaByLocation(locationId: String, minId: Option[String] = None, maxId: Option[String] = None): Future[Response[MediaFeed]] = {
    val params: Map[String, Option[String]] = Map(
      QueryParam.MIN_ID -> minId,
      QueryParam.MAX_ID -> maxId
    )
    val apiMethod: String = Methods.LOCATIONS_RECENT_MEDIA_BY_ID format locationId
    request(Verbs.GET, apiMethod, Some(params))
  }

  /**
    * Set a like on this media by the currently authenticated user.
    *
    * @param mediaId
    * a mediaId of the Media
    * @return a LikesFeed object.
    *         if any error occurs.
    */
  def setUserLike(mediaId: String): Future[Response[NoDataResponse]] = {
    request(Verbs.POST, Methods.LIKES_BY_MEDIA_ID format mediaId)
  }

  /**
    * Get information about a media object.
    *
    * @param mediaId
    * mediaId of the Media object.
    * @return a mediaFeed object.
    *         if any error occurs.
    */
  def getMediaInfo(mediaId: String): Future[Response[MediaInfoFeed]] = {
    request(Verbs.GET, Methods.MEDIA_BY_ID format mediaId)
  }

  /**
    * Get the next page of recent media objects from a previously executed
    * request
    *
    * @param pagination
    */
  def getRecentMediaNextPage(pagination: Pagination): Future[Response[MediaFeed]] = {
    val page: PaginationHelper.Page = PaginationHelper.parseNextUrl(pagination, Constants.API_URL)
    request(Verbs.GET, page.apiPath, Some(page.queryStringParams))
  }

  /**
    * Get the next page of user feed objects from a previously executed request
    *
    * @param pagination
    */
  def getUserFeedInfoNextPage(pagination: Pagination): Future[Response[UserFeed]] = {
    val page: PaginationHelper.Page = PaginationHelper.parseNextUrl(pagination, Constants.API_URL)
    request(Verbs.GET, page.apiPath, Option(page.queryStringParams))
  }

  /**
    * Remove a like on this media by the currently authenticated user.
    *
    * @param mediaId
    * a mediaId of the Media
    * @return a LikesFeed object.
    *         if any error occurs.
    */
  def deleteUserLike(mediaId: String): Future[Response[NoDataResponse]] = {
    request(Verbs.DELETE, Methods.LIKES_BY_MEDIA_ID format mediaId)
  }

  /**
    * Get a full list of comments on a media.
    *
    * @param mediaId
    * a mediaId
    * @return a MediaCommentsFeed object.
    *         if any error occurs.
    */
  def getMediaComments(mediaId: String): Future[Response[MediaCommentsFeed]] = {
    request(Verbs.GET, Methods.MEDIA_COMMENTS format mediaId)
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
    *         if any error occurs.
    */
  def setMediaComments(mediaId: String, text: String): Future[Response[NoDataResponse]] = {
    val params: Map[String, Option[String]] = Map(
      QueryParam.TEXT -> Some(text)
    )
    request(Verbs.POST, Methods.MEDIA_COMMENTS format mediaId, Some(params))
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
    *         if any error occurs.
    */
  def deleteMediaCommentById(mediaId: String, commentId: String): Future[Response[NoDataResponse]] = {
    request(Verbs.DELETE, Methods.DELETE_MEDIA_COMMENTS format(mediaId, commentId))
  }

  /**
    * Get the next page for list of 'users' the authenticated is followed by.
    *
    * @param cursor
    */
  def getUserFollowedByList(cursor: Option[String] = None): Future[Response[UserFeed]] = {
    val params: Map[String, Option[String]] = Map(
      QueryParam.CURSOR -> cursor
    )
    request(Verbs.GET, Methods.USERS_FOLLOWED_BY, Some(params))
  }

  /**
    * Get the next page for list of 'users' the authenticated is followed by.
    *
    * @param pagination
    */
  def getUserFollowedByListNextPage(pagination: Pagination): Future[Response[UserFeed]] = {
    getUserFeedInfoNextPage(pagination)
  }

  /**
    * Get all followers the authenticated
    *
    * @param executionContextExecutor
    * @return
    */
  def getUserAllFollowersList(implicit executionContextExecutor: ExecutionContextExecutor): Future[Seq[User]] = {
    def getFollowers(followers: Seq[User], pagination: Option[Pagination]): Future[Seq[User]] = {
      pagination.flatMap(_.nextUrl) match {
        case Some(_) =>
          getUserFollowedByListNextPage(pagination.get).flatMap {
            case Response(data, _) =>
              val d = data.get
              getFollowers(d.data.getOrElse(Seq.empty[User]) ++ followers, d.pagination)
            case _ => Future successful followers
          }
        case None => Future successful followers
      }
    }

    getUserFollowedByList().flatMap { response =>
      response.data match {
        case Some(data) =>
          getFollowers(data.data.getOrElse(Seq.empty), data.pagination)
        case None => Future successful Seq.empty
      }
    }
  }

  /**
    * Get information about a media object.
    *
    * @param shortCode
    * shortcode of the Media object.
    * @return a mediaFeed object.
    *         if any error occurs.
    */
  def getMediaInfoByShortCode(shortCode: String): Future[Response[MediaInfoFeed]] = {
    request(Verbs.GET, Methods.MEDIA_BY_SHORT_CODE format shortCode)
  }

  /**
    * Search for tags by name - results are ordered first as an exact match,
    * then by popularity.
    *
    * @param tagName
    * name of the tag
    * @return a TagSearchFeed object.
    *         if any error occurs.
    */
  def searchTags(tagName: String): Future[Response[TagSearchFeed]] = {
    val params: Map[String, Option[String]] = Map(
      QueryParam.SEARCH_QUERY -> Some(tagName)
    )
    request(Verbs.GET, Methods.TAGS_SEARCH, Some(params))
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
    *         the media feed for the first page
    *         if any error occurs.
    */
  def getRecentMediaFeedTags(tagName: String, minTagId: Option[String] = None, maxTagId: Option[String] = None, count: Option[Long] = None): Future[Response[MediaFeed]] = {
    val apiPath: String = Methods.TAGS_RECENT_MEDIA format tagName
    val params: Map[String, Option[String]] = Map(
      QueryParam.MIN_TAG_ID -> minTagId,
      QueryParam.MAX_TAG_ID -> maxTagId,
      QueryParam.COUNT -> count.map(_.toString)
    )
    request(Verbs.GET, apiPath, Some(params))
  }

  /**
    * Set the relationship between the current user and the target user
    *
    * @param userId
    * userId of the user.
    * @param relationship
    * Relationship status
    * @return a Relationship feed object
    *         if any error occurs.
    */
  def setUserRelationship(userId: String, relationship: Relationship): Future[Response[RelationshipFeed]] = {
    val params: Map[String, Option[String]] = Map(
      QueryParam.ACTION -> Some(relationship.value)
    )
    request(Verbs.POST, Methods.USERS_ID_RELATIONSHIP format userId, Some(params))
  }

  /**
    * Get a list of users who have requested this user's permission to follow
    *
    * @return a UserFeed object.
    *         if any error occurs.
    */
  def getUserRequestedBy: Future[Response[UserFeed]] = {
    request(Verbs.GET, Methods.USERS_SELF_REQUESTED_BY)
  }

  /**
    * Get information about the current user's relationship
    * (follow/following/etc) to another user.
    *
    * @param userId
    * userId of the User.
    * @return a Relationship feed object.
    *         if any error occurs.
    */
  def getUserRelationship(userId: String): Future[Response[RelationshipFeed]] = {
    request(Verbs.GET, Methods.USERS_ID_RELATIONSHIP format userId)
  }

  /**
    * Search for a location by Facebook places id.
    *
    * @param facebookPlacesId
    * Facebook places id of the location
    * @return a LocationSearchFeed object.
    *         if any error occurs.
    */
  def searchFacebookPlace(facebookPlacesId: String): Future[Response[LocationSearchFeed]] = {
    val params: Map[String, Option[String]] = Map(
      QueryParam.FACEBOOK_PLACES_ID -> Some(facebookPlacesId)
    )
    request(Verbs.GET, Methods.LOCATIONS_SEARCH, Some(params))
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
    *         if any error occurs
    */
  def searchMedia(latitude: Double, longitude: Double, distance: Option[Int] = None): Future[Response[MediaFeed]] = {
    val params: Map[String, Option[String]] = Map(
      QueryParam.LATITUDE -> Some(latitude.toString),
      QueryParam.LONGITUDE -> Some(longitude.toString),
      QueryParam.DISTANCE -> distance.map(_.toString)
    )
    request(Verbs.GET, Methods.MEDIA_SEARCH, Some(params))
  }

  /**
    * Get a list of users who have liked this media.
    *
    * @param mediaId
    * a mediaId of the Media
    * @return a LikesFeed object.
    *         if any error occurs.
    */
  def getUserLikes(mediaId: String): Future[Response[LikesFeed]] = {
    request(Verbs.GET, Methods.LIKES_BY_MEDIA_ID format mediaId)
  }
}