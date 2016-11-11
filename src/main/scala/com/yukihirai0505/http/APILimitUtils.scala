package com.yukihirai0505.http

/**
  * Helper class to process the HTTP headers from a Response object
  * and get the available and remaining API limit status
  * author Yuki Hirai on 2016/11/08.
  */
object APILimitUtils {

  protected val LIMIT_HEADER_KEY: String = "X-Ratelimit-Limit"
  protected val REMAINING_HEADER_KEY: String = "X-Ratelimit-Remaining"

  /**
    * Get the available API limit. It correspond to the value of
    * X-Ratelimit-Limit key in HTTP response headers. For Instagram
    * v1 API, this method should return 5000.
    *
    * @param headers HTTP headers from a Response object
    * @return Available API limit. -1 if response header is invalid or does not contains the API
    *         limit information
    */
  def getAPILimitStatus(headers: Map[String, String]): Int = {
    APILimitUtils.getIntegerValue(headers, LIMIT_HEADER_KEY)
  }

  /**
    * Get the remaining API limit. It correspond to the value of
    * X-Ratelimit-Remaining key in HTTP response headers.
    *
    * @param headers HTTP headers from a Response object
    * @return Remaining API limit. -1 if response header is invalid or does not contains the remaining
    *         limit information
    */
  def getRemainingLimitStatus(headers: Map[String, String]): Int = {
    APILimitUtils.getIntegerValue(headers, REMAINING_HEADER_KEY)
  }

  private def getIntegerValue(header: Map[String, String], key: String): Int = {
    val intValueStr: String = header.getOrElse(key, key.toLowerCase)
    try {
      return intValueStr.toInt
    }
    catch {
      case e: NumberFormatException => {
        print("Invalid Integer value for key: " + key + ", value :" + intValueStr)
      }
    }
    -1
  }
}
