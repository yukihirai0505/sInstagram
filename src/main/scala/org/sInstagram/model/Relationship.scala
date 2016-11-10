package org.sInstagram.model

/**
	* author Yuki Hirai on 2016/11/09.
	*/
sealed abstract class Relationship(val value: String)
object Relationship {
	case object FOLLOW extends Relationship("follow")
	case object UN_FOLLOW extends Relationship("unfollow")
	case object BLOCK extends Relationship("block")
	case object UN_BLOCK extends Relationship("unblock")
	case object APPROVE extends Relationship("approve")
	case object IGNORE extends Relationship("ignore")
}
