package actors

import akka.actor.{Actor, Props, Timers}
import repo.RepoDao

object Worker {
  def props(repo: RepoDao): Props = Props(new Worker(repo))

  sealed trait ActionCode
  case object HelloWorld extends ActionCode

}

class Worker(repo: RepoDao) extends Actor with Timers {
  import Worker._
  // import context.dispatcher

  override def receive: Receive = {
    case HelloWorld =>
      // more todo

  }

}

