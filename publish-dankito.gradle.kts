
setExtraValueIfNotSet("useNewSonatypeRepo", false)
setExtraValueIfNotSet("packageGroup", "net.dankito")

setExtraValueIfNotSet("developerId", "dankito")
setExtraValueIfNotSet("developerName", "Christian Dankl")
setExtraValueIfNotSet("developerMail", "maven@dankito.net")


apply(from = File(buildscript.sourceFile?.parentFile, "publish.gradle.kts"))


fun setExtraValueIfNotSet(name: String, vararg values: Any?) {
  if (isExtraValueSet(name) == false) {
    extra[name] = values.firstOrNull { it != null }
  }
}

fun isExtraValueSet(name: String) =
  extra.has(name)
