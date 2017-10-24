package com.yukihirai0505.sInstagram.utils

import java.net.{MalformedURLException, URLDecoder}

import com.netaporter.uri.Uri
import com.yukihirai0505.com.scala.model.Response
import com.yukihirai0505.sInstagram.exceptions.OAuthException
import com.yukihirai0505.sInstagram.responses.{DataWithPage, Pagination}

import scala.concurrent.{ExecutionContextExecutor, Future}

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

  def getAllPages[T](get: (Option[String]) => Future[Response[DataWithPage[T]]], page: (Pagination) => Future[Response[DataWithPage[T]]])
                    (implicit executionContextExecutor: ExecutionContextExecutor): Future[Seq[T]] = {
    def getPages(seq: Seq[T], pagination: Pagination): Future[Seq[T]] = {
      pagination.nextUrl match {
        case Some(_) =>
          page(pagination).flatMap {
            case Response(data, _) =>
              val d = data.get
              getPages(d.data ++ seq, d.pagination)
            case _ => Future successful seq
          }
        case None => Future successful seq
      }
    }

    get(None).flatMap { response =>
      response.data match {
        case Some(data) =>
          getPages(data.data, data.pagination)
        case None => Future successful Seq.empty
      }
    }
  }

}
