package org.sInstagram.utils

import java.net.{MalformedURLException, URL, URLDecoder}

import org.sInstagram.exceptions.OAuthException
import org.sInstagram.responses.common.Pagination

/**
  * Created by yukihirai on 2016/11/09.
  */
object PaginationHelper {
  def parseNextUrl(pagination: Pagination, apiUrl: String): PaginationHelper.Page = {
    if (pagination.nextUrl.isDefined) {
      try {
        val nextUrl: String = decodeUrl(pagination.nextUrl.getOrElse(""))
        val params: Map[String, String] = toQueryMap(nextUrl)
        val apiPath: String = substringBetween(nextUrl, apiUrl, "?")
        return new PaginationHelper.Page(apiPath, params)
      } catch {
        case mue: MalformedURLException => throw new OAuthException("Malformed URL", mue)
      }
    }
    throw new OAuthException("No nextUrl")
  }

  class Page(val apiPath: String, val queryStringParams: Map[String, String])

  def toQueryMap(url: String): Map[String, String] = {
    val query = try {
      Option(new URL(url).getQuery).getOrElse("")
    } catch {
      case e: Throwable => ""
    }
    queryStringMap(query)
  }

  private def queryStringMap(query: String) = {
    val parts = query.split("&")
    parts.filter(_.nonEmpty).map {
      p =>
        val q = p.split("=")
        q.length match {
          case 2 => decodeUrl(q(0)) -> decodeUrl(q(1))
          case 1 => decodeUrl(q(0)) -> ""
        }
    }.toMap
  }
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
