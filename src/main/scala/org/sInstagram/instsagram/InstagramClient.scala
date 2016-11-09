package org.sInstagram.instsagram

import dispatch._
import org.sInstagram.exceptions.InstagramException
import org.sInstagram.http.Response
import org.sInstagram.model.Relationship
import org.sInstagram.responses.comments.{MediaCommentResponse, MediaCommentsFeed}
import org.sInstagram.responses.common.Pagination
import org.sInstagram.responses.likes.LikesFeed
import org.sInstagram.responses.locations.{LocationSearchFeed, LocationInfo}
import org.sInstagram.responses.media.{MediaInfoFeed, MediaFeed}
import org.sInstagram.responses.tags.{TagSearchFeed, TagInfoFeed}
import org.sInstagram.responses.relationships.RelationshipFeed
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
		* @throws InstagramException
		* if any error occurs.
		*/
	@throws(classOf[InstagramException])
	def getUserInfo(userId: String): Future[Response[UserInfo]]

	/**
		* Get basic information about a user.
		*
		* @return a UserInfo object.
		* @throws InstagramException
		* if any error occurs.
		*/
	@throws(classOf[InstagramException])
	def getCurrentUserInfo: Future[Response[UserInfo]]

	/**
		* Get current user's recent media
		*
		* @return a MediaFeedObject
		* @throws InstagramException
		* @author tolstovdmit
		*/
	@throws(classOf[InstagramException])
	def getUserRecentMedia(count: Option[Int], minId: Option[String], maxId: Option[String]): Future[Response[MediaFeed]]

	/**
		* Get the most recent media published by a user.
		*
		* @param userId
		* @param count
		* @param minId
		* @param maxId
		* @return the mediaFeed object
		* @throws InstagramException
		* if any error occurs
		*/
	@throws(classOf[InstagramException])
	def getRecentMediaFeed(userId: Option[String], count: Option[Int], minId: Option[String], maxId: Option[String]): MediaFeed

	/**
		* Get the next page of recent media objects from a previously executed
		* request
		*
		* @param pagination
		* @throws InstagramException
		*/
	@throws(classOf[InstagramException])
	def getRecentMediaNextPage(pagination: Option[Pagination]): Future[Response[MediaFeed]]

	/**
		* Get the authenticated user's list of media they've liked.
		*
		* @return a MediaFeed object.
		* @throws InstagramException
		* if any error occurs.
		*/
	@throws(classOf[InstagramException])
	def getUserLikedMediaFeed: Future[Response[MediaFeed]]

	/**
		* Get the authenticated user's list of media they've liked.
		*
		* @return a MediaFeed object.
		* @throws InstagramException
		* if any error occurs.
		*/
	@throws(classOf[InstagramException])
	def getUserLikedMediaFeed(maxLikeId: Option[Long], count: Option[Int]): Future[Response[MediaFeed]]

	/**
		* Search for a user by name.
		*
		* @param query
		* A query string.
		* @return a UserFeed object.
		* @throws InstagramException
		* if any error occurs.
		*/
	@throws(classOf[InstagramException])
	def searchUser(query: Option[String]): Future[Response[UserFeed]]

	/**
		* Search for a user by name.
		*
		* @param query
		* A query string.
		* @return a UserFeed object.
		* @throws InstagramException
		* if any error occurs.
		*/
	@throws(classOf[InstagramException])
	def searchUser(query: Option[String], count: Option[Int]): Future[Response[UserFeed]]

	/**
		* Get the list of 'users' the authenticated user follows.
		*
		* @param userId
		* userId of the User.
		* @return a UserFeed object.
		* @throws InstagramException
		* if any error occurs.
		*/
	@throws(classOf[InstagramException])
	def getUserFollowList(userId: Option[String]): Future[Response[UserFeed]]

	/**
		* Get the next page for list of 'users' the authenticated user follows.
		*
		* @throws InstagramException
		*/
	@throws(classOf[InstagramException])
	def getUserFollowListNextPage(userId: Option[String], cursor: Option[String]): Future[Response[UserFeed]]

	/**
		* Get the next page for list of 'users' the authenticated user follows.
		*
		* @param pagination
		* @throws InstagramException
		*/
	@throws(classOf[InstagramException])
	def getUserFollowListNextPage(pagination: Option[Pagination]): Future[Response[UserFeed]]

	/**
		* Get the list of 'users' the current given user is followed by.
		*
		* @param userId
		* userId of the User.
		* @return a UserFeed object.
		* @throws InstagramException
		* if any error occurs.
		*/
	@throws(classOf[InstagramException])
	def getUserFollowedByList(userId: Option[String]): Future[Response[UserFeed]]

	/**
		* Get the next page for list of 'users' the authenticated is followed by.
		*
		* @param userId
		* @param cursor
		* @throws InstagramException
		*/
	@throws(classOf[InstagramException])
	def getUserFollowedByListNextPage(userId: Option[String], cursor: Option[String]): Future[Response[UserFeed]]

	/**
		* Get the next page for list of 'users' the authenticated is followed by.
		*
		* @param pagination
		* @throws InstagramException
		*/
	@throws(classOf[InstagramException])
	def getUserFollowedByListNextPage(pagination: Option[Pagination]): Future[Response[UserFeed]]

	/**
		* Get a list of users who have requested this user's permission to follow
		*
		* @return a UserFeed object.
		* @throws InstagramException
		* if any error occurs.
		*/
	@throws(classOf[InstagramException])
	def getUserRequestedBy: Future[Response[UserFeed]]

	/**
		* Get information about the current user's relationship
		* (follow/following/etc) to another user.
		*
		* @param userId
		* userId of the User.
		* @return a Relationship feed object.
		* @throws InstagramException
		* if any error occurs.
		*/
	@throws(classOf[InstagramException])
	def getUserRelationship(userId: Option[String]): Future[Response[RelationshipFeed]]

	/**
		* Set the relationship between the current user and the target user
		*
		* @param userId
		* userId of the user.
		* @param relationship
		* Relationship status
		* @return a Relationship feed object
		* @throws InstagramException
		* if any error occurs.
		*/
	@throws(classOf[InstagramException])
	def setUserRelationship(userId: Option[String], relationship: Option[Relationship]): Future[Response[RelationshipFeed]]

	/**
		* Get information about a media object.
		*
		* @param mediaId
		* mediaId of the Media object.
		* @return a mediaFeed object.
		* @throws InstagramException
		* if any error occurs.
		*/
	@throws(classOf[InstagramException])
	def getMediaInfo(mediaId: Option[String]): Future[Response[MediaInfoFeed]]

	/**
		* Get information about a media object.
		*
		* @param shortCode
		* shortcode of the Media object.
		* @return a mediaFeed object.
		* @throws InstagramException
		* if any error occurs.
		*/
	@throws(classOf[InstagramException])
	def getMediaInfoByShortCode(shortCode: Option[String]): Future[Response[MediaInfoFeed]]

	/**
		* Search for media in a given area.
		*
		* @param latitude
		* Latitude of the center search coordinate.
		* @param longitude
		* Longitude of the center search coordinate.
		* @return a MediaFeed object.
		* @throws InstagramException
		* if any error occurs
		*/
	@throws(classOf[InstagramException])
	def searchMedia(latitude: Option[Double], longitude: Option[Double]): Future[Response[MediaFeed]]

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
		* @throws InstagramException
		* if any error occurs
		*/
	@throws(classOf[InstagramException])
	def searchMedia(latitude: Option[Double], longitude: Option[Double], distance: Option[Int]): Future[Response[MediaFeed]]

	/**
		* Get a full list of comments on a media.
		*
		* @param mediaId
		* a mediaId
		* @return a MediaCommentsFeed object.
		* @throws InstagramException
		* if any error occurs.
		*/
	@throws(classOf[InstagramException])
	def getMediaComments(mediaId: Option[String]): Future[Response[MediaCommentsFeed]]

	/**
		* Create a comment on a media.
		*
		* @param mediaId
		* a mediaId
		* @param text
		* Text to post as a comment on the media as specified in
		* media-id.
		* @return a MediaCommentResponse feed.
		* @throws InstagramException
		* if any error occurs.
		*/
	@throws(classOf[InstagramException])
	def setMediaComments(mediaId: Option[String], text: Option[String]): Future[Response[MediaCommentResponse]]

	/**
		* Remove a comment either on the authenticated user's media or authored by
		* the authenticated user.
		*
		* @param mediaId
		* a mediaId of the Media
		* @param commentId
		* a commentId of the Comment
		* @return a MediaCommentResponse feed.
		* @throws InstagramException
		* if any error occurs.
		*/
	@throws(classOf[InstagramException])
	def deleteMediaCommentById(mediaId: Option[String], commentId: Option[String]): Future[Response[MediaCommentResponse]]

	/**
		* Get a list of users who have liked this media.
		*
		* @param mediaId
		* a mediaId of the Media
		* @return a LikesFeed object.
		* @throws InstagramException
		* if any error occurs.
		*/
	@throws(classOf[InstagramException])
	def getUserLikes(mediaId: Option[String]): Future[Response[LikesFeed]]

	/**
		* Set a like on this media by the currently authenticated user.
		*
		* @param mediaId
		* a mediaId of the Media
		* @return a LikesFeed object.
		* @throws InstagramException
		* if any error occurs.
		*/
	@throws(classOf[InstagramException])
	def setUserLike(mediaId: Option[String]): Future[Response[LikesFeed]]

	/**
		* Remove a like on this media by the currently authenticated user.
		*
		* @param mediaId
		* a mediaId of the Media
		* @return a LikesFeed object.
		* @throws InstagramException
		* if any error occurs.
		*/
	@throws(classOf[InstagramException])
	def deleteUserLike(mediaId: Option[String]): Future[Response[LikesFeed]]

	/**
		* Get information about a tag object.
		*
		* @param tagName
		* name of the tag.
		* @return a TagInfoFeed object.
		* @throws InstagramException
		* if any error occurs.
		*/
	@throws(classOf[InstagramException])
	def getTagInfo(tagName: Option[String]): Future[Response[TagInfoFeed]]

	/**
		* Get a list of recently tagged media.
		*
		* @param tagName { @link String}
		*                        the name of the tag
		* @return { @link MediaFeed}
		*                 the media feed for the first page
		* @throws InstagramException
		* if any error occurs.
		*/
	@throws(classOf[InstagramException])
	def getRecentMediaFeedTags(tagName: Option[String]): Future[Response[MediaFeed]]

	/**
		* Get at most <em>count</em> number of recently tagged media.
		*
		* @param tagName { @link String}
		*                        the name of the tag
		* @param count { @code int}
		*                      set to 0 to use default
		* @return { @link MediaFeed}
		*                 the media feed for the first page
		* @throws InstagramException
		* if any error occurs.
		*/
	@throws(classOf[InstagramException])
	def getRecentMediaFeedTags(tagName: Option[String], count: Option[Long]): Future[Response[MediaFeed]]

	/**
		* Get a list of recently tagged media.
		*
		* @param tagName
		* name of the tag.
		* @param minTagId
		* (return media before this tag_id), can be null
		* @param maxTagId
		* (return media before this tag_id), can be null
		* @return { @link MediaFeed}
		*                 the media feed for the first page
		* @throws InstagramException
		* if any error occurs.
		*/
	@throws(classOf[InstagramException])
	def getRecentMediaFeedTags(tagName: Option[String], minTagId: Option[String], maxTagId: Option[String]): Future[Response[MediaFeed]]

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
		* @throws InstagramException
		* if any error occurs.
		*/
	@throws(classOf[InstagramException])
	def getRecentMediaFeedTags(tagName: Option[String], minTagId: Option[String], maxTagId: Option[String], count: Option[Long]): Future[Response[MediaFeed]]

	/**
		* Get a list of recently tagged media.
		*
		* @param tagName
		* name of the tag.
		* @return { @link MediaFeed}
		*                 the media feed for the first page
		* @throws InstagramException
		* if any error occurs.
		*/
	@throws(classOf[InstagramException])
	def getRecentMediaFeedTagsByRegularIds(tagName: Option[String], minId: Option[String], maxId: Option[String]): Future[Response[MediaFeed]]

	/**
		* Search for tags by name - results are ordered first as an exact match,
		* then by popularity.
		*
		* @param tagName
		* name of the tag
		* @return a TagSearchFeed object.
		* @throws InstagramException
		* if any error occurs.
		*/
	@throws(classOf[InstagramException])
	def searchTags(tagName: Option[String]): Future[Response[TagSearchFeed]]

	/**
		* Get information about a location.
		*
		* @param locationId
		* an id of the Location
		* @return a LocationInfo object.
		* @throws InstagramException
		* if any error occurs.
		*/
	@throws(classOf[InstagramException])
	def getLocationInfo(locationId: Option[String]): Future[Response[LocationInfo]]

	/**
		* Get a list of recent media objects from a given location.
		*
		* @param locationId
		* a id of the Media.
		* @return a MediaFeed object.
		* @throws InstagramException
		* if any error occurs.
		*/
	@throws(classOf[InstagramException])
	def getRecentMediaByLocation(locationId: Option[String]): Future[Response[MediaFeed]]

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
		* @throws InstagramException
		* if any error occurs.
		*/
	@throws(classOf[InstagramException])
	def getRecentMediaByLocation(locationId: Option[String], minId: Option[String], maxId: Option[String]): Future[Response[MediaFeed]]

	/**
		* Search for a location by geographic coordinate.
		*
		* @param latitude
		* Latitude of the center search coordinate.
		* @param longitude
		* Longitude of the center search coordinate.
		* @return a LocationSearchFeed object.
		* @throws InstagramException
		* if any error occurs.
		*/
	@throws(classOf[InstagramException])
	def searchLocation(latitude: Option[Double], longitude: Option[Double]): Future[Response[LocationSearchFeed]]

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
		* @throws InstagramException
		* if any error occurs.
		*/
	@throws(classOf[InstagramException])
	def searchLocation(latitude: Option[Double], longitude: Option[Double], distance: Option[Int]): Future[Response[LocationSearchFeed]]

	/**
		* Search for a location by v2 Foursquare id.
		*
		* @param foursquareId
		* Foursquare Venue ID of the location
		* @return a LocationSearchFeed object.
		* @throws InstagramException
		* if any error occurs.
		*/
	@throws(classOf[InstagramException])
	def searchFoursquareVenue(foursquareId: Option[String]): Future[Response[LocationSearchFeed]]

	/**
		* Search for a location by Facebook places id.
		*
		* @param facebookPlacesId
		* Facebook places id of the location
		* @return a LocationSearchFeed object.
		* @throws InstagramException
		* if any error occurs.
		*/
	@throws(classOf[InstagramException])
	def searchFacebookPlace(facebookPlacesId: Option[String]): Future[Response[LocationSearchFeed]]

}
