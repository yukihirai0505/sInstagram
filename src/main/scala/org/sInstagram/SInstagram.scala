package org.sInstagram

import org.sInstagram.http.{Response, Request}
import org.sInstagram.instsagram.InstagramBase
import dispatch._
import org.sInstagram.model.Constants
import org.sInstagram.responses.auth.Auth
import org.sInstagram.responses.comments.MediaCommentsFeed
import org.sInstagram.responses.common.User
import org.sInstagram.responses.locations.LocationSearchFeed
import org.sInstagram.responses.media.MediaFeed
import org.sInstagram.responses.relationships.RelationshipFeed
import org.sInstagram.responses.tags.TagInfoFeed
import org.sInstagram.responses.users.basicinfo.UserInfo

class SInstagram(accessToken: String) extends InstagramBase(accessToken) {

  /**
   * Get basic information about a user.
   *
   * @param auth   Credentials.
   * @param userId Id-number of the name to get information about.
   * @return       A Future of Response[Profile].
   */
  def userInfo(auth: Auth, userId: String): Future[Response[UserInfo]] = {
    val stringAuth = Authentication.toGETParams(auth)
    val urlString = addSecureSigIfNeeded(auth, s"${Constants.API_URL}/users/$userId?$stringAuth")
    val request = url(urlString)
    Request.send[UserInfo](request)
  }

  /**
   * See the authenticated user's list of liked media.
   *
   * @param auth      Credentials.
   * @param count     Max number of results to return.
   * @param maxLikeId Return media liked before this id.
   * @return          A Future of a Response of a List[Media].
   */
  def liked(auth: Auth, count: Option[Int] = None, maxLikeId: Option[String] = None)
  : Future[Response[List[MediaFeed]]] = {
    val stringAuth = Authentication.toGETParams(auth)
    val urlString = addSecureSigIfNeeded(auth,
      s"${Constants.API_URL}/users/self/media/liked?$stringAuth&count=${count.mkString}" +
      s"&max_like_id=${maxLikeId.mkString}")
    val request = url(urlString)
    Request.send[List[MediaFeed]](request)
  }

  /**
   * Search for a user by name.
   *
   * @param auth  Credentials.
   * @param name  Name to search a user for.
   * @param count Max number of results to return.
   * @return      A Future of a Response of a List[UserSearch].
   */
  def userSearch(auth: Auth, name: String, count: Option[Int] = None)
  : Future[Response[List[UserInfo]]] = {
    val stringAuth = Authentication.toGETParams(auth)
    val urlString = addSecureSigIfNeeded(auth,
      s"${Constants.API_URL}/users/search?$stringAuth&q=$name&count=${count.mkString}")
    val request = url(urlString)
    Request.send[List[UserInfo]](request)
  }

  /**
   * Get the list of users this user follows.
   *
   * @param auth   Credentials.
   * @param userId Instagram ID of the user.
   * @param count  Max number of results to return.
   * @param cursor Return users after this cursor.
   * @return       A Future of a Response of a List[User].
   */
  def follows(auth: Auth, userId: String, count: Option[Int] = None, cursor: Option[String] = None)
  : Future[Response[List[User]]] = {
    val stringAuth = Authentication.toGETParams(auth)
    val urlString = addSecureSigIfNeeded(auth,
      s"${Constants.API_URL}/users/$userId/follows?$stringAuth&count=${count.mkString}&cursor=${cursor.mkString}")
    val request = url(urlString)
    Request.send[List[User]](request)
  }

  /**
   * Get the list of users this user is followed by.
   *
   * @param auth   Credentials.
   * @param userId Instagram ID of the user.
   * @param count  Max number of results to return.
   * @param cursor Return users after this cursor.
   * @return       A Future of a Response of a List[User].
   */
  def followedBy(auth: Auth, userId: String, count: Option[Int] = None, cursor: Option[String] = None)
  : Future[Response[List[User]]] = {
    val stringAuth = Authentication.toGETParams(auth)
    val urlString = addSecureSigIfNeeded(auth,
      s"${Constants.API_URL}/users/$userId/followed-by?$stringAuth&count=${count.mkString}&cursor=${cursor.mkString}")
    val request = url(urlString)
    Request.send[List[User]](request)
  }

