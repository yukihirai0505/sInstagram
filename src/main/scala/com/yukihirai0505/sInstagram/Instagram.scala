package com.yukihirai0505.sInstagram

import java.net.URLEncoder

import com.yukihirai0505.com.scala.constants.Verbs
import com.yukihirai0505.com.scala.model.Response
import com.yukihirai0505.sInstagram.model._
import com.yukihirai0505.sInstagram.responses.auth.Auth
import com.yukihirai0505.sInstagram.responses.{CommentData, LikeUserInfo, MediaFeed, RelationshipFeed, TagInfoFeed, UserInfo, _}
import com.yukihirai0505.sInstagram.utils.PaginationHelper

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.language.postfixOps


/**
  * author Yuki Hirai on 2016/11/09.
  */
class Instagram(override val auth: Auth) extends InstagramTrait {

  /* Users */

  /**
    * Get basic information about a user.
    *
    * @param userId
    * user-id
    * @return a Data[MediaFeed] object.
    *         if any error occurs.
    */
  def getUserInfo(userId: Option[String] = None): Future[Response[Data[UserInfo]]] = {
    val apiPath = userId match {
      case Some(id) => Methods.USERS_WITH_ID format id
      case None => Methods.USERS_SELF
    }
    request(Verbs.GET, apiPath)
  }

