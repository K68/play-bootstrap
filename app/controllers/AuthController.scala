package controllers

import java.time.LocalDateTime

import actors.{MyWebSocketActor, MyWebSocketTypedActor}
import akka.actor.ActorSystem
import akka.actor.typed.Props
import akka.stream.Materializer
import com.amzport.cluster
import com.amzport.cluster.MiracleSystem
import com.amzport.controllers.AuthBaseController
import javax.inject.{Inject, Singleton}
import play.api.cache.SyncCacheApi
import play.api.libs.json.{JsValue, Json}
import play.api.libs.streams.ActorFlow
import play.api.mvc._
import repo.ClusterModel
import akka.actor.typed.javadsl.Adapter

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AuthController @Inject()(cc: ControllerComponents,
                               actionBuilder: ActionRuleBuilder,
                               cache: SyncCacheApi
                              )(implicit executionContext: ExecutionContext, actorSystem: ActorSystem, mat: Materializer)
  extends AuthBaseController(cc, actionBuilder, cache) {
  import ErrorCode._

  def clusterStatus(): Action[AnyContent] = Action.async { _ =>
    MiracleSystem.askMaster(cluster.ClusterStatus()).map { rsp =>
      val status = rsp.asInstanceOf[cluster.ClusterStatusRsp]
      NormalRsp.SUCCESS(Json.obj("globalStatus" -> status.globalStatus,
        "selfMemberStatus" -> status.selfMemberStatus))
    }
  }

  def helloWorld(nm: String): Action[AnyContent] = Action.async { _ =>
    MiracleSystem.askMaster(ClusterModel.HelloWorld(nm)).map { rsp =>
      val result = rsp.asInstanceOf[ClusterModel.HelloWorldRsp]
      NormalRsp.SUCCESS(result.result)
    }
  }

  def testGenAuth(): Action[AnyContent] = Action.async { _ =>
    MiracleSystem.askMaster(cluster.GenAccountAuthMeta(0, LocalDateTime.now(), List.empty)).map { rsp =>
      val authMeta = rsp.asInstanceOf[cluster.GenAccountAuthMetaRsp].authMeta
      NormalRsp.SUCCESS(Json.obj(
        "accountId" -> authMeta.accountId,
        "groupIds" -> authMeta.groupIds,
        "groupTrees" -> authMeta.groupTrees,
        "roles" -> authMeta.roles,
        "ts" -> authMeta.ts
      ))
    }
  }

  // 参考 https://www.playframework.com/documentation/2.8.x/ScalaWebSockets
  def socket1 = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef { out =>
      MyWebSocketActor.props(out)
    }
  }

//  def socket2 = WebSocket.accept[String, String] { request =>
//    ActorFlow.actorRef { out =>
//      MyWebSocketTypedActor
//        .create(Adapter.toTyped[String](out))
//    }
//  }

}
