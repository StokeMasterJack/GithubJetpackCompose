include(":ssutil", ":app")
rootProject.name = "JetpackGithub"
rootProject.buildFileName = "build.gradle.kts"
project(":ssutil").projectDir =  File(rootProject.projectDir,"../ssutil")
