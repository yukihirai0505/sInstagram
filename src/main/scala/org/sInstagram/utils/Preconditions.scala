package org.sInstagram.utils

/**
  * author Yuki Hirai on 2016/11/09.
  */
object Preconditions {

  private val DEFAULT_MESSAGE: String = "Received an invalid parameter"
  /**
    * Checks that a string is not null or empty
    *
    * @param string   any string
    * @param errorMsg error message
    * @throws IllegalArgumentException if the string is null or empty
    */
  def checkEmptyString(string: String, errorMsg: String) {
    check(!string.isEmpty, errorMsg)
  }

  private def check(requirements: Boolean, error: String) {
    val message: String = if (error.isEmpty) DEFAULT_MESSAGE else error
    if (!requirements) {
      throw new IllegalArgumentException(message)
    }
  }
}
