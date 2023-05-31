
setExtraValueIfNotSet("useNewSonatypeRepo", false)
setExtraValueIfNotSet("packageGroup", "net.dankito")

setExtraValueIfNotSet("developerId", "dankito")
setExtraValueIfNotSet("developerName", "Christian Dankl")
setExtraValueIfNotSet("developerMail", "maven@dankito.net")


val publishScript = File(File(project.gradle.gradleUserHomeDir, "scripts"), "publish.gradle.kts")
apply(from = publishScript)


fun setExtraValueIfNotSet(name: String, vararg values: Any?) {
  if (isExtraValueSet(name) == false) {
    extra[name] = values.firstOrNull { it != null }
  }
}

fun isExtraValueSet(name: String) =
  extra.has(name)
