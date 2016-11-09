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
}