import org.gradle.api.publish.PublishingExtension

apply(plugin = "maven-publish")
apply(plugin = "signing")

// defined in publish-codinux.gradle.kts respectively publish-dankito.gradle.kts
val repositoryReleaseUrl: String by project
val repositorySnapshotsUrl: String by project

val repositoryUsername: String? by project
val repositoryPassword: String? by project

val groupId: String = getOrDefault("groupId", project.group as String)
val customArtifactId: String? by extra
val artifactVersion: String = getOrDefault("artifactVersion", project.version as String)

val projectDescription: String by extra
val sourceCodeRepositoryBaseUrl: String by extra

val licenseName: String = getOrDefault("licenseName", "The Apache License, Version 2.0")
val licenseUrl: String = getOrDefault("licenseUrl", "http://www.apache.org/licenses/LICENSE-2.0.txt")

// optional properties
val orgId: String? by project
val orgName: String? by project
val orgUrl: String? by project
val developerId: String? by project
val developerName: String? by project
val developerMail: String? by project


fun <T> getOrDefault(name: String, defaultValue: T): T {
  return project.findProperty(name) as? T ?: defaultValue
}


tasks {
    create<Jar>("javadocJar") {
        archiveClassifier.set("javadoc")
//        dependsOn(dokkaHtml)
//        from(dokkaHtml.get().outputDirectory)
    }
}

val isKotlinMultiplatformProject = project.plugins.any { it::class.java.name.startsWith("org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPlugin") }

configure<PublishingExtension> {
  val publishing = this
  publications {
    if (isKotlinMultiplatformProject) {
      withType<MavenPublication> {
        val artifactIdToUse = customArtifactId?.let { customArtifactId ->
          if (name.isNullOrEmpty() || name == "kotlinMultiplatform") {
            customArtifactId
          } else {
            "$customArtifactId-$name"
          }
        } ?: artifactId

        this.configure(artifactIdToUse)
      }
    } else {
      create<MavenPublication>("mavenJava") {
        from(components["java"])

        val artifactIdToUse = customArtifactId ?: artifactId

        this.configure(artifactIdToUse)
      }
    }
  }

  repositories {
    maven(getRepositoryUrl(artifactVersion)) {
      if (repositoryUsername != null && repositoryPassword != null) {
        credentials {
          username = repositoryUsername
          password = repositoryPassword
        }
      }
    }
  }

  if (repositoryUsername != null && repositoryPassword != null) {
    configure<SigningExtension> {
      sign(publishing.publications)
    }
  }
}

fun org.gradle.api.publish.maven.MavenPublication.configure(artifactIdToUse: String) {
  groupId = groupId
  artifactId = artifactIdToUse
  version = artifactVersion

  println("Publishing $groupId:$artifactId:$version ($name)")

  if (name == "jvm" || name == "mavenJava") {
    artifact(tasks["javadocJar"])
  }

  pom {
    name.set(artifactIdToUse)
    description.set(projectDescription)
    url.set("https://$sourceCodeRepositoryBaseUrl")

    licenses {
      license {
        name.set(licenseName)
        url.set(licenseUrl)
      }
    }

    scm {
      url.set("https://$sourceCodeRepositoryBaseUrl")
      connection.set("scm:git:git://${sourceCodeRepositoryBaseUrl}.git")
      developerConnection.set("scm:git:ssh://${sourceCodeRepositoryBaseUrl}.git")
    }

    developers {
      if (!developerId.isNullOrEmpty()) {
        developer {
          id.set(developerId)
          name.set(developerName)
          email.set(developerMail)
        }
      }
      if (!orgId.isNullOrEmpty()) {
        developer {
          id.set(orgId)
          name.set(orgName)
          organization.set(orgName)
          organizationUrl.set(orgUrl)
        }
      }
    }
    if (!orgName.isNullOrEmpty()) {
      organization {
        name.set(orgName)
        if (!orgUrl.isNullOrEmpty())
          url.set(orgUrl)
      }
    }
  }
}

fun getRepositoryUrl(artifactVersion: String): String {
  val isReleaseVersion = artifactVersion.endsWith("SNAPSHOT") == false

  return if (isReleaseVersion) repositoryReleaseUrl
    else repositorySnapshotsUrl
}
