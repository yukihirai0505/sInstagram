package org.sInstagram.model

/**
	* author Yuki Hirai on 2016/11/09.
	*/
sealed abstract class Relationship(val value: String)
object Relationship {
	case object FOLLOW extends Relationship("FOLLOW")
	case object UN_FOLLOW extends Relationship("UN_FOLLOW")
	case object BLOCK extends Relationship("BLOCK")
	case object UN_BLOCK extends Relationship("UN_BLOCK")
	case object APPROVE extends Relationship("APPROVE")
	case object DENY extends Relationship("DENY")
}
