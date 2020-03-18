package controllers

import akka.actor.ActorSystem
import akka.stream.Materializer
import com.amzport.cluster.MiracleSystem
import com.amzport.controllers.AuthBaseController
import javax.inject.{Inject, Singleton}
import play.api.cache.SyncCacheApi
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AuthController @Inject()(cc: ControllerComponents,
                               actionBuilder: ActionRuleBuilder,
                               cache: SyncCacheApi
                              )(implicit executionContext: ExecutionContext, actorSystem: ActorSystem, mat: Materializer)
  extends AuthBaseController(cc, actionBuilder, cache) {
  import ErrorCode._

  def clusterStatus(): Action[AnyContent] = Action.async { _ =>
    MiracleSystem.askMaster(MiracleSystem.ClusterStatus).map { rsp =>
      val status = rsp.asInstanceOf[MiracleSystem.ClusterStatusRsp]
      NormalRsp.SUCCESS(Json.obj("globalStatus" -> status.globalStatus,
        "selfMemberStatus" -> status.selfMemberStatus))
    }
  }

}
