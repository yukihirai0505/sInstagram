package com.yukihirai0505.sInstagram.http

import com.ning.http.client.FluentCaseInsensitiveStringsMap
import dispatch.Defaults._
import dispatch._
import com.yukihirai0505.sInstagram.responses.common.Meta
import play.api.libs.json._

import scala.collection.JavaConverters._

object Request {

  /**
   * Send the prepared request to an URL and parse the response to an appropriate case class.
   *
   * @param request Req, the dispatch request ready to by sent
   * @tparam T      represent the type of the Instagram data requested
   * @return        a Future of Response[T]
   */
  def send[T](request: Req)(implicit r: Reads[T]): Future[Option[T]] = {
    Http(request).map { resp =>
      val response = resp.getResponseBody
      // val headers = ningHeadersToMap(resp.getHeaders)
      if (resp.getStatusCode != 200) throw new Exception(parseMeta(response).toString)
      // println(Json.prettyPrint(Json.parse(response)))
      val body = Json.parse(response).validate[T] match {
        case JsError(e) => throw new Exception(e.toString())
        case JsSuccess(value, _) => value match {
          case None => None
          case _ => Some(value)
        }
      }
      body
    }
  }

  /***
    * Parse meta information
    * @param response
    * @return
    */
  private def parseMeta(response: String): Meta = {
    // This looks weird right? Well Instagram JSON errors are not always formatted the same way...
    val errorMeta = Meta(Some("UnknownException"), 500, Some("Unknown error"))
    (Json.parse(response) \ "meta").validate[Meta].getOrElse(
      Json.parse(response).validate[Meta].getOrElse(errorMeta)
    )
  }

  /***
    * ning header to scala map
    * @param headers
    * @return
    */
  private def ningHeadersToMap(headers: FluentCaseInsensitiveStringsMap) = {
    mapAsScalaMapConverter(headers).asScala.map(e => e._1 -> e._2.asScala).toMap
  }

}