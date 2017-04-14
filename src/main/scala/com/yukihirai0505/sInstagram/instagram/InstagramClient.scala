package com.yukihirai0505.sInstagram.instagram

import com.yukihirai0505.sInstagram.model.Relationship
import com.yukihirai0505.sInstagram.responses.comments.{MediaCommentResponse, MediaCommentsFeed}
import com.yukihirai0505.sInstagram.responses.common.Pagination
import com.yukihirai0505.sInstagram.responses.likes.LikesFeed
import com.yukihirai0505.sInstagram.responses.locations.{LocationInfo, LocationSearchFeed}
import com.yukihirai0505.sInstagram.responses.media.{MediaFeed, MediaInfoFeed}
import com.yukihirai0505.sInstagram.responses.relationships.RelationshipFeed
import com.yukihirai0505.sInstagram.responses.tags.{TagInfoFeed, TagSearchFeed}
import com.yukihirai0505.sInstagram.responses.users.basicinfo.UserInfo
import com.yukihirai0505.sInstagram.responses.users.feed.UserFeed
import dispatch._

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
  def getUserInfo(userId: String): Future[Option[UserInfo]]

  /**
    * Get basic information about a user.
    *
    * @return a UserInfo object.
    * if any error occurs.
    */
  def getCurrentUserInfo: Future[Option[UserInfo]]

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
  def getRecentMediaFeed(userId: Option[String], count: Option[Int], minId: Option[String], maxId: Option[String]): Future[Option[MediaFeed]]

  /**
    * Get the next page of recent media objects from a previously executed
    * request
    *
    * @param pagination
    */
  def getRecentMediaNextPage(pagination: Pagination): Future[Option[MediaFeed]]

  /**
    * Get the next page of user feed objects from a previously executed request
    *
    * @param pagination
    */
  def getUserFeedInfoNextPage(pagination: Pagination): Future[Option[UserFeed]]

  /**
    * Get the authenticated user's list of media they've liked.
    *
    * @return a MediaFeed object.
    * if any error occurs.
    */
  def getUserLikedMediaFeed(maxLikeId: Option[Long], count: Option[Int]): Future[Option[MediaFeed]]

  /**
    * Search for a user by name.
    *
    * @param query
    * A query string.
    * @return a UserFeed object.
    * if any error occurs.
    */
  def searchUser(query: String, count: Option[Int]): Future[Option[UserFeed]]

  /**
    * Get the list of 'users' the authenticated user follows.
    *
    * @param userId
    * userId of the User.
    * @return a UserFeed object.
    * if any error occurs.
    */
  def getUserFollowList(userId: String): Future[Option[UserFeed]]

  /**
    * Get the next page for list of 'users' the authenticated user follows.
    *
    */
  def getUserFollowListNextPage(userId: String, cursor: Option[String]): Future[Option[UserFeed]]

  /**
    * Get the next page for list of 'users' the authenticated user follows.
    *
    * @param pagination
    */
  def getUserFollowListNextPageByPage(pagination: Pagination): Future[Option[UserFeed]]

  /**
    * Get the next page for list of 'users' the authenticated is followed by.
    *
    * @param userId
    * @param cursor
    */
  def getUserFollowedByList(userId: String, cursor: Option[String]): Future[Option[UserFeed]]

  /**
    * Get the next page for list of 'users' the authenticated is followed by.
    *
    * @param pagination
    */
  def getUserFollowedByListNextPage(pagination: Pagination): Future[Option[UserFeed]]

  /**
    * Get a list of users who have requested this user's permission to follow
    *
    * @return a UserFeed object.
    * if any error occurs.
    */
  def getUserRequestedBy: Future[Option[UserFeed]]

  /**
    * Get information about the current user's relationship
    * (follow/following/etc) to another user.
    *
    * @param userId
    * userId of the User.
    * @return a Relationship feed object.
    * if any error occurs.
    */
  def getUserRelationship(userId: String): Future[Option[RelationshipFeed]]

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
  def setUserRelationship(userId: String, relationship: Relationship): Future[Option[RelationshipFeed]]

  /**
    * Get information about a media object.
    *
    * @param mediaId
    * mediaId of the Media object.
    * @return a mediaFeed object.
    * if any error occurs.
    */
  def getMediaInfo(mediaId: String): Future[Option[MediaInfoFeed]]

  /**
    * Get information about a media object.
    *
    * @param shortCode
    * shortcode of the Media object.
    * @return a mediaFeed object.
    * if any error occurs.
    */
  def getMediaInfoByShortCode(shortCode: String): Future[Option[MediaInfoFeed]]

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
  def searchMedia(latitude: Double, longitude: Double, distance: Option[Int]): Future[Option[MediaFeed]]

  /**
    * Get a full list of comments on a media.
    *
    * @param mediaId
    * a mediaId
    * @return a MediaCommentsFeed object.
    * if any error occurs.
    */
  def getMediaComments(mediaId: String): Future[Option[MediaCommentsFeed]]

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
  def setMediaComments(mediaId: String, text: String): Future[Option[MediaCommentResponse]]

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
  def deleteMediaCommentById(mediaId: String, commentId: String): Future[Option[MediaCommentResponse]]

  /**
    * Get a list of users who have liked this media.
    *
    * @param mediaId
    * a mediaId of the Media
    * @return a LikesFeed object.
    * if any error occurs.
    */
  def getUserLikes(mediaId: String): Future[Option[LikesFeed]]

  /**
    * Set a like on this media by the currently authenticated user.
    *
    * @param mediaId
    * a mediaId of the Media
    * @return a LikesFeed object.
    * if any error occurs.
    */
  def setUserLike(mediaId: String): Future[Option[LikesFeed]]

  /**
    * Remove a like on this media by the currently authenticated user.
    *
    * @param mediaId
    * a mediaId of the Media
    * @return a LikesFeed object.
    * if any error occurs.
    */
  def deleteUserLike(mediaId: String): Future[Option[LikesFeed]]

  /**
    * Get information about a tag object.
    *
    * @param tagName
    * name of the tag.
    * @return a TagInfoFeed object.
    * if any error occurs.
    */
  def getTagInfo(tagName: String): Future[Option[TagInfoFeed]]

  /**
    * Search for tags by name - results are ordered first as an exact match,
    * then by popularity.
    *
    * @param tagName
    * name of the tag
    * @return a TagSearchFeed object.
    * if any error occurs.
    */
  def searchTags(tagName: String): Future[Option[TagSearchFeed]]

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
  def getRecentMediaFeedTags(tagName: String, minTagId: Option[String], maxTagId: Option[String], count: Option[Long]): Future[Option[MediaFeed]]

  /**
    * Get information about a location.
    *
    * @param locationId
    * an id of the Location
    * @return a LocationInfo object.
    * if any error occurs.
    */
  def getLocationInfo(locationId: String): Future[Option[LocationInfo]]

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
  def getRecentMediaByLocation(locationId: String, minId: Option[String], maxId: Option[String]): Future[Option[MediaFeed]]

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
  def searchLocation(latitude: Double, longitude: Double, distance: Option[Int]): Future[Option[LocationSearchFeed]]

  /**
    * Search for a location by Facebook places id.
    *
    * @param facebookPlacesId
    * Facebook places id of the location
    * @return a LocationSearchFeed object.
    * if any error occurs.
    */
  def searchFacebookPlace(facebookPlacesId: String): Future[Option[LocationSearchFeed]]

}
