package controllers

import akka.actor.ActorSystem
import akka.stream.Materializer
import com.amzport.controllers.AuthBaseController
import javax.inject.{Inject, Singleton}
import play.api.cache.SyncCacheApi
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import repo.RepoDao

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AuthController @Inject()(cc: ControllerComponents,
                               actionBuilder: ActionRuleBuilder,
                               repoDao: RepoDao,
                               cache: SyncCacheApi
                              )(implicit executionContext: ExecutionContext, actorSystem: ActorSystem, mat: Materializer)
  extends AuthBaseController(cc, actionBuilder, repoDao, cache) {
  import ErrorCode._



}
