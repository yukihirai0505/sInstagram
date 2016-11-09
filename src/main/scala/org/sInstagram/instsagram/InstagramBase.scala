package org.sInstagram.instsagram

import com.netaporter.uri.Uri._
import dispatch._
import org.sInstagram.Authentication
import org.sInstagram.http.{Verbs, Response, Request}
import org.sInstagram.model.{Relationship, QueryParam, Methods, Constants}
import org.sInstagram.responses.auth.Authentication
import org.sInstagram.responses.comments.{MediaCommentsFeed, MediaCommentResponse}
import org.sInstagram.responses.common.Pagination
import org.sInstagram.responses.likes.LikesFeed
import org.sInstagram.responses.locations.{LocationInfo, LocationSearchFeed}
import org.sInstagram.responses.media.{MediaInfoFeed, MediaFeed}
import org.sInstagram.responses.relationships.RelationshipFeed
import org.sInstagram.responses.tags.{TagInfoFeed, TagSearchFeed}
import org.sInstagram.responses.users.basicinfo.UserInfo
import org.sInstagram.responses.auth.SignedAccessToken
import org.sInstagram.responses.users.feed.UserFeed
import org.sInstagram.utils.Preconditions
import play.api.libs.json.Reads


/**
	* author Yuki Hirai on 2016/11/09.
	*/
class InstagramBase(accessToken: String) extends InstagramClient {
	protected val USER_ID_CANNOT_BE_NULL_OR_EMPTY: String = "UserId cannot be null or empty."

	protected def addSecureSigIfNeeded(auth: Authentication, url: String, postData: Option[Map[String,String]] = None)
	: String = auth match {
		case SignedAccessToken(_, secret) =>
			val uri = parse(url)
			val params = uri.query.params
			val sig = Authentication.createSignedParam(secret, uri.pathRaw.replace("/v1", ""), concatMapOpt(postData, params.toMap))
			uri.addParam("sig", sig).toStringRaw
		case _ => url
	}

	protected def concatMapOpt(postData: Option[Map[String,String]], params: Map[String,Option[String]])
	: Map[String,Option[String]] = postData match {
		case Some(m) => params ++ m.mapValues(Some(_))
		case _ => params
	}

	def request[T](verbs: Verbs, apiPath: String, params: Option[Map[String, Seq[String]]] = None)(implicit r: Reads[T]): Future[Response[T]] = {
		val effectiveUrl = s"${Constants.API_URL}$apiPath?access_token=$accessToken"
		print(effectiveUrl)
		val parameters: Map[String, Seq[String]] = if (params.isDefined) params.get else Map()
		val request = url(effectiveUrl).setMethod(verbs.method).setParameters(parameters)
		Request.send[T](request)
	}

	override def getUserInfo(userId: String): Future[Response[UserInfo]] = {
		Preconditions.checkEmptyString(userId, USER_ID_CANNOT_BE_NULL_OR_EMPTY)
		val apiPath: String = Methods.USERS_WITH_ID format userId
		request[UserInfo](Verbs.GET, apiPath)
	}

	override def getCurrentUserInfo: Future[Response[UserInfo]] = {
		request[UserInfo](Verbs.GET, Methods.USERS_SELF)
	}

	override def getUserRecentMedia(count: Option[Int] = None, minId: Option[String] = None, maxId: Option[String] = None): Future[Response[MediaFeed]] = {
		val params: Map[String, Seq[String]] = Map(
			QueryParam.COUNT -> Seq(count.mkString),
			QueryParam.MIN_ID -> Seq(minId.mkString),
			QueryParam.MAX_ID -> Seq(maxId.mkString)
		)
		request[MediaFeed](Verbs.GET, Methods.USERS_SELF_RECENT_MEDIA, Option(params))
	}

	override def getRecentMediaFeed(userId: Option[String], count: Option[Int], minId: Option[String], maxId: Option[String]): MediaFeed = ???

	override def getMediaComments(mediaId: Option[String]): Future[Response[MediaCommentsFeed]] = ???

	override def getUserFollowList(userId: Option[String]): Future[Response[UserFeed]] = ???

	override def getUserFollowListNextPage(userId: Option[String], cursor: Option[String]): Future[Response[UserFeed]] = ???