  /**
   * View users who has sent a follow request.
   *
   * @param auth   Credentials.
   * @param count  Max number of results to return.
   * @param cursor Return users after this cursor.
   * @return       A Future of a Response of a List[User].
   */
  def relationshipRequests(auth: Auth, count: Option[Int] = None, cursor: Option[String] = None)
  : Future[Response[List[User]]] = {
    val stringAuth = Authentication.toGETParams(auth)
    val urlString = addSecureSigIfNeeded(auth,
      s"${Constants.API_URL}/users/self/requested-by?$stringAuth&count=${count.mkString}&cursor=${cursor.mkString}")
    val request = url(urlString)
    Request.send[List[User]](request)
  }

  /**
   * Get information about a relationship to another user.
   *
   * @param auth   Credentials.
   * @param userId Instagram ID of the user.
   * @return       A Future of a Response of a Relationship.
   */
  def relationship(auth: Auth, userId: String): Future[Response[RelationshipFeed]] = {
    val stringAuth = Authentication.toGETParams(auth)
    val urlString = addSecureSigIfNeeded(auth,
      s"${Constants.API_URL}/users/$userId/relationship?$stringAuth")
    val request = url(urlString)
    Request.send[RelationshipFeed](request)
  }

  /**
   * Send the request to update the relationship status.
   * This method is called from the methods named relationshipXXX.
   *
   * @param auth         Credentials.
   * @param userId       Instagram ID of the user.
   * @param action       Action (follow/unfollow/block/unblock/approve/deny).
   * @return             A Future of a Response of a Relationship.
   */
  private def updateRelationship(auth: Auth, userId: String, action: String)
  : Future[Response[RelationshipFeed]] = {
    val stringAuth = Authentication.toGETParams(auth)
    val actionMap = Map("action" -> action)
    val urlString = addSecureSigIfNeeded(auth,
      s"${Constants.API_URL}/users/$userId/relationship?$stringAuth", Some(actionMap))
    val request = url(urlString) << actionMap
    Request.send[RelationshipFeed](request)
  }

  /**
   * Follow a user.
   *
   * @param auth         Credentials.
   * @param userId       Instagram ID of the user.
   * @return             A Future of a Response of a Relationship.
   */
  def relationshipFollow(auth: Auth, userId: String)
  : Future[Response[RelationshipFeed]] = {
    updateRelationship(auth, userId, action = "follow")
  }

  /**
   * Unfollow a user.
   *
   * @param auth         Credentials.
   * @param userId       Instagram ID of the user.
   * @return             A Future of a Response of a Relationship.
   */
  def relationshipUnfollow(auth: Auth, userId: String)
  : Future[Response[RelationshipFeed]] = {
    updateRelationship(auth, userId, action = "unfollow")
  }

  /**
   * Block a user.
   *
   * @param auth         Credentials.
   * @param userId       Instagram ID of the user.
   * @return             A Future of a Response of a Relationship.
   */
  def relationshipBlock(auth: Auth, userId: String)
  : Future[Response[RelationshipFeed]] = {
    updateRelationship(auth, userId, action = "block")
  }

  /**
   * Unblock a user.
   *
   * @param auth         Credentials.
   * @param userId       Instagram ID of the user.
   * @return             A Future of a Response of a Relationship.
   */
  def relationshipUnblock(auth: Auth, userId: String)
  : Future[Response[RelationshipFeed]] = {
    updateRelationship(auth, userId, action = "unblock")
  }

  /**
   * Approve a follow request from a user.
   *
   * @param auth         Credentials.
   * @param userId       Instagram ID of the user.
   * @return             A Future of a Response of a Relationship.
   */
  def relationshipApprove(auth: Auth, userId: String)
  : Future[Response[RelationshipFeed]] = {
    updateRelationship(auth, userId, action = "approve")
  }

  /**
   * Ignore a follow request from a user.
   *
   * @param auth         Credentials.
   * @param userId       Instagram ID of the user.
   * @return             A Future of a Response of a Relationship.
   */
  def relationshipIgnore(auth: Auth, userId: String)
  : Future[Response[RelationshipFeed]] = {
    updateRelationship(auth, userId, action = "ignore")
  }

