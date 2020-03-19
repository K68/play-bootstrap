package repo

import com.amzport.cluster.GlobalCommand

object ClusterModel {

  case class HelloWorld(name: String) extends GlobalCommand
  case class HelloWorldRsp(result: String) extends GlobalCommand

}
