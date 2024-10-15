
setExtraValueIfNotSet("developerId", "codinux")
setExtraValueIfNotSet("developerName", "codinux GmbH & Co. KG")
setExtraValueIfNotSet("developerMail", "git@codinux.net")

setExtraValueIfNotSet("repositoryReleaseUrl", "https://maven.dankito.net/api/packages/codinux/maven")
setExtraValueIfNotSet("repositorySnapshotsUrl", "https://maven.dankito.net/api/packages/codinux/maven")

// defined in user's global gradle.properties
val codinuxRegistryWriterUsername: String by project
val codinuxRegistryWriterPassword: String by project

setExtraValueIfNotSet("repositoryUsername", codinuxRegistryWriterUsername)
setExtraValueIfNotSet("repositoryPassword", codinuxRegistryWriterPassword)


apply(from = File(buildscript.sourceFile?.parentFile, "publish.gradle.kts"))


fun setExtraValueIfNotSet(name: String, vararg values: Any?) {
  if (isExtraValueSet(name) == false) {
    extra[name] = values.firstOrNull { it != null }
  }
}

fun isExtraValueSet(name: String) =
  extra.has(name)
