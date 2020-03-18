package controllers

import com.amzport.controllers.AdapterBaseController
import javax.inject.{Inject, Singleton}
import play.api.Logging
import play.api.cache.SyncCacheApi
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import scala.concurrent.duration._
import repo.RepoDao

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AdapterController @Inject()(cc: ControllerComponents,
                                  actionBuilder: ActionRuleBuilder,
                                  repoDao: RepoDao,
                                  cache: SyncCacheApi
                                 )(implicit executionContext: ExecutionContext)
  extends AdapterBaseController(cc, actionBuilder, repoDao, cache) with Logging {
  import ErrorCode._



}
