package controllers

import java.time.LocalDateTime

import akka.actor.ActorSystem
import akka.stream.Materializer
import com.amzport.cluster
import com.amzport.cluster.MiracleSystem
import com.amzport.controllers.AuthBaseController
import javax.inject.{Inject, Singleton}
import play.api.cache.SyncCacheApi
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import repo.ClusterModel

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

}
