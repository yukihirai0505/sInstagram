package com.yukihirai0505.sInstagram.model

/**
  * author Yuki Hirai on 2016/11/11.
  */
sealed abstract class Scope(val label: String)

object Scope {

  case object BASIC extends Scope("basic")

  case object FOLLOWER_LIST extends Scope("follower_list")

  case object PUBLIC_CONTENT extends Scope("public_content")

  case object COMMENTS extends Scope("comments")

  case object RELATIONSHIPS extends Scope("relationships")

  case object LIKES extends Scope("likes")

}