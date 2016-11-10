package org.sInstagram.exceptions

/**
  * Created by yukihirai on 2016/11/09.
  */
class OAuthException(message: String, e: Exception = null) extends RuntimeException(message, e)
