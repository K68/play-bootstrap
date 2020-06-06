package controllers

import java.time.{LocalDateTime, ZoneOffset}

import com.amzport.controllers.AdapterBaseController
import javax.inject.{Inject, Singleton}
import play.api.{Logging, mvc}
import play.api.cache.SyncCacheApi
import play.api.libs.json.{JsArray, JsValue, Json}
import play.api.mvc._
import play.mvc.Http.MimeTypes
import repo.RepoDao

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AdapterController @Inject()(cc: ControllerComponents,
                                  actionBuilder: ActionRuleBuilder,
                                  cache: SyncCacheApi,
                                  // repoDao: RepoDao
                                 )(implicit executionContext: ExecutionContext)
  extends AdapterBaseController(cc, actionBuilder, cache) with Logging {
  import ErrorCode._

//  def testWebCategory(): Action[AnyContent] = Action.async { _ =>
//    repoDao.getCategoryTest.map { rsp =>
//      NormalRsp.SUCCESS(JsArray(rsp))
//    }
//  }
//
//  def testWebComBase(): Action[AnyContent] = Action.async { _ =>
//    repoDao.getWebComBaseTest.map { rsp =>
//      NormalRsp.SUCCESS(JsArray(rsp))
//    }
//  }
//
//  def isPhoneExist(phone: String): Action[AnyContent] = Action.async { _ =>
//    repoDao.isPhoneNumberExist(phone).map{ rsp =>
//      NormalRsp.SUCCESS(rsp.toString)
//    }
//  }

  def testXML(): Action[AnyContent] = Action.async { _ =>
    val XXX = "ABC"
    val url1 = s"https://www.mogorip.com/register?invite_code=$XXX"
    val url2 = s"https://www.mogorip.com/api/poster?pid=0&code=$XXX"
    Future.successful {
      Ok(
        <div>
          <input value={url1} type="text" onclick="this.select()" style="width: 100%;"/>
          <br></br>
          <img src={url2}/>
        </div>
      ).as(MimeTypes.HTML)

      val urls = List((111, 222, LocalDateTime.now()), (101, 202, LocalDateTime.now()))
      val body = urls.map{i =>
        s"""<url>
           <loc>${i._1}</loc>
           <lastmod>${i._3.atOffset(ZoneOffset.ofHours(8)).toString}</lastmod>
           <priority>${i._2}</priority>
        </url>"""
      }.mkString("\n")
      val result = s"""<?xml version="1.0" encoding="UTF-8"?>
                <urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xsi:schemaLocation="http://www.sitemaps.org/schemas/sitemap/0.9
                http://www.sitemaps.org/schemas/sitemap/0.9/sitemap.xsd">
                $body
                </urlset>"""

      mvc.Results.Ok(result).as(MimeTypes.XML)

    }
    // NotFound(<h1>参数不合法</h1>)
  }

}
