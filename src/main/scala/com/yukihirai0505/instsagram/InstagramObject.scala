package com.yukihirai0505.instsagram

import com.yukihirai0505.http.APILimitUtils

/**
  * author Yuki Hirai on 2016/11/09.
  */
class InstagramObject(headers: Option[Map[String, String]] = None) extends InstagramResponse {

  /**
    * Get the available API limit. It correspond to the value of
    * X-Ratelimit-Limit key in HTTP response headers. For Instagram
    * v1 API, this method should return 5000.
    *
    * @return Available API limit
    */
  def getAPILimitStatus: Int = {
    APILimitUtils.getAPILimitStatus (headers.get)
  }

  /**
    * Get the remaining API limit. It correspond to the value of
    * X-Ratelimit-Remaining key in HTTP response headers.
    *
    * @return Remaining API limit
    */
  def getRemainingLimitStatus: Int = {
    APILimitUtils.getRemainingLimitStatus (headers.get)
  }
}
