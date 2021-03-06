name := "actors"

version := "1.0-SNAPSHOT"

scalaVersion := "2.10.3"

resolvers ++= Seq(
  "OSS Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases",
  "OSS Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
  "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases",
  "Typesafe Snapshots" at "http://repo.typesafe.com/typesafe/snapshots"
)

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3-M1" % "test",
  "net.liftweb" %% "lift-actor" % "2.6-M1" % "test",
  "org.scala-lang" % "scala-actors" % "2.10.2" % "test",
  "org.scalaz" %% "scalaz-concurrent" % "7.1.0-M3" % "test",
  "com.higherfrequencytrading" % "affinity" % "1.7" % "test",
  "org.specs2" %% "specs2" % "2.2.2-scalaz-7.1.0-M3" % "test",
  "junit" % "junit-dep" % "4.11" % "test"
)

scalacOptions ++= Seq("-target:jvm-1.7", "-deprecation", "-unchecked")

parallelExecution in test := false

testGrouping <<= definedTests in Test map { tests =>
  tests.map { test =>
    import Tests._
    import scala.collection.JavaConversions._
    new Group(
      name = test.name,
      tests = Seq(test),
      runPolicy = SubProcess(javaOptions = Seq("-server", "-Xms8g", "-Xmx8g", "-Xss192k", "-XX:NewSize=7g", "-XX:PermSize=64m", "-XX:MaxPermSize=64m",
        "-XX:+TieredCompilation", "-XX:+UseG1GC", "-XX:+UseNUMA", "-XX:+UseCondCardMark", "-XX:-UseBiasedLocking", "-XX:+AlwaysPreTouch") ++
        System.getProperties.propertyNames.toSeq.map(key => "-D" + key.toString + "=" + System.getProperty(key.toString))))
  }.sortWith(_.name < _.name)
}
