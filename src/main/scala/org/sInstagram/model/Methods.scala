package org.sInstagram.model

/**
	* author Yuki Hirai on 2016/11/08.
	*/
object Methods {
	/**
		* Get basic information about a user.
		*/
	lazy val USERS_WITH_ID: String = "/users/%s"

	/**
		* Get basic information about a user (self).
		*/
	val USERS_SELF: String = "/users/self"

	/**
		* Get the most recent media published by the owner of the access_token.
		*/
	lazy val USERS_SELF_RECENT_MEDIA: String = "/users/self/media/recent/"

	/**
		* Get the most recent media published by a user.
		*/
	val USERS_RECENT_MEDIA: String = "/users/%s/media/recent"

  /**
    * Get a full list of comments on a media.
    *
    * Required scope: comments
    */
  val MEDIA_COMMENTS: String = "/media/%s/comments"

  /**
    * Get the list of users this user follows.
    *
    * Required scope: relationships
    */
  val USERS_ID_FOLLOWS: String = "/users/%s/follows"
  /**
    * See the authenticated user's list of media they've liked. Note that this
    * list is ordered by the order in which the user liked the media. Private
    * media is returned as long as the authenticated user has permission to
    * view that media. Liked media lists are only available for the currently
    * authenticated user.
    */
  val USERS_SELF_LIKED_MEDIA: String = "/users/self/media/liked"

  /**
    * Search for a user by name.
    */
  val USERS_SEARCH: String = "/users/search"

  /**
    * Get a list of users who have liked this media.
    *
    * Required scope : likes
    */
  val LIKES_BY_MEDIA_ID: String = "/media/%s/likes"

  /**
    * Get information about a location.
    */
  val LOCATIONS_BY_ID: String = "/locations/%s"

  /**
    * Search for a location by geographic coordinate.
    */
  val LOCATIONS_SEARCH: String = "/locations/search"
  /**
    * Get a list of recent media objects from a given location.
    */
  val LOCATIONS_RECENT_MEDIA_BY_ID: String = "/locations/%s/media/recent"

  /**
    * Get information about a media object.
    */
  val MEDIA_BY_ID: String = "/media/%s"

  /**
    * Get information about a tag object.
    */
  val TAGS_BY_NAME: String = "/tags/%s"
}