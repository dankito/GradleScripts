# Gradle scrips

Common Gradle scripts I use in multiple projects e.g. to add an Integration Test configuration and 
publish artifacts to local Maven repository and Maven Central or releasing artifacts published to Maven Central.

## Usage

I keep my scripts in GRADLE_USER_HOME/scripts. (GRADLE_USER_HOME usually refers to /home/<your_username>/.gradle on Linux, 
/User/<your_username>/.gradle on macOS and C:\Users\\<your_username>\\.gradle on Windows.)

Scripts in GRADLE_USER_HOME can be applied from all Gradle projects:

Groovy:
```
def commonScriptsFile = new File(new File(project.gradle.gradleUserHomeDir, "scripts"), "commonScripts.gradle")
if (commonScriptsFile.exists()) {
    apply from: commonScriptsFile
}
```

Kotlin:
```
val commonScriptsFile = File(File(project.gradle.gradleUserHomeDir, "scripts"), "commonScripts.gradle")
if (commonScriptsFile.exists()) {
    apply(from = commonScriptsFile)
}
```