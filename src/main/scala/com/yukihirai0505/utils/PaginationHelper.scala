package com.yukihirai0505.utils

import java.net.{MalformedURLException, URLDecoder}

import com.netaporter.uri.Uri
import com.yukihirai0505.exceptions.OAuthException
import com.yukihirai0505.responses.common.Pagination

/**
  * Created by yukihirai on 2016/11/09.
  */
object PaginationHelper {
  def parseNextUrl(pagination: Pagination, apiUrl: String): PaginationHelper.Page = {
    if (pagination.nextUrl.isDefined) {
      try {
        val nextUrl: String = decodeUrl(pagination.nextUrl.getOrElse(""))
        val params: Map[String, Option[String]] = Uri.parse(nextUrl).query.params.toMap
        val apiPath: String = substringBetween(nextUrl, apiUrl, "?")
        return new PaginationHelper.Page(apiPath, params)
      } catch {
        case mue: MalformedURLException => throw new OAuthException("Malformed URL", mue)
      }
    }
    throw new OAuthException("No nextUrl")
  }

  class Page(val apiPath: String, val queryStringParams: Map[String, Option[String]])

  private def decodeUrl(s: String): String = {
    URLDecoder.decode(s, "UTF-8")
  }

  def substringBetween(str: String, open: String, close: String): String = {
    val start: Int = str.indexOf(open)
    if (start != -1) {
      val end: Int = str.indexOf(close, start + open.length)
      if (end != -1) return str.substring(start + open.length, end)
    }
    ""
  }
}
