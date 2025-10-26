import com.vanniktech.maven.publish.KotlinMultiplatform
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.vanniktech.mavenPublish)
    alias(libs.plugins.signing)
}

kotlin {
    androidLibrary {
        namespace = "com.arttttt.nav3router"
        compileSdk = libs.versions.android.compileSdk.get().toInt()

        packaging {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Nav3Router"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.ui)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.androidx.navigation3.runtime)
            implementation(libs.ui.backhandler)
        }

        androidMain.dependencies {
            implementation(libs.androidx.navigationevent.lib)
            implementation(libs.androidx.navigationevent.compose)
        }
    }
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()

    configure(
        KotlinMultiplatform(
            sourcesJar = true,
            androidVariantsToPublish = listOf("debug", "release"),
        )
    )

    coordinates("io.github.arttttt.nav3router", "nav3router", "1.0.2")

    pom {
        name.set("Nav3 Router")
        description.set("A simple yet powerful Kotlin Multiplatform navigation library built on top of Jetpack Navigation 3.")
        inceptionYear.set("2025")
        url.set("https://github.com/arttttt/Nav3Router")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("https://github.com/arttttt/Nav3Router/blob/master/LICENSE")
                distribution.set("https://github.com/arttttt/Nav3Router/blob/master/LICENSE")
            }
        }
        developers {
            developer {
                id.set("arttttt")
                name.set("Artem Bambalov")
                url.set("https://github.com/arttttt")
            }
        }
        scm {
            url.set("https://github.com/arttttt/Nav3Router/")
            connection.set("scm:git:git://github.com/arttttt/Nav3Router.git")
            developerConnection.set("scm:git:ssh://git@github.com/arttttt/Nav3Router.git")
        }
    }
}

signing {
    useGpgCmd()
}
