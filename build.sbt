import org.scalajs.linker.interface.ModuleSplitStyle

lazy val root = project
  .in(file("."))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    name         := "mortgage-visuals",
    scalaVersion := "3.3.0",
    scalacOptions ++= Seq(
      "-encoding",
      "utf-8",
      "-deprecation",
      "-feature",
      "-Wunused:all"
    ),
    scalaJSUseMainModuleInitializer := true,
    scalaJSLinkerConfig ~= {
      _.withModuleKind(ModuleKind.ESModule)
        .withModuleSplitStyle(ModuleSplitStyle.SmallModulesFor(List("example")))
    },
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom"        % "2.4.0",
      "be.doeraene"  %%% "web-components-ui5" % "1.10.0",
      "com.raquo"    %%% "laminar"            % "16.0.0"
    )
  )
