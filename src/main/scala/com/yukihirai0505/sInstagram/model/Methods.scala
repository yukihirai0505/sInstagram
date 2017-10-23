package com.yukihirai0505.sInstagram.model

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
    * List the users who have requested this user's permission to follow
    *
    * Required scope: relationships
    */
  val USERS_SELF_REQUESTED_BY: String = "/users/self/requested-by"

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
    * Get information about a media object.
    */
  val MEDIA_BY_SHORT_CODE: String = "/media/shortcode/%s"

  /**
    * Get information about a media object.
    */
  val MEDIA_BY_ID: String = "/media/%s"

  /**
    * Search for media in a given area.
    */
  val MEDIA_SEARCH: String = "/media/search"

  /**
    * Get a list of recently tagged media. Note that this media is ordered by
    * when the media was tagged with this tag, rather than the order it was
    * posted. Use the max_tag_id and min_tag_id parameters in the pagination
    * response to paginate through these objects.
    */
  val TAGS_RECENT_MEDIA: String = "/tags/%s/media/recent"

  /**
    * Get the list of users this user follows.
    *
    * Required scope: relationships
    */
  val USERS_ID_FOLLOWS: String = "/users/self/follows"

  /**
    * Get the list of users this user is followed by.
    *
    * Required scope: relationships
    */
  val USERS_ID_FOLLOWED_BY: String = "/users/%s/followed-by"

  /**
    * Get information about the current user's relationship
    * (follow/following/etc) to another user.
    *
    * Required scope: relationships
    */
  val USERS_ID_RELATIONSHIP: String = "/users/%s/relationship"

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
    * Remove a comment either on the authenticated user's media or authored by
    * the authenticated user.
    *
    * Required scope: comments
    *
    * DELETE /media/{id}/comments/{id}
    *
    * this method is valid for only own media feed
    */
  val DELETE_MEDIA_COMMENTS: String = "/media/%s/comments/%s"

  /**
    * Get information about a tag object.
    */
  val TAGS_BY_NAME: String = "/tags/%s"

  /**
    * Search for tags by name - results are ordered first as an exact match,
    * then by popularity.
    */
  val TAGS_SEARCH: String = "/tags/search"
}