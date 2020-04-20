package repo

import akka.actor.{ActorRef, ActorSystem}
import io.getquill.{PostgresJdbcContext, SnakeCase}
import javax.inject.{Inject, Singleton}
import play.api.cache.{AsyncCacheApi, SyncCacheApi}

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import Model._
import actors.Worker
import akka.util.Timeout
import play.api.{Configuration, Logging, mvc}
import com.amzport.repo.{DaoBase, WebsiteTrait}
import com.amzport.repo.model._
import play.api.libs.json.{JsValue, Json}

import scala.util.Try

object RepoDao {
  lazy val ctx = new PostgresJdbcContext(SnakeCase, configPrefix = "ctx")
}

@Singleton
class RepoDao @Inject()(actorSystem: ActorSystem,
                        cacheAsync: AsyncCacheApi,
                        cacheSync: SyncCacheApi,
                        configuration: Configuration
                       )
  extends DaoBase(configuration, RepoDao.ctx, cacheAsync, cacheSync) with WebsiteTrait with Logging {

  override val ctxWebsiteTrait: PostgresJdbcContext[SnakeCase.type] = this.ctx

  import this.ctx._
  implicit val ecBlocking: ExecutionContext = actorSystem.dispatchers.lookup(id = "blockingPool")
  private val webServiceGap = configuration.underlying.getInt("web.service.gap")

  import akka.pattern.ask
  implicit val timeout: Timeout = 30.seconds

  val workerRef: ActorRef = actorSystem.actorOf(Worker.props(this), "Actor_Worker")

  actorSystem.scheduler.scheduleWithFixedDelay(30.seconds, webServiceGap.seconds) { () =>
    // 抽取系统设置表信息
    run(
      query[SystemSetting]
    ).foreach { setting =>
      val key = s"SYSTEM:SETTING:${setting.settingKey}"
      cacheSync.set(key, setting, 30.seconds)
    }
  }

  override def dealNewAccountParent(accountId: Long): Unit = {
    //
  }

  override def dealNewAccountSelf(accountId: Long): Unit = {
    //
  }

  def getCategoryTest: Future[List[JsValue]] = {
    Future {
      allWebCategory.map(i => Json.toJson(i))
    }
  }

  def getWebComBaseTest: Future[List[JsValue]] = {
    Future {
      run(query[WebComBase]).map(i => Json.toJson(i))
    }
  }

}
