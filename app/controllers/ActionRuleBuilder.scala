package controllers

import akka.actor.ActorSystem
import com.amzport.chat.{ChatDao, ChatMailActor}
import javax.inject.{Inject, Singleton}
import play.api.cache.SyncCacheApi
import play.api.Configuration
import play.api.mvc._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import repo.RepoDao
import com.amzport.controllers.{ActionBuilder, AuthConstBase, ErrorCodeBase, NormalRspBase}
import com.amzport.h5.H5ResourceDao
import com.amzport.markdown.MarkdownDao
import com.amzport.media.MediaDao
import com.amzport.oss.QiniuHelper
import com.amzport.pay.{AbTradeBackDao, AliPayHelper, ApplePayHelper, WxPayHelper}
import com.amzport.sttp.UtilHelper
import com.amzport.trace.TraceDao
import play.api.libs.json.Json
import play.api.mvc.Results.{Forbidden, Ok}

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

@Singleton
class ActionRuleBuilder @Inject()(actorSystem: ActorSystem,
                                  parser: BodyParsers.Default,
                                  configuration: Configuration,
                                  repoDao: RepoDao,
                                  cache: SyncCacheApi
                          )(implicit ec: ExecutionContext)
  extends ActionBuilder(parser, configuration, repoDao, cache) {

  override def daoAbTradeBack: Option[AbTradeBackDao] = None
  override def daoMarkdown: Option[MarkdownDao] = None
  override def daoChat: Option[ChatDao] = None
  override def daoTrace: Option[TraceDao] = None
  override def daoH5Resource: Option[H5ResourceDao] = None
  override def daoMedia: Option[MediaDao] = None

  override val objAuthConst: AuthConstBase = AuthConst
  override val objNormalRsp: NormalRspBase = NormalRsp
  override val objErrorCode: ErrorCodeBase = ErrorCode
  override val objUtilSTTP: UtilHelper = UtilSTTP
  override val objUtilQiniu: QiniuHelper = UtilQiniu
  override val objUtilAliPay: AliPayHelper = UtilAliPay
  override val objUtilWxPay: WxPayHelper = UtilWxPay
  override val objUtilApplePay: ApplePayHelper = UtilApplePay

  super.init()

  // Start Chat Engine
  // ChatMailActor.startChatMailBoxes(actorSystem, repoDao)

  // More

  /*
    lifecycle.addStopHook { () =>
    Future.successful{
      RepoDao.ctx.dataSource.close()
      actorSystem.terminate()
    }
  }
   */

}
