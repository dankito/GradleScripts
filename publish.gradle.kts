import org.gradle.api.publish.PublishingExtension

apply(plugin = "maven-publish")
apply(plugin = "signing")

// defined in user's global gradle.properties
val sonatypeUsername: String? by project
val sonatypePassword: String? by project

val groupId: String = getOrDefault("groupId", project.group as String)
val artifactId: String by extra
val artifactVersion: String = getOrDefault("artifactVersion", project.version as String)

val projectDescription: String by extra
val sourceCodeRepositoryBaseUrl: String by extra
val useNewSonatypeRepo: Boolean by extra

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

configure<PublishingExtension> {
  val publishing = this
  publications {
    withType<MavenPublication> {
      groupId = groupId
      artifactId = artifactId
      version = artifactVersion

      if (name == "jvm") {
        artifact(tasks["javadocJar"])
      }

      pom {
        name.set(artifactId)
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
  }

  repositories {
    maven(getSonatypeUrl(useNewSonatypeRepo, artifactVersion)) {
      if (sonatypeUsername != null && sonatypePassword != null) {
        credentials {
          username = sonatypeUsername
          password = sonatypePassword
        }
      }
    }
  }

  if (sonatypeUsername != null && sonatypePassword != null) {
    configure<SigningExtension> {
      sign(publishing.publications)
    }
  }
}

fun getSonatypeUrl(useNewSonatypeRepo: Boolean, artifactVersion: String): String {
  val isReleaseVersion = artifactVersion.endsWith("SNAPSHOT") == false

  return if (useNewSonatypeRepo) {
    if (isReleaseVersion) "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
    else "https://s01.oss.sonatype.org/content/repositories/snapshots/"
  } else {
    if (isReleaseVersion) "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
    else "https://oss.sonatype.org/content/repositories/snapshots/"
  }
}
