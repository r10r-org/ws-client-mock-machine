name := "ws-client-mock-machine"

scalaVersion := "2.12.4"

crossScalaVersions := Seq("2.11.11", "2.12.4")

scalacOptions ++= Seq("-deprecation", "-feature")

organization := "org.r10r"

val playVersion = "2.6.9"

fork := true

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play"                             % playVersion % "provided",
  "com.typesafe.play" %% "play-ahc-ws"                      % playVersion % "provided",
  "com.typesafe.play" %% "play-test"                        % playVersion % "provided",
  "org.mockito" %  "mockito-core" % "2.13.0"                              % "provided"
)

libraryDependencies ++= Seq(
  "org.scalatest"  %% "scalatest"    % "3.0.4",
  "org.scalacheck" %% "scalacheck"   % "1.13.5"
) map (_ % Test)

Release.settings