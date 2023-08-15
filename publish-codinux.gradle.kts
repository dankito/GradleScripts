
setExtraValueIfNotSet("useNewSonatypeRepo", true)
setExtraValueIfNotSet("packageGroup", "net.codinux")

setExtraValueIfNotSet("developerId", "codinux")
setExtraValueIfNotSet("developerName", "codinux GmbH & Co. KG")
setExtraValueIfNotSet("developerMail", "git@codinux.net")


apply(from = File(buildscript.sourceFile?.parentFile, "publish.gradle.kts"))


fun setExtraValueIfNotSet(name: String, vararg values: Any?) {
  if (isExtraValueSet(name) == false) {
    extra[name] = values.firstOrNull { it != null }
  }
}

fun isExtraValueSet(name: String) =
  extra.has(name)
