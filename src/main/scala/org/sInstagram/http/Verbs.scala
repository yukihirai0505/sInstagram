package org.sInstagram.http

/**
	* author Yuki Hirai on 2016/11/09.
	*/
sealed abstract class Verbs(val method: String)
object Verbs {
	case object GET extends Verbs("GET")
	case object POST extends Verbs("POST")
	case object PUT extends Verbs("PUT")
	case object DELETE extends Verbs("DELETE")
}