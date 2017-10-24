package com.yukihirai0505.sInstagram.responses

import com.github.tototoshi.play.json.JsonNaming
import play.api.libs.functional.syntax.{unlift, _}
import play.api.libs.json.{Format, Json, __}


case class Pagination(
                       deprecationWarning: Option[String],
                       minTagId: Option[String],
                       nextMaxId: Option[String],
                       nextMaxTagId: Option[String],
                       nextUrl: Option[String],
                       nextCursor: Option[String]
                     )

object Pagination {
  implicit val PaginationFormat = JsonNaming.snakecase(Json.format[Pagination])
}

case class Meta(
                 errorType: Option[String],
                 code: Int,
                 errorMessage: Option[String]
               )

object Meta {
  implicit val MetaFormat = JsonNaming.snakecase(Json.format[Meta])
}

case class Data[T](
                    data: T,
                    meta: Meta
                  )

object Data {
  implicit def dataFormat[T: Format]: Format[Data[T]] =
    ((__ \ "data").format[T] ~
      (__ \ "meta").format[Meta]) (Data.apply, unlift(Data.unapply))
}


case class DataWithPage[T](
                            data: Seq[T],
                            pagination: Pagination,
                            meta: Meta
                          )

object DataWithPage {
  implicit def dataWithPageFormat[T: Format]: Format[DataWithPage[T]] =
    ((__ \ "data").format[Seq[T]] ~
      (__ \ "pagination").format[Pagination] ~
      (__ \ "meta").format[Meta]) (DataWithPage.apply, unlift(DataWithPage.unapply))
}


case class NoDataResponse(
                           meta: Option[Meta]
                         )


object NoDataResponse {
  implicit val NodataResponseFormat = Json.format[NoDataResponse]
}

case class Position(
                     x: Double,
                     y: Double
                   )

object Position {
  implicit val PositionFormat = JsonNaming.snakecase(Json.format[Position])
}

case class User(
                 fullName: String,
                 id: String,
                 profilePicture: String,
                 username: String
               )

object User {
  implicit val UserFormat = JsonNaming.snakecase(Json.format[User])
}


case class UsersInPhoto(
                         user: User,
                         position: Position
                       )


object UsersInPhoto {
  implicit val UsersInPhotoFormat = JsonNaming.snakecase(Json.format[UsersInPhoto])
}

case class CarouselMedia(
                          images: Option[Images],
                          videos: Option[Videos],
                          usersInPhoto: Seq[UsersInPhoto],
                          `type`: String
                        )


object CarouselMedia {
  implicit val CarouselMediaFormat = JsonNaming.snakecase(Json.format[CarouselMedia])
}


case class FromTagData(
                        fullName: String,
                        id: String,
                        profilePicture: String,
                        username: String
                      )

object FromTagData {
  implicit val FromTagDataFormat = JsonNaming.snakecase(Json.format[FromTagData])
}

case class Caption(
                    createdTime: String,
                    from: FromTagData,
                    id: String,
                    text: String
                  )


object Caption {
  implicit val CaptionFormat = JsonNaming.snakecase(Json.format[Caption])
}

case class Comment(
                    createdTime: String,
                    text: String,
                    from: User,
                    id: String
                  )


object Comment {
  implicit val CommentFormat = JsonNaming.snakecase(Json.reads[Comment])
}


case class Comments(count: Int)

object Comments {
  implicit val CommentsFormat = Json.format[Comments]
}


case class Image(url: String, width: Int, height: Int)

object Image {
  implicit val ImageFormat = Json.format[Image]
}

case class Images(
                   lowResolution: Image,
                   thumbnail: Image,
                   standardResolution: Image
                 )

object Images {
  implicit val ImagesFormat = JsonNaming.snakecase(Json.format[Images])
}

case class Likes(count: Int)

object Likes {
  implicit val LikesFormat = Json.format[Likes]
}

case class Location(
                     id: Option[String],
                     latitude: Option[Double],
                     longitude: Option[Double],
                     name: Option[String]
                   )

object Location {
  implicit val LocationFormat = Json.format[Location]
}


case class MediaLocation(
                          id: Option[Long],
                          latitude: Option[Double],
                          longitude: Option[Double],
                          name: Option[String]
                        )

object MediaLocation {
  implicit val mediaLocationFormat = Json.format[MediaLocation]
}


case class Video(url: String, width: Int, height: Int)


object Video {
  implicit val VideoFormat = Json.format[Video]
}

case class Videos(
                   lowBandwith: Option[Video],
                   lowResolution: Video,
                   standardResolution: Video
                 )

object Videos {
  implicit val VideosFormat = JsonNaming.snakecase(Json.format[Videos])
}