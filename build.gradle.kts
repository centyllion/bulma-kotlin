import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile

val kotlinx_html_version: String by project

group = "com.centyllion"
version = "0.1"

plugins {
    id("kotlin2js") version "1.3.50"
}

repositories {
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-js"))
    implementation("org.jetbrains.kotlinx:kotlinx-html-js:$kotlinx_html_version")
}


tasks.withType<Kotlin2JsCompile> {
    kotlinOptions.moduleKind = "amd"
}