  /**
    * Get the most recent media published by a user.
    *
    * @param userId
    * @param count
    * @param minId
    * @param maxId
    * @return the DataWithPage[MediaFeed] object
    *         if any error occurs
    */
  def getRecentMediaFeed(userId: Option[String] = None, count: Option[Int] = None, minId: Option[String] = None, maxId: Option[String] = None): Future[Response[DataWithPage[MediaFeed]]] = {
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
    * Get the authenticated user's list of media they've liked.
    *
    * @return a DataWithPage[MediaFeed] object.
    *         if any error occurs.
    */
  def getUserLikedMediaFeed(maxLikeId: Option[Long] = None, count: Option[Int] = None): Future[Response[DataWithPage[MediaFeed]]] = {
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
  def searchUser(query: String, count: Option[Int] = None): Future[Response[Data[Seq[User]]]] = {
    val params: Map[String, Option[String]] = Map(
      QueryParam.SEARCH_QUERY -> Some(query),
      QueryParam.COUNT -> count.map(_.toString)
    )
    request(Verbs.GET, Methods.USERS_SEARCH, Some(params))
  }

  /* Relationship */

  /**
    * Get the next page for list of 'users' the authenticated user follows.
    *
    */
  def getFollowList(cursor: Option[String] = None): Future[Response[DataWithPage[User]]] = {
    val params: Map[String, Option[String]] = Map(
      QueryParam.CURSOR -> cursor
    )
    val apiPath: String = Methods.USERS_FOLLOWS
    request(Verbs.GET, apiPath, Some(params))
  }

  /**
    * Get all followers the authenticated
    *
    * @param executionContextExecutor
    * @return
    */
  def getAllFollowsList(implicit executionContextExecutor: ExecutionContextExecutor): Future[Seq[User]] = {
    PaginationHelper.getAllPages(getFollowList, getNextPage[DataWithPage[User]])
  }

  /**
    * Get the next page for list of 'users' the authenticated is followed by.
    *
    * @param cursor
    */
  def getFollowedByList(cursor: Option[String] = None): Future[Response[DataWithPage[User]]] = {
    val params: Map[String, Option[String]] = Map(
      QueryParam.CURSOR -> cursor
    )
    request(Verbs.GET, Methods.USERS_FOLLOWED_BY, Some(params))
  }

  /**
    * Get all followers the authenticated
    *
    * @param executionContextExecutor
    * @return
    */
  def getAllFollowersList(implicit executionContextExecutor: ExecutionContextExecutor): Future[Seq[User]] = {
    PaginationHelper.getAllPages(getFollowedByList, getNextPage[DataWithPage[User]])
  }

  /**
    * Get a list of users who have requested this user's permission to follow
    *
    * @return a UserFeed object.
    *         if any error occurs.
    */
  def getUserRequestedBy: Future[Response[Data[Seq[User]]]] = {
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
  def getUserRelationship(userId: String): Future[Response[Data[RelationshipFeed]]] = {
    request(Verbs.GET, Methods.USERS_ID_RELATIONSHIP format userId)
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
  def setUserRelationship(userId: String, relationship: Relationship): Future[Response[Data[RelationshipFeed]]] = {
    val params: Map[String, Option[String]] = Map(
      QueryParam.ACTION -> Some(relationship.value)
    )
    request(Verbs.POST, Methods.USERS_ID_RELATIONSHIP format userId, Some(params))
  }

  /* Media */

  /**
    * Get information about a media object.
    *
    * @param mediaId
    * mediaId of the Media object.
    * @return a DataWithPage[MediaFeed] object.
    *         if any error occurs.
    */
  def getMediaInfo(mediaId: String): Future[Response[Data[MediaFeed]]] = {
    request(Verbs.GET, Methods.MEDIA_BY_ID format mediaId)
  }

  /**
    * Get information about a media object.
    *
    * @param shortcode
    * shortcode of the Media object.
    * @return a DataWithPage[MediaFeed] object.
    *         if any error occurs.
    */
  def getMediaInfoByShortcode(shortcode: String): Future[Response[Data[MediaFeed]]] = {
    request(Verbs.GET, Methods.MEDIA_BY_SHORT_CODE format shortcode)
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
    * @return a DataWithPage[MediaFeed] object.
    *         if any error occurs
    */
  def searchMedia(latitude: Double, longitude: Double, distance: Option[Int] = None): Future[Response[Data[Seq[MediaFeed]]]] = {
    val params: Map[String, Option[String]] = Map(
      QueryParam.LATITUDE -> Some(latitude.toString),
      QueryParam.LONGITUDE -> Some(longitude.toString),
      QueryParam.DISTANCE -> distance.map(_.toString)
    )
    request(Verbs.GET, Methods.MEDIA_SEARCH, Some(params))
  }

  /* Comments */

  /**
    * Get a full list of comments on a media.
    *
    * @param mediaId
    * a mediaId
    * @return a MediaCommentsFeed object.
    *         if any error occurs.
    */
  def getMediaComments(mediaId: String): Future[Response[Data[Seq[CommentData]]]] = {
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
    * @return a NoDataResponse feed.
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
    * @return a NoDataResponse feed.
    *         if any error occurs.
    */
  def deleteMediaCommentById(mediaId: String, commentId: String): Future[Response[NoDataResponse]] = {
    request(Verbs.DELETE, Methods.DELETE_MEDIA_COMMENTS format(mediaId, commentId))
  }

  /* Likes */

  /**
    * Get a list of users who have liked this media.
    *
    * @param mediaId
    * a mediaId of the Media
    * @return a LikesFeed object.
    *         if any error occurs.
    */
  def getUserLikes(mediaId: String): Future[Response[Data[Seq[LikeUserInfo]]]] = {
    request(Verbs.GET, Methods.LIKES_BY_MEDIA_ID format mediaId)
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

  /* Tags */
  /**
    * Get information about a tag object.
    *
    * @param tagName
    * name of the tag.
    * @return a TagInfoFeed object.
    *         if any error occurs.
    */
  def getTagInfo(tagName: String): Future[Response[Data[TagInfoFeed]]] = {
    val apiPath: String = Methods.TAGS_BY_NAME format URLEncoder.encode(tagName, "UTF-8")
    request(Verbs.GET, apiPath)
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
    * @return { @link DataWithPage[MediaFeed]}
    *         the media feed for the first page
    *         if any error occurs.
    */
  def getTagsRecentMedia(tagName: String, minTagId: Option[String] = None, maxTagId: Option[String] = None, count: Option[Long] = None): Future[Response[DataWithPage[MediaFeed]]] = {
    val apiPath: String = Methods.TAGS_RECENT_MEDIA format tagName
    val params: Map[String, Option[String]] = Map(
      QueryParam.MIN_TAG_ID -> minTagId,
      QueryParam.MAX_TAG_ID -> maxTagId,
      QueryParam.COUNT -> count.map(_.toString)
    )
    request(Verbs.GET, apiPath, Some(params))
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
  def searchTags(tagName: String): Future[Response[Data[Seq[TagInfoFeed]]]] = {
    val params: Map[String, Option[String]] = Map(
      QueryParam.SEARCH_QUERY -> Some(tagName)
    )
    request(Verbs.GET, Methods.TAGS_SEARCH, Some(params))
  }

  /* Locations */

  /**
    * Get information about a location.
    *
    * @param locationId
    * an id of the Location
    * @return a LocationInfo object.
    *         if any error occurs.
    */
  def getLocationInfo(locationId: String): Future[Response[Data[Location]]] = {
    val apiPath: String = Methods.LOCATIONS_BY_ID format locationId
    request(Verbs.GET, apiPath)
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
    * @return a DataWithPage[MediaFeed] object.
    *         if any error occurs.
    */
  def getLocationRecentMedia(locationId: String, minId: Option[String] = None, maxId: Option[String] = None): Future[Response[DataWithPage[MediaFeed]]] = {
    val params: Map[String, Option[String]] = Map(
      QueryParam.MIN_ID -> minId,
      QueryParam.MAX_ID -> maxId
    )
    val apiMethod: String = Methods.LOCATIONS_RECENT_MEDIA_BY_ID format locationId
    request(Verbs.GET, apiMethod, Some(params))
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
  def searchLocation(latitude: Double, longitude: Double, distance: Option[Int] = None): Future[Response[Data[Seq[Location]]]] = {
    val params: Map[String, Option[String]] = Map(
      QueryParam.LATITUDE -> Some(latitude.toString),
      QueryParam.LONGITUDE -> Some(longitude.toString),
      QueryParam.DISTANCE -> Some(distance.getOrElse(Constants.LOCATION_DEFAULT_DISTANCE).toString)
    )
    request(Verbs.GET, Methods.LOCATIONS_SEARCH, Some(params))
  }

  /**
    * Search for a location by Facebook places id.
    *
    * @param facebookPlacesId
    * Facebook places id of the location
    * @return a LocationSearchFeed object.
    *         if any error occurs.
    */
  def searchFacebookPlace(facebookPlacesId: String): Future[Response[Data[Seq[Location]]]] = {
    val params: Map[String, Option[String]] = Map(
      QueryParam.FACEBOOK_PLACES_ID -> Some(facebookPlacesId)
    )
    request(Verbs.GET, Methods.LOCATIONS_SEARCH, Some(params))
  }

}