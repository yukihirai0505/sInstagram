package org.sInstagram.http

/**
	* author Yuki Hirai on 2016/11/09.
	*/
case class Response[T](body: Option[T], code: Int, headers: Map[String, Seq[String]])