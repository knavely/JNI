name := "HelloWorldJNIwithRegisterNatives"

version := "0.0.1-SNAPSHOT"

organization := "org.digimead"

// Although I think I've locked sufficiently, sometimes I get test
// failures without this.
//parallelExecution in Test := false

scalacOptions ++= Seq("-unchecked", "-deprecation")

// call make -f Makefile.native clean
clean <<= (clean, resourceManaged in Compile, sourceDirectory, classDirectory in Compile,
      managedClasspath in Compile) map { (clean, dir, src, classDir, runPath) => {
    val home = System.getProperty("java.home")
    val basePath = runPath.map(_.data.toString).reduceLeft(_ + ":" + _)
    val classpath = classDir.toString + ":" + basePath
    val result = sbt.Process(
      "make" :: "-f" :: "Makefile.native" :: "clean" :: Nil,
      None,
      "COMPILE_PATH" -> classDir.toString,
      "CLASSPATH" -> classpath,
      "JAVA_HOME" -> home
      ) ! ;
    //
    if (result != 0)
      error("Error cleaning native library")
    clean
  }
}

// call make -f Makefile.native all
compile <<= (compile in Compile, resourceManaged in Compile, sourceDirectory, classDirectory in Compile,
      managedClasspath in Compile) map { (compile, dir, src, classDir, runPath) => {
    val superCompile = compile
    val home = System.getProperty("java.home")
    val basePath = runPath.map(_.data.toString).reduceLeft(_ + ":" + _)
    val classpath = classDir.toString + ":" + basePath
    val result = sbt.Process(
      "make" :: "-f" :: "Makefile.native" :: "all" :: Nil,
      None,
      "COMPILE_PATH" -> classDir.toString,
      "CLASSPATH" -> classpath,
      "JAVA_HOME" -> home
      ) ! ;
    //
    if (result != 0)
      error("Error compiling native library")
    superCompile
  }
}

fork in run := true

javaOptions in run += "-Djava.library.path=./target/so"


resolvers ++= Seq(
  "Sonatype Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/",
  "Sonatype Releases" at "http://oss.sonatype.org/content/repositories/releases/",
"Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
            "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/"
) 



libraryDependencies ++= Seq(
  "org.scala-saddle" %% "saddle-core" % "1.3.+",
  "net.sf.trove4j" % "trove4j" % "3.0.3",
  "com.twitter" % "algebird-core_2.9.3" % "0.2.0",
  "com.twitter" % "algebird-util_2.9.3" % "0.2.0",
  "org.scalanlp" % "breeze-math_2.10" % "0.5-SNAPSHOT",
  "org.scalanlp" % "breeze-viz_2.10" % "0.5-SNAPSHOT"
 // (OPTIONAL) "org.scala-saddle" %% "saddle-hdf5" % "1.3.+"
)

scalaVersion := "2.10.2"
