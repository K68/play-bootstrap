package controllers

import com.amzport.controllers.AdapterBaseController
import javax.inject.{Inject, Singleton}
import play.api.Logging
import play.api.cache.SyncCacheApi
import play.api.libs.json.{JsArray, JsValue, Json}
import play.api.mvc._
import repo.RepoDao

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AdapterController @Inject()(cc: ControllerComponents,
                                  actionBuilder: ActionRuleBuilder,
                                  cache: SyncCacheApi,
                                  repoDao: RepoDao
                                 )(implicit executionContext: ExecutionContext)
  extends AdapterBaseController(cc, actionBuilder, cache) with Logging {
  import ErrorCode._

  def testWebCategory(): Action[AnyContent] = Action.async { _ =>
    repoDao.getCategoryTest.map { rsp =>
      NormalRsp.SUCCESS(JsArray(rsp))
    }
  }

  def testWebComBase(): Action[AnyContent] = Action.async { _ =>
    repoDao.getWebComBaseTest.map { rsp =>
      NormalRsp.SUCCESS(JsArray(rsp))
    }
  }

}
