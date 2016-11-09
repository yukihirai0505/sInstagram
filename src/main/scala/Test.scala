import org.sInstagram.SInstagram
import org.sInstagram.http.Response


import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

/**
	* author Yuki Hirai on 2016/11/08.
	*/
object Test extends App {

	val sInstagram: SInstagram = new SInstagram("")
	val future = sInstagram.getUserRecentMedia()

	import scala.concurrent.Await
	import scala.concurrent.duration._
	import scala.language.postfixOps

	future onComplete {
		case Success(Response(body, code, headers)) => {
			println(body.get.data.get)
		} // do stuff
		case Failure(t) => println("An error has occured: " + t.getMessage)
	}
	Await.result(future, 10 seconds)

}