	override def getUserFollowListNextPage(pagination: Option[Pagination]): Future[Response[UserFeed]] = ???

	override def getUserLikedMediaFeed: Future[Response[MediaFeed]] = ???

	override def getUserLikedMediaFeed(maxLikeId: Option[Long], count: Option[Int]): Future[Response[MediaFeed]] = ???

	override def searchUser(query: Option[String]): Future[Response[UserFeed]] = ???

	override def searchUser(query: Option[String], count: Option[Int]): Future[Response[UserFeed]] = ???

	override def getLocationInfo(locationId: Option[String]): Future[Response[LocationInfo]] = ???

	override def getTagInfo(tagName: Option[String]): Future[Response[TagInfoFeed]] = ???

	override def searchLocation(latitude: Option[Double], longitude: Option[Double]): Future[Response[LocationSearchFeed]] = ???

	override def searchLocation(latitude: Option[Double], longitude: Option[Double], distance: Option[Int]): Future[Response[LocationSearchFeed]] = ???

	override def getRecentMediaByLocation(locationId: Option[String]): Future[Response[MediaFeed]] = ???

	override def getRecentMediaByLocation(locationId: Option[String], minId: Option[String], maxId: Option[String]): Future[Response[MediaFeed]] = ???

	override def setUserLike(mediaId: Option[String]): Future[Response[LikesFeed]] = ???

	override def getMediaInfo(mediaId: Option[String]): Future[Response[MediaInfoFeed]] = ???

	override def getRecentMediaNextPage(pagination: Option[Pagination]): Future[Response[MediaFeed]] = ???

	override def deleteUserLike(mediaId: Option[String]): Future[Response[LikesFeed]] = ???

	override def searchFoursquareVenue(foursquareId: Option[String]): Future[Response[LocationSearchFeed]] = ???

	override def deleteMediaCommentById(mediaId: Option[String], commentId: Option[String]): Future[Response[MediaCommentResponse]] = ???

	override def getUserFollowedByListNextPage(userId: Option[String], cursor: Option[String]): Future[Response[UserFeed]] = ???

	override def getUserFollowedByListNextPage(pagination: Option[Pagination]): Future[Response[UserFeed]] = ???

	override def setMediaComments(mediaId: Option[String], text: Option[String]): Future[Response[MediaCommentResponse]] = ???

	override def getMediaInfoByShortCode(shortCode: Option[String]): Future[Response[MediaInfoFeed]] = ???

	override def searchTags(tagName: Option[String]): Future[Response[TagSearchFeed]] = ???

	override def setUserRelationship(userId: Option[String], relationship: Option[Relationship]): Future[Response[RelationshipFeed]] = ???

	override def getUserFollowedByList(userId: Option[String]): Future[Response[UserFeed]] = ???

	override def getUserRequestedBy: Future[Response[UserFeed]] = ???

	override def getUserRelationship(userId: Option[String]): Future[Response[RelationshipFeed]] = ???

	override def searchFacebookPlace(facebookPlacesId: Option[String]): Future[Response[LocationSearchFeed]] = ???

	override def searchMedia(latitude: Option[Double], longitude: Option[Double]): Future[Response[MediaFeed]] = ???

	override def searchMedia(latitude: Option[Double], longitude: Option[Double], distance: Option[Int]): Future[Response[MediaFeed]] = ???

	override def getRecentMediaFeedTagsByRegularIds(tagName: Option[String], minId: Option[String], maxId: Option[String]): Future[Response[MediaFeed]] = ???

	override def getRecentMediaFeedTags(tagName: Option[String]): Future[Response[MediaFeed]] = ???

	override def getRecentMediaFeedTags(tagName: Option[String], count: Option[Long]): Future[Response[MediaFeed]] = ???

	override def getRecentMediaFeedTags(tagName: Option[String], minTagId: Option[String], maxTagId: Option[String]): Future[Response[MediaFeed]] = ???

	override def getRecentMediaFeedTags(tagName: Option[String], minTagId: Option[String], maxTagId: Option[String], count: Option[Long]): Future[Response[MediaFeed]] = ???

	override def getUserLikes(mediaId: Option[String]): Future[Response[LikesFeed]] = ???
}