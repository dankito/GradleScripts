
setExtraValueIfNotSet("useNewSonatypeRepo", true)
setExtraValueIfNotSet("packageGroup", "net.codinux")

setExtraValueIfNotSet("developerId", "codinux")
setExtraValueIfNotSet("developerName", "codinux GmbH & Co. KG")
setExtraValueIfNotSet("developerMail", "git@codinux.net")

// defined in user's global gradle.properties
val codinuxSonatypeTokenUsername: String by project
val codinuxSonatypeTokenPassword: String by project

setExtraValueIfNotSet("sonatypeUsername", codinuxSonatypeTokenUsername)
setExtraValueIfNotSet("sonatypePassword", codinuxSonatypeTokenPassword)


apply(from = File(buildscript.sourceFile?.parentFile, "publish.gradle.kts"))


fun setExtraValueIfNotSet(name: String, vararg values: Any?) {
  if (isExtraValueSet(name) == false) {
    extra[name] = values.firstOrNull { it != null }
  }
}

fun isExtraValueSet(name: String) =
  extra.has(name)
