import sbt._
import sbt.Keys._
import sbtrelease.ReleasePlugin.autoImport._
import sbtrelease.ReleasePlugin.autoImport.ReleaseTransformations._
import xerial.sbt.Sonatype.SonatypeKeys._

object Release {

  val settings =
    Seq(
      releaseCrossBuild := true,

      sonatypeProfileName := "org.r10r",
      publishMavenStyle := true,

      pomExtra := {
        <url>https://github.com/r10r-org/wsclientmockmachine</url>
          <licenses>
            <license>
              <name>MIT</name>
              <url>http://opensource.org/licenses/MIT</url>
            </license>
          </licenses>
          <scm>
            <connection>scm:git:github.com/r10r-org/wsclientmockmachine</connection>
            <developerConnection>scm:git:git@github.com:/r10r-org/wsclientmockmachine</developerConnection>
            <url>github.com/r10r-org/wsclientmockmachine</url>
          </scm>
          <developers>
            <developer>
              <id>rbauer</id>
              <name>Raphael Bauer</name>
              <url>http://r10r.org</url>
            </developer>
          </developers>
      },

      releaseProcess := Seq[ReleaseStep](
        checkSnapshotDependencies,
        inquireVersions,
        runClean,
        runTest,
        setReleaseVersion,
        commitReleaseVersion,
        tagRelease,
        ReleaseStep(action = Command.process("publishSigned", _), enableCrossBuild = true),
        setNextVersion,
        commitNextVersion,
        ReleaseStep(action = Command.process("sonatypeReleaseAll", _), enableCrossBuild = true),
        pushChanges
      )
    )
}