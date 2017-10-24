package com.yukihirai0505.sInstagram

import com.netaporter.uri.Uri.parse
import com.yukihirai0505.com.scala.Request
import com.yukihirai0505.com.scala.constants.Verbs
import com.yukihirai0505.com.scala.model.Response
import com.yukihirai0505.sInstagram.model.{Constants, OAuthConstants, QueryParam}
import com.yukihirai0505.sInstagram.responses.Pagination
import com.yukihirai0505.sInstagram.responses.auth.{AccessToken, Auth, SignedAccessToken}
import com.yukihirai0505.sInstagram.utils.PaginationHelper
import dispatch.{Req, url}
import play.api.libs.json.Reads

import scala.concurrent.Future

trait InstagramTrait {


  protected val auth: Auth

  /**
    * Request instagram api method
    *
    * @param verb
    * @param apiPath
    * @param params
    * @param r
    * @tparam T
    * @return
    */
  def request[T](verb: Verbs, apiPath: String, params: Option[Map[String, Option[String]]] = None)(implicit r: Reads[T]): Future[Response[T]] = {
    val parameters: Map[String, String] = params match {
      case Some(m) => m.filter(_._2.isDefined).mapValues(_.getOrElse("")).filter(!_._2.isEmpty)
      case None => Map.empty
    }
    val accessTokenUrl = s"${Constants.API_URL}$apiPath?${authToGETParams(auth)}"
    val effectiveUrl: String = verb match {
      case Verbs.GET => addSecureSigIfNeeded(accessTokenUrl)
      case _ => addSecureSigIfNeeded(accessTokenUrl, Some(parameters))
    }
    val request: Req = url(effectiveUrl).setMethod(verb.label)
    val requestWithParams = if (verb.label == Verbs.GET.label) {
      request <<? parameters
    } else {
      request << parameters
    }
    //println(requestWithParams.url)
    Request.sendRequestJson[T](requestWithParams)
  }

  /**
    * Transform an Authentication type to be used in a URL.
    *
    * @param a Authentication
    * @return String
    */
  protected def authToGETParams(a: Auth): String = a match {
    case AccessToken(token) => s"${OAuthConstants.ACCESS_TOKEN}=$token"
    case SignedAccessToken(token, _) => s"${OAuthConstants.ACCESS_TOKEN}=$token"
  }

  /**
    * Add sign for secure request
    *
    * @param url
    * @param postData
    * @return
    */
  protected def addSecureSigIfNeeded(url: String, postData: Option[Map[String, String]] = None)
  : String = auth match {
    case SignedAccessToken(_, secret) =>
      val uri = parse(url)
      val params = uri.query.params
      val auth: InstagramAuth = new InstagramAuth
      val sig = auth.createSignedParam(
        secret,
        uri.pathRaw.replace(Constants.VERSION, ""),
        concatMapOpt(postData, params.toMap)
      )
      uri.addParam(QueryParam.SIGNATURE, sig).toStringRaw
    case _ => url
  }

  /**
    * concat map option
    *
    * @param postData
    * @param params
    * @return
    */
  protected def concatMapOpt(postData: Option[Map[String, String]], params: Map[String, Option[String]])
  : Map[String, Option[String]] = postData match {
    case Some(m) => params ++ m.mapValues(Some(_))
    case _ => params
  }

  /**
    * Get the next page of recent media objects from a previously executed
    * request
    *
    * @param pagination
    */
  def getNextPage[T](pagination: Pagination)
                    (implicit r: Reads[T]): Future[Response[T]] = {
    val page: PaginationHelper.Page = PaginationHelper.parseNextUrl(pagination, Constants.API_URL)
    request[T](Verbs.GET, page.apiPath, Some(page.queryStringParams))
  }
}