  /**
   * Get information about a media object.
   *
   * @param auth    Credentials.
   * @param mediaId ID of an Instagram media.
   * @return        A Future of a Response of a Media.
   */
  def media(auth: Auth, mediaId: String): Future[Response[MediaFeed]] = {
    val stringAuth = Authentication.toGETParams(auth)
    val urlString = addSecureSigIfNeeded(auth,
      s"${Constants.API_URL}/media/$mediaId?$stringAuth")
    val request = url(urlString)
    Request.send[MediaFeed](request)
  }

  /**
   * Get information about a media object.
   *
   * @param auth      Credentials.
   * @param shortcode Shortcode of an Instagram ID.
   *                  A media object's shortcode can be found in its shortlink URL.
                      An example shortlink is http://instagram.com/p/D/
                      Its corresponding shortcode is D.
   * @return          A Future of a Response of a Media.
   */
  def mediaShortcode(auth: Auth, shortcode: String): Future[Response[MediaFeed]] = {
    val stringAuth = Authentication.toGETParams(auth)
    val urlString = addSecureSigIfNeeded(auth,
      s"${Constants.API_URL}/media/shortcode/$shortcode?$stringAuth")
    val request = url(urlString)
    Request.send[MediaFeed](request)
  }

  /**
   * Search for media in a given area.
   * The default time span is set to 5 days. The time span must not exceed 7 days. Defaults time stamps cover the last 5 days.
   *
   * @param auth         Credentials.
   * @param coordinates  Tuple2: Latitude & Longitude coordinates.
   * @param minTimestamp Return media after this UNIX timestamp.
   * @param maxTimestamp Return media before this UNIX timestamp.
   * @param distance     Default is 1000m (distance=1000), max distance is 5000.
   * @return             A Future of a Response of a List of Media.
   */
  def mediaSearch(auth: Auth, coordinates: (Double, Double), minTimestamp: Option[String] = None,
                  maxTimestamp: Option[String] = None, distance: Option[Int] = None)
  : Future[Response[List[MediaFeed]]] = {
    val stringAuth = Authentication.toGETParams(auth)
    val urlString = addSecureSigIfNeeded(auth,
      s"${Constants.API_URL}/media/search?$stringAuth&lat=${coordinates._1.toString}&lng=${coordinates._2.toString}" +
      s"&min_timestamp=${minTimestamp.mkString}&max_timestamp=${maxTimestamp.mkString}&distance=${distance.mkString}")
    val request = url(urlString)
    Request.send[List[MediaFeed]](request)
  }

  /**
   * Get a list of currently popular media.
   *
   * @param auth Credentials.
   * @return     A Future of a Response of a List of Media.
   */
  def popular(auth: Auth): Future[Response[List[MediaFeed]]] = {
    val stringAuth = Authentication.toGETParams(auth)
    val urlString = addSecureSigIfNeeded(auth,
      s"${Constants.API_URL}/media/popular?$stringAuth")
    val request = url(urlString)
    Request.send[List[MediaFeed]](request)
  }

  /**
   * Get a full list of comments on a media.
   *
   * @param auth    Credentials.
   * @param mediaId Id-number of media object.
   * @return        A Future of a Response of a List of Comment.
   */
  def comments(auth: Auth, mediaId: String): Future[Response[List[MediaCommentsFeed]]] = {
    val stringAuth = Authentication.toGETParams(auth)
    val urlString = addSecureSigIfNeeded(auth,
      s"${Constants.API_URL}/media/$mediaId/comments?$stringAuth")
    val request = url(urlString)
    Request.send[List[MediaCommentsFeed]](request)
  }

  /**
   * Create a comment on a media.
   * Please email apidevelopers[at]instagram.com or visit http://bit.ly/instacomments for access.
   *
   * @param auth         Credentials.
   * @param mediaId      Id-number of media object.
   * @param comment      The actual comment. See http://instagram.com/developer/endpoints/comments/#post_media_comments
   *                     for the list of restrictions (anti-spam).
   * @return             A Future of a Response of Option[String].
   */
  def comment(auth: Auth, mediaId: String, comment: String)
  : Future[Response[Option[String]]] = {
    val stringAuth = Authentication.toGETParams(auth)
    val commentTextMap = Map("text" -> comment)
    val urlString = addSecureSigIfNeeded(auth,
      s"${Constants.API_URL}/media/$mediaId/comments?$stringAuth", Some(commentTextMap))
    val request = url(urlString) << commentTextMap
    Request.send[Option[String]](request)
  }

  /**
   * Remove a comment either on the authenticated user's media object or authored by the authenticated user.
   *
   * @param auth         Credentials.
   * @param mediaId      Id-number of media object.
   * @param commentId    Id-number of the comment.
   * @return             A Future of a Response of Option[String].
   */
  def commentDelete(auth: Auth, mediaId: String, commentId: String)
  : Future[Response[Option[String]]] = {
    val stringAuth = Authentication.toGETParams(auth)
    val urlString = addSecureSigIfNeeded(auth,
      s"${Constants.API_URL}/media/$mediaId/comments/$commentId?$stringAuth")
    val request = url(urlString).DELETE
    Request.send[Option[String]](request)
  }

  /**
   * Get a list of users who have liked this media.
   *
   * @param auth    Credentials.
   * @param mediaId Id-number of media object.
   * @return        A Future of a Response of a List of User.
   */
  def likes(auth: Auth, mediaId: String): Future[Response[List[User]]] = {
    val stringAuth = Authentication.toGETParams(auth)
    val urlString = addSecureSigIfNeeded(auth,
      s"${Constants.API_URL}/media/$mediaId/likes?$stringAuth")
    val request = url(urlString)
    Request.send[List[User]](request)
  }

  /**
   * Set a like on this media by the currently authenticated user.
   *
   * @param auth         Credentials.
   * @param mediaId      Id-number of media object.
   * @return             A Future of a Response of Option[String].
   */
  def like(auth: Auth, mediaId: String)
  : Future[Response[Option[String]]] = {
    val stringAuth = Authentication.toGETParams(auth)
    val urlString = addSecureSigIfNeeded(auth,
      s"${Constants.API_URL}/media/$mediaId/likes?$stringAuth")
    val request = url(urlString).POST
    Request.send[Option[String]](request)
  }

  /**
   * Remove a like on this media by the currently authenticated user.
   *
   * @param auth         Credentials.
   * @param mediaId      Id-number of media object.
   * @return             A Future of a Response of Option[String].
   */
  def unlike(auth: Auth, mediaId: String)
  : Future[Response[Option[String]]] = {
    val stringAuth = Authentication.toGETParams(auth)
    val urlString = addSecureSigIfNeeded(auth,
      s"${Constants.API_URL}/media/$mediaId/likes?$stringAuth")
    val request = url(urlString).DELETE
    Request.send[Option[String]](request)
  }

  /**
   * Get information about a tag object.
   *
   * @param auth Credentials.
   * @param tag  A valid tag name without a leading #. (eg. snowy, nofilter).
   * @return     A Future of a Response of a Tag
   */
  def tagInformation(auth: Auth, tag: String): Future[Response[TagInfoFeed]] = {
    val stringAuth = Authentication.toGETParams(auth)
    val urlString = addSecureSigIfNeeded(auth,
      s"${Constants.API_URL}/tags/$tag?$stringAuth")
    val request = url(urlString)
    Request.send[TagInfoFeed](request)
  }

  /**
   * Get a list of recently tagged media.
   *
   * @param auth     Credentials.
   * @param tag      A valid tag name without a leading #. (eg. snowy, nofilter).
   * @param minTagId Return media later than this.
   * @param maxTagId Return media earlier than this.
   * @return         A Future of a Response of a List of Media.
   */
  def tagRecent(auth: Auth, tag: String, minTagId: Option[String] = None, maxTagId: Option[String] = None)
    : Future[Response[List[MediaFeed]]] = {
    val stringAuth = Authentication.toGETParams(auth)
    val urlString = addSecureSigIfNeeded(auth,
      s"${Constants.API_URL}/tags/$tag/media/recent?$stringAuth" +
      s"&min_tag_id=${minTagId.mkString}&max_tag_id=${maxTagId.mkString}")
    val request = url(urlString)
    Request.send[List[MediaFeed]](request)
  }

