package actors

import akka.actor._
import scala.concurrent.duration._

object MyWebSocketActor {
  def props(out: ActorRef) = Props(new MyWebSocketActor(out))
}

class MyWebSocketActor(out: ActorRef) extends Actor with Timers {
  import context.dispatcher

  timers.startTimerWithFixedDelay("abc", "hello", 1.seconds)

  def receive = {
    case msg: String =>
      out ! msg
  }
}
