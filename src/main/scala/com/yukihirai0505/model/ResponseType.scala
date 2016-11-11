package com.yukihirai0505.model

/**
  * author Yuki Hirai on 2016/11/11.
  */
sealed abstract class ResponseType(val label: String)
object ResponseType {
  case object TOKEN extends ResponseType("token")
  case object CODE extends ResponseType("code")
}
