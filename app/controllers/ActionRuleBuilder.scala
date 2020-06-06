package controllers

import akka.actor.ActorSystem
import com.amzport.chat.{ChatDao, ChatMailActor}
import com.amzport.cluster.MiracleSystem
import com.amzport.cluster
import javax.inject.{Inject, Singleton}
import play.api.cache.SyncCacheApi
import play.api.Configuration
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._
import repo.RepoDao
import com.amzport.controllers.{ActionBuilder, AuthConstBase, ErrorCodeBase, NormalRspBase}
import com.amzport.h5.H5ResourceDao
import com.amzport.markdown.MarkdownDao
import com.amzport.media.MediaDao
import com.amzport.oss.QiniuHelper
import com.amzport.pay.{AbTradeBackDao, AliPayHelper, ApplePayHelper, WxPayHelper}
import com.amzport.repo.{DaoImplSimple, DaoTrait}
import com.amzport.sttp.UtilHelper
import com.amzport.trace.TraceDao
import play.api.inject.ApplicationLifecycle
import play.api.libs.json.Json
import play.api.mvc.Results.{Forbidden, Ok}
import repo.ClusterModel

object AuthConst extends AuthConstBase {
  // More
}

object NormalRsp extends NormalRspBase {
  // More
}

object ErrorCode extends ErrorCodeBase {
  // More
}

object UtilSTTP extends UtilHelper {
  import com.softwaremill.sttp._

  def fetchRaw(url: String): Option[String] = {
    sttp.get(uri"$url").readTimeout(10.seconds).send().body.toOption
  }
}

object UtilQiniu extends QiniuHelper {}

object UtilAliPay extends AliPayHelper {}

object UtilWxPay extends WxPayHelper {}

object UtilApplePay extends ApplePayHelper {}

object UtilDaoImpl extends DaoImplSimple { // SlaveClusterDao

}

@Singleton
class ActionRuleBuilder @Inject()(actorSystem: ActorSystem,
                                  appLifecycle: ApplicationLifecycle,
                                  parser: BodyParsers.Default,
                                  configuration: Configuration,
                                  // repoDao: RepoDao,
                                  cache: SyncCacheApi
                          )(implicit ec: ExecutionContext)
  extends ActionBuilder(parser, configuration, cache) {

  override def daoAbTradeBack: Option[AbTradeBackDao] = None
  override def daoMarkdown: Option[MarkdownDao] = None
  override def daoChat: Option[ChatDao] = None
  override def daoTrace: Option[TraceDao] = None
  override def daoH5Resource: Option[H5ResourceDao] = None
  override def daoMedia: Option[MediaDao] = None

  override def repoDao: DaoTrait = UtilDaoImpl        // RepoDao 或其他 DaoImpl
  override val objAuthConst: AuthConstBase = AuthConst
  override val objNormalRsp: NormalRspBase = NormalRsp
  override val objErrorCode: ErrorCodeBase = ErrorCode
  override val objUtilSTTP: UtilHelper = UtilSTTP
  override val objUtilQiniu: QiniuHelper = UtilQiniu
  override val objUtilAliPay: AliPayHelper = UtilAliPay
  override val objUtilWxPay: WxPayHelper = UtilWxPay
  override val objUtilApplePay: ApplePayHelper = UtilApplePay

  super.init(initRepoDao = false)

  // Start Chat Engine
  // ChatMailActor.startChatMailBoxes(actorSystem, repoDao)

  // More
  MiracleSystem.setup(actorSystem, appLifecycle, repoDao,
    shutdownSysHook = { () =>
      // RepoDao.ctx.dataSource.close()
      actorSystem.terminate()
      Future.successful(())
    },
    globalCommandHook = { command: cluster.GlobalCommand =>
      command match {
        case ClusterModel.HelloWorld(name, b) =>
          Future.successful(ClusterModel.HelloWorldRsp(s"Hello World: $name ${b.mkString}"))

        case _ =>
          Future.successful(cluster.Invalid())
      }
    }
  )

}
