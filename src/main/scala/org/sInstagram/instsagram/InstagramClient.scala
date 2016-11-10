package org.sInstagram.instsagram

import dispatch._
import org.sInstagram.http.Response
import org.sInstagram.model.Relationship
import org.sInstagram.responses.comments.{MediaCommentResponse, MediaCommentsFeed}
import org.sInstagram.responses.common.Pagination
import org.sInstagram.responses.likes.LikesFeed
import org.sInstagram.responses.locations.{LocationInfo, LocationSearchFeed}
import org.sInstagram.responses.media.{MediaFeed, MediaInfoFeed}
import org.sInstagram.responses.relationships.RelationshipFeed
import org.sInstagram.responses.tags.{TagInfoFeed, TagSearchFeed}
import org.sInstagram.responses.users.basicinfo.UserInfo
import org.sInstagram.responses.users.feed.UserFeed

/**
  * Instagram interface
  * author Yuki Hirai on 2016/11/08.
  */
trait InstagramClient {

  /**
    * Get basic information about a user.
    *
    * @param userId
    * user-id
    * @return a MediaFeed object.
    * if any error occurs.
    */
  def getUserInfo(userId: String): Future[Response[UserInfo]]

  /**
    * Get basic information about a user.
    *
    * @return a UserInfo object.
    * if any error occurs.
    */
  def getCurrentUserInfo: Future[Response[UserInfo]]

  /**
    * Get current user's recent media
    *
    * @return a MediaFeedObject
    * @author tolstovdmit
    */
  def getUserRecentMedia(count: Option[Int], minId: Option[String], maxId: Option[String]): Future[Response[MediaFeed]]

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
  def getRecentMediaFeed(userId: String, count: Option[Int], minId: Option[String], maxId: Option[String]): Future[Response[MediaFeed]]

  /**
    * Get the next page of recent media objects from a previously executed
    * request
    *
    * @param pagination
    */
  def getRecentMediaNextPage(pagination: Pagination): Future[Response[MediaFeed]]

  /**
    * Get the next page of user feed objects from a previously executed request
    *
    * @param pagination
    */
  def getUserFeedInfoNextPage(pagination: Pagination): Future[Response[UserFeed]]

  /**
    * Get the authenticated user's list of media they've liked.
    *
    * @return a MediaFeed object.
    * if any error occurs.
    */
  def getUserLikedMediaFeed(maxLikeId: Option[Long], count: Option[Int]): Future[Response[MediaFeed]]

  /**
    * Search for a user by name.
    *
    * @param query
    * A query string.
    * @return a UserFeed object.
    * if any error occurs.
    */
  def searchUser(query: String, count: Option[Int]): Future[Response[UserFeed]]

  /**
    * Get the list of 'users' the authenticated user follows.
    *
    * @param userId
    * userId of the User.
    * @return a UserFeed object.
    * if any error occurs.
    */
  def getUserFollowList(userId: String): Future[Response[UserFeed]]

  /**
    * Get the next page for list of 'users' the authenticated user follows.
    *
    */
  def getUserFollowListNextPage(userId: String, cursor: Option[String]): Future[Response[UserFeed]]

  /**
    * Get the next page for list of 'users' the authenticated user follows.
    *
    * @param pagination
    */
  def getUserFollowListNextPageByPage(pagination: Option[Pagination]): Future[Response[UserFeed]]

  /**
    * Get the next page for list of 'users' the authenticated is followed by.
    *
    * @param userId
    * @param cursor
    */
  def getUserFollowedByList(userId: String, cursor: Option[String]): Future[Response[UserFeed]]

  /**
    * Get the next page for list of 'users' the authenticated is followed by.
    *
    * @param pagination
    */
  def getUserFollowedByListNextPage(pagination: Pagination): Future[Response[UserFeed]]

  /**
    * Get a list of users who have requested this user's permission to follow
    *
    * @return a UserFeed object.
    * if any error occurs.
    */
  def getUserRequestedBy: Future[Response[UserFeed]]

