import java.util.Date

val kotlinx_html_version: String = "0.7.2"
val currentVersion = versioning.info.base.let { if (it.isEmpty()) "dev" else it }

group = "com.centyllion"
version = currentVersion

plugins {
    kotlin("js") version "1.4.10"
    id("fr.coppernic.versioning") version "3.1.2"
    id("com.jfrog.bintray") version "1.8.4"
    id("maven-publish")
}

apply {
    plugin("com.jfrog.bintray")
}


repositories {
    jcenter()
}

kotlin {
    js { browser() }
}

dependencies {
    implementation(kotlin("stdlib-js"))
    implementation("org.jetbrains.kotlinx:kotlinx-html-js:$kotlinx_html_version")

    implementation(npm("bulma-toast", "2.0.3", generateExternals = false))

    testImplementation(kotlin("test-js"))
}

tasks {
    publishToMavenLocal { dependsOn(build) }
    bintrayUpload { dependsOn(publishToMavenLocal) }
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
    publications {
        components.forEach { createPublication(it.name) }
    }
}

bintray {
    user = System.getenv("BINTRAY_USER") ?: System.getProperty("bintray.user")
    key = System.getenv("BINTRAY_KEY") ?: System.getProperty("bintray.key")
    publish = true
    override = true


    setPublications(
            *publishing.publications
                    .map { it.name }
                    .filter { it != "kotlinMultiplatform"
                    }.toTypedArray()
    )

    pkg.apply {
        repo = "Libraries"
        userOrg = "centyllion"
        name = rootProject.name
        setLicenses("Apache-2.0")
        setLabels("Kotlin", "JS", "Bulma")
        vcsUrl = pomScmUrl
        websiteUrl = pomUrl
        issueTrackerUrl = pomIssueUrl
        githubRepo = repo
        githubReleaseNotesFile = githubReadme

        version.apply {
            name = currentVersion
            desc = pomDesc
            released = Date().toString()
            vcsTag = project.versioning.info.tag
        }
    }
}