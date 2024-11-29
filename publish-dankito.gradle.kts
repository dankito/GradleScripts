
setExtraValueIfNotSet("developerId", "dankito")
setExtraValueIfNotSet("developerName", "Christian Dankl")
setExtraValueIfNotSet("developerMail", "maven@dankito.net")

setExtraValueIfNotSet("repositoryReleaseUrl", "https://oss.sonatype.org/service/local/staging/deploy/maven2/")
setExtraValueIfNotSet("repositorySnapshotsUrl", "https://oss.sonatype.org/content/repositories/snapshots/")

// defined in user's global gradle.properties
val dankitoSonatypeTokenUsername: String? by project
val dankitoSonatypeTokenPassword: String? by project

setExtraValueIfNotSet("repositoryUsername", dankitoSonatypeTokenUsername)
setExtraValueIfNotSet("repositoryPassword", dankitoSonatypeTokenPassword)

setExtraValueIfNotSet("packageGroup", "net.dankito")


apply(from = File(buildscript.sourceFile?.parentFile, "publish.gradle.kts"))


fun setExtraValueIfNotSet(name: String, vararg values: Any?) {
  if (isExtraValueSet(name) == false) {
    extra[name] = values.firstOrNull { it != null }
  }
}

fun isExtraValueSet(name: String) =
  extra.has(name)
