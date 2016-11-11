package com.yukihirai0505.instsagram

/**
  * Interface to represent information in Instagram response header
  * author Yuki Hirai on 2016/11/08.
  */
trait InstagramResponse {

  /**
    * Get the available API limit. It correspond to the value of
    * X-Ratelimit-Limit key in HTTP response headers. For Instagram
    * v1 API, this method should return 5000.
    *
    * @return Available API limit. -1 if headers invalid.
    */
  def getAPILimitStatus: Int

  /**
    * Get the remaining API limit. It correspond to the value of
    * X-Ratelimit-Remaining key in HTTP response headers.
    *
    * @return Remaining API limit. -1 if headers invalid.
    */
  def getRemainingLimitStatus: Int

}
