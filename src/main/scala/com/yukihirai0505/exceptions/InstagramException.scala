package com.yukihirai0505.exceptions

import java.io.IOException

import com.yukihirai0505.http.APILimitUtils
import com.yukihirai0505.instsagram.InstagramResponse

/**
  * author Yuki Hirai on 2016/11/09.
  */
@SerialVersionUID(942488788539151888L)
case class InstagramException(message: String, e: Exception = null, private val headers: Option[Map[String, String]] = None, private val errorType: String = null) extends IOException(message, e) with InstagramResponse {

  def getErrorType: String = {
    errorType
  }

  override def getAPILimitStatus: Int = {
    if (headers.isDefined) {
      APILimitUtils.getAPILimitStatus(headers.get)
    } else {
      -1
    }
  }

  override def getRemainingLimitStatus: Int = {
    if (headers.isDefined) {
      APILimitUtils.getRemainingLimitStatus(headers.get)
    } else {
      -1
    }
  }

}