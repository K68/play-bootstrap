package actors

import akka.actor.typed.ActorRef
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

// https://www.playframework.com/documentation/2.8.x/AkkaTyped
object MyWebSocketTypedActor {
  def create(out: ActorRef[String]): Behavior[String] = {
    Behaviors.setup { context =>
      Behaviors.receiveMessage[String] { msg =>
        context.log.info(msg)
        val timeStamp = System.currentTimeMillis()
        out ! timeStamp.toString
        Behaviors.same
      }
    }
  }
}
