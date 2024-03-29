
val kotlinx_html_version: String = "0.7.3"
val currentVersion = versioning.info.base.let { if (it.isEmpty()) "dev" else it }

val grpUser: String by project
val grpToken: String by project

group = "com.centyllion"
version = currentVersion

plugins {
    kotlin("js") version "1.5.21"
    id("fr.coppernic.versioning") version "3.1.2"
    id("maven-publish")
}

repositories {
    mavenCentral()
}

kotlin {
    js(IR) { browser() }
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-html-js:$kotlinx_html_version")
    implementation(npm("bulma-toast", "2.0.3", generateExternals = false))

    testImplementation(kotlin("test-js"))
}

tasks {
    publishToMavenLocal { dependsOn(build) }
}

val artifactName = "bulma-kotlin"
val artifactGroup = "com.centyllion"

val repo = "centyllion/bulma-kotlin"
val pomUrl = "https://github.com/$repo"
val pomScmUrl = "https://github.com/$repo"
val pomIssueUrl = "$pomUrl/issues"
val pomDesc = "Kotlin library to create and control Bulma elements"

val pomScmConnection = "scm:git:git://github.com/$repo"
val pomScmDevConnection = pomScmConnection

val githubReadme = "README.md"

val pomLicenseName = "The Apache Software License, Version 2.0"
val pomLicenseUrl = "http://www.apache.org/licenses/LICENSE-2.0.txt"
val pomLicenseDist = "repo"

val pomDeveloperId = "centyllion"
val pomDeveloperName = "Centyllion"

fun PublicationContainer.createPublication(name: String) {
    create<MavenPublication>(name) {
        groupId = artifactGroup
        artifactId = artifactName
        version = currentVersion
        from(components[name])

        pom.withXml {
            asNode().apply {
                appendNode("description", pomDesc)
                appendNode("name", rootProject.name)
                appendNode("url", pomUrl)
                appendNode("licenses").appendNode("license").apply {
                    appendNode("name", pomLicenseName)
                    appendNode("url", pomLicenseUrl)
                    appendNode("distribution", pomLicenseDist)
                }
                appendNode("developers").appendNode("developer").apply {
                    appendNode("id", pomDeveloperId)
                    appendNode("name", pomDeveloperName)
                }
                appendNode("scm").apply {
                    appendNode("url", pomScmUrl)
                    appendNode("connection", pomScmConnection)
                }
            }
        }
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/centyllion/bulma-kotlin")
            credentials {
                username = grpUser
                password = grpToken
            }
        }
    }
    publications {
        components.forEach { createPublication(it.name) }
    }
}
