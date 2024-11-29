
setExtraValueIfNotSet("developerId", "codinux")
setExtraValueIfNotSet("developerName", "codinux GmbH & Co. KG")
setExtraValueIfNotSet("developerMail", "git@codinux.net")

setExtraValueIfNotSet("repositoryReleaseUrl", "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
setExtraValueIfNotSet("repositorySnapshotsUrl", "https://s01.oss.sonatype.org/content/repositories/snapshots/")

// defined in user's global gradle.properties
val codinuxSonatypeTokenUsername: String? by project
val codinuxSonatypeTokenPassword: String? by project

setExtraValueIfNotSet("repositoryUsername", codinuxSonatypeTokenUsername)
setExtraValueIfNotSet("repositoryPassword", codinuxSonatypeTokenPassword)

setExtraValueIfNotSet("packageGroup", "net.codinux")


apply(from = File(buildscript.sourceFile?.parentFile, "publish.gradle.kts"))


fun setExtraValueIfNotSet(name: String, vararg values: Any?) {
  if (isExtraValueSet(name) == false) {
    extra[name] = values.firstOrNull { it != null }
  }
}

fun isExtraValueSet(name: String) =
  extra.has(name)
