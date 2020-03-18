name := "play-bootstrap"

version := "0.9.1"

lazy val `play-bootstrap` = (project in file(".")).enablePlugins(PlayScala)
      
scalaVersion := "2.13.1"

libraryDependencies ++= Seq(
  guice,
  caffeine,

  // play framework & akka
  "com.typesafe.akka" %% "akka-cluster-typed" % "2.6.3",

  // postgresql
  "org.postgresql" % "postgresql" % "42.2.10",

  // quill
  "io.getquill" %% "quill-jdbc" % "3.5.0",

  // sttp
  "com.softwaremill.sttp" %% "core" % "1.7.2",
  "com.softwaremill.sttp" %% "okhttp-backend" % "1.7.2",

  // bitcoinj
  "org.bitcoinj" % "bitcoinj-core" % "0.15.6",

  // web3j
  "org.web3j" % "core" % "4.5.14",

  // qiniu
  "com.qiniu" % "qiniu-java-sdk" % "7.2.28",

  // alipay & wechat-pay
  "com.egzosn" % "pay-java-ali" % "2.13.1",
  "com.egzosn" % "pay-java-wx" % "2.13.1",

  // barcode
  "com.google.zxing" % "core" % "3.4.0",
  "com.google.zxing" % "javase" % "3.4.0"

)