  /**
    * Get information about the current user's relationship
    * (follow/following/etc) to another user.
    *
    * @param userId
    * userId of the User.
    * @return a Relationship feed object.
    * if any error occurs.
    */
  def getUserRelationship(userId: String): Future[Response[RelationshipFeed]]

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
  def setUserRelationship(userId: String, relationship: Relationship): Future[Response[RelationshipFeed]]

  /**
    * Get information about a media object.
    *
    * @param mediaId
    * mediaId of the Media object.
    * @return a mediaFeed object.
    * if any error occurs.
    */
  def getMediaInfo(mediaId: String): Future[Response[MediaInfoFeed]]

  /**
    * Get information about a media object.
    *
    * @param shortCode
    * shortcode of the Media object.
    * @return a mediaFeed object.
    * if any error occurs.
    */
  def getMediaInfoByShortCode(shortCode: String): Future[Response[MediaInfoFeed]]

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
  def searchMedia(latitude: Double, longitude: Double, distance: Option[Int]): Future[Response[MediaFeed]]

  /**
    * Get a full list of comments on a media.
    *
    * @param mediaId
    * a mediaId
    * @return a MediaCommentsFeed object.
    * if any error occurs.
    */
  def getMediaComments(mediaId: String): Future[Response[MediaCommentsFeed]]

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
  def setMediaComments(mediaId: String, text: String): Future[Response[MediaCommentResponse]]

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
  def deleteMediaCommentById(mediaId: String, commentId: String): Future[Response[MediaCommentResponse]]

  /**
    * Get a list of users who have liked this media.
    *
    * @param mediaId
    * a mediaId of the Media
    * @return a LikesFeed object.
    * if any error occurs.
    */
  def getUserLikes(mediaId: String): Future[Response[LikesFeed]]

  /**
    * Set a like on this media by the currently authenticated user.
    *
    * @param mediaId
    * a mediaId of the Media
    * @return a LikesFeed object.
    * if any error occurs.
    */
  def setUserLike(mediaId: String): Future[Response[LikesFeed]]

  /**
    * Remove a like on this media by the currently authenticated user.
    *
    * @param mediaId
    * a mediaId of the Media
    * @return a LikesFeed object.
    * if any error occurs.
    */
  def deleteUserLike(mediaId: String): Future[Response[LikesFeed]]

  /**
    * Get information about a tag object.
    *
    * @param tagName
    * name of the tag.
    * @return a TagInfoFeed object.
    * if any error occurs.
    */
  def getTagInfo(tagName: String): Future[Response[TagInfoFeed]]

  /**
	  * Search for tags by name - results are ordered first as an exact match,
    * then by popularity.
    *
    * @param tagName
    * name of the tag
    * @return a TagSearchFeed object.
    * if any error occurs.
    */
  def searchTags(tagName: String): Future[Response[TagSearchFeed]]

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
  def getRecentMediaFeedTags(tagName: String, minTagId: Option[String], maxTagId: Option[String], count: Option[Long]): Future[Response[MediaFeed]]

  /**
    * Get information about a location.
    *
    * @param locationId
    * an id of the Location
    * @return a LocationInfo object.
    * if any error occurs.
    */
  def getLocationInfo(locationId: String): Future[Response[LocationInfo]]

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
  def getRecentMediaByLocation(locationId: String, minId: Option[String], maxId: Option[String]): Future[Response[MediaFeed]]

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
  def searchLocation(latitude: Double, longitude: Double, distance: Option[Int]): Future[Response[LocationSearchFeed]]

  /**
    * Search for a location by Facebook places id.
    *
    * @param facebookPlacesId
    * Facebook places id of the location
    * @return a LocationSearchFeed object.
    * if any error occurs.
    */
  def searchFacebookPlace(facebookPlacesId: String): Future[Response[LocationSearchFeed]]

}
