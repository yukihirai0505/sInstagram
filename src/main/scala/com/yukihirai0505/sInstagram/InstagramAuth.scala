package com.yukihirai0505.sInstagram

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

import play.api.libs.json.Json
import com.ning.http.client.FluentCaseInsensitiveStringsMap
import com.yukihirai0505.sInstagram.model.{Constants, OAuthConstants, ResponseType, Scope}
import com.yukihirai0505.sInstagram.responses.Meta
import com.yukihirai0505.sInstagram.responses.auth.{AccessToken, Auth, Oauth, SignedAccessToken}
import com.yukihirai0505.sInstagram.utils.Configurations.{callbackUrl, clientId, clientSecret}
import dispatch.Defaults._
import dispatch._

import scala.collection.JavaConverters._

class InstagramAuth {

  /**
    * Tell if authentication type is secure.
    *
    * @param a Authentication
    * @return Boolean
    */
  def isSecure(a: Auth): Boolean = a match {
    case SignedAccessToken(_, _) => true
    case _ => false
  }

  /**
    * Scope string which will be append to the URL.
    *
    * @param scopes Scopes
    * @return String
    */
  def setScopes(scopes: Seq[Scope]): String = {
    val scopeLabels: List[String] = scopes.map(s => s.label).toList
    if (scopes.nonEmpty) scopeLabels.mkString("scope=", "+", "")
    else ""
  }

  /**
    * Create the URL to call when retrieving an access token.
    *
    * @param clientId     Client identifier. (You need to register this on instagram.com/developer)
    * @param callbackUrl  URI which the response is sent to. (You need to register this on instagram.com/developer)
    * @param responseType Response type code or token
    * @param scopes       Require scope.
    */
  def authURL(clientId: String = clientId, callbackUrl: String = callbackUrl, responseType: ResponseType = ResponseType.CODE, scopes: Seq[Scope] = Seq()): String = {
    (Constants.AUTHORIZE_URL format(clientId, callbackUrl, responseType.label)) + s"&${setScopes(scopes)}"
  }

  /**
    * Converts an array of `bytes` to a hexadecimal representation String.
    * "Stolen" from the sbt source code.
    * Credits: http://www.scala-sbt.org/0.13.7/sxr/sbt/Hash.scala.html
    *
    * @param bytes Array of bytes
    * @return String
    */
  private def toHex(bytes: Array[Byte]): String = {
    val buffer = new StringBuilder(bytes.length * 2)
    for (i <- bytes.indices) {
      val b = bytes(i)
      val bi: Int = if (b < 0) b + 256 else b
      buffer append toHex((bi >>> 4).asInstanceOf[Byte])
      buffer append toHex((bi & 0x0F).asInstanceOf[Byte])
    }
    buffer.toString()
  }

  private def toHex(b: Byte): Char = {
    require(b >= 0 && b <= 15, "Byte " + b + " was not between 0 and 15")
    if (b < 10) ('0'.asInstanceOf[Int] + b).asInstanceOf[Char]
    else ('a'.asInstanceOf[Int] + (b - 10)).asInstanceOf[Char]
  }

  /**
    * Signed header for Instagram. More informations on this page
    * http://instagram.com/developer/restrict-api-requests/
    *
    * @param clientSecret Your Instagram client secret token.
    * @param endpoint     API endpoint
    * @param params       Map of API parameters
    * @return String       Encoded header.
    */
  def createSignedParam(clientSecret: String, endpoint: String, params: Map[String, Option[String]]): String = {
    val paramsString = params.keys.toList.sorted.map(key => s"|$key=${params(key).mkString}").mkString
    val sig = s"$endpoint$paramsString"
    val secret = new SecretKeySpec(clientSecret.getBytes, "HmacSHA256")
    val mac = Mac.getInstance("HmacSHA256")
    mac.init(secret)
    val result = mac.doFinal(sig.getBytes)
    toHex(result)
  }

  /**
    * Post request to exchange a authentication code against an access token.
    * Note that an authentication code is valid one time only.
    *
    * @param clientId     Client identifier. (You need to register this on instagram.com/developer)
    * @param clientSecret Client secret. (You need to register this on instagram.com/developer)
    * @param callbackUrl  URI which the response is sent to. (You need to register this on instagram.com/developer)
    * @param code         Authentication code. You can retrieve it via codeURL.
    * @return Future of Response[Authentication]
    */
  def requestToken(clientId: String = clientId, clientSecret: String = clientSecret, callbackUrl: String = callbackUrl, code: String): Future[Option[Auth]] = {
    val params = Map(
      OAuthConstants.CLIENT_ID -> clientId,
      OAuthConstants.CLIENT_SECRET -> clientSecret,
      OAuthConstants.REDIRECT_URI -> callbackUrl,
      OAuthConstants.CODE -> code,
      OAuthConstants.GRANT_TYPE -> OAuthConstants.AUTHORIZATION_CODE
    )
    val request = url(Constants.ACCESS_TOKEN_ENDPOINT) << params
    Http(request).map { resp =>
      val response = resp.getResponseBody
      //val headers = ningHeadersToMap(resp.getHeaders)
      if (resp.getStatusCode != 200) throw new Exception(parseMeta(response).toString)
      Json.parse(response).asOpt[Oauth] match {
        case Some(o: Oauth) => Some(AccessToken(o.accessToken))
        case _ =>
          val errorMeta = Meta(Some("OauthException"), 500, Some("Unknown error"))
          val meta = Json.parse(response).asOpt[Meta].getOrElse(errorMeta)
          None
      }
    }
  }

  private def parseMeta(response: String): Meta = {
    val errorMeta = Meta(Some("OauthException"), 500, Some("Unknown error"))
    Json.parse(response).validate[Meta].getOrElse(errorMeta)
  }

  private def ningHeadersToMap(headers: FluentCaseInsensitiveStringsMap) = {
    mapAsScalaMapConverter(headers).asScala.map(e => e._1 -> e._2.asScala).toMap
  }

}
