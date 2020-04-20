package repo

import java.time.LocalDateTime

import com.amzport.repo.model.{WebComBase, WebComCategory}
import play.api.libs.json.{Json, OFormat}

object Model {

  implicit val WebComBaseFormat: OFormat[WebComBase] = Json.format[WebComBase]
  implicit val WebComCategoryFormat: OFormat[WebComCategory] = Json.format[WebComCategory]
}