  /**
   * Search for tags by name. Results are ordered first as an exact match, then by popularity.
   * Short tags will be treated as exact matches.
   *
   * @param auth Credentials.
   * @param tag  A valid tag name without a leading #. (eg. snowy, nofilter).
   * @return     A Future of a Response of a List of Tag.
   */
  def tagSearch(auth: Auth, tag: String): Future[Response[List[TagInfoFeed]]] = {
    val stringAuth = Authentication.toGETParams(auth)
    val urlString = addSecureSigIfNeeded(auth,
      s"${Constants.API_URL}/tags/search?q=$tag&$stringAuth")
    val request = url(urlString)
    Request.send[List[TagInfoFeed]](request)
  }

  /**
   * Get information about a location.
   *
   * @param auth       Credentials.
   * @param locationId ID-number of the location.
   * @return           A Future of a Response of a LocationSearch.
   */
  def location(auth: Auth, locationId: String): Future[Response[LocationSearchFeed]] = {
    val stringAuth = Authentication.toGETParams(auth)
    val urlString = addSecureSigIfNeeded(auth,
      s"${Constants.API_URL}/locations/$locationId?$stringAuth")
    val request = url(urlString)
    Request.send[LocationSearchFeed](request)
  }

  /**
   * Get a list of recent media objects from a given location.
   * May return a mix of both image and video types.
   *
   * @param auth         Credentials.
   * @param locationId   ID-number of the location.
   * @param minTimestamp Return media after this UNIX timestamp.
   * @param maxTimestamp Return media before this UNIX timestamp.
   * @param minId        Return media later than this.
   * @param maxId        Return media earlier than this.
   * @return             A Future of a Response of a List of Media.
   */
  def locationMedia(auth: Auth, locationId: String, minTimestamp: Option[String] = None,
                    maxTimestamp: Option[String] = None, minId: Option[String] = None,
                    maxId: Option[String] = None)
  : Future[Response[List[MediaFeed]]] = {
    val stringAuth = Authentication.toGETParams(auth)
    val urlString = addSecureSigIfNeeded(auth,
      s"${Constants.API_URL}/locations/$locationId/media/recent?$stringAuth" +
      s"&min_timestamp=${minTimestamp.mkString}&max_timestamp=${maxTimestamp.mkString}" +
      s"&min_id=${minId.mkString}&max_id=${maxId.mkString}")
    val request = url(urlString)
    Request.send[List[MediaFeed]](request)
  }

  /**
   * Search for a location by geographic coordinate.
   *
   * @param auth             Credentials
   * @param coordinates      Latitude & Longitude coordinates.
   * @param distance         Default is 1000m (distance=1000), max distance is 5000.
   * @param facebookPlacesId Returns a location mapped off of a Facebook places id.
   *                         If used, a Foursquare id and lat, lng are not required.
   * @param foursquareV2Id   Returns a location mapped off of a foursquare v2 api location id.
   *                         If used, you are not required to use lat and lng.
   * @return                 A Future of a Response of a List of LocationSearch.
   */
  def locationSearch(auth: Auth, coordinates: Option[(Double, Double)], distance: Option[Int] = None,
                     facebookPlacesId: Option[String] = None, foursquareV2Id: Option[String] = None)
  : Future[Response[List[LocationSearchFeed]]] = {
    val stringAuth = Authentication.toGETParams(auth)
    val latitudeLongitude =
      if (coordinates.isDefined) s"&lat=${coordinates.get._1.toString}&lng=${coordinates.get._2.toString}"
      else ""
    val urlString = addSecureSigIfNeeded(auth,
      s"${Constants.API_URL}/locations/search?$stringAuth&facebook_places_id=${facebookPlacesId.mkString}" +
      s"&foursquare_v2_id=${foursquareV2Id.mkString}$latitudeLongitude")
    val request = url(urlString)
    Request.send[List[LocationSearchFeed]](request)
  }

}
