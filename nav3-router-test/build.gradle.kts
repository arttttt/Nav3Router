import com.vanniktech.maven.publish.KotlinMultiplatform
import com.vanniktech.maven.publish.SourcesJar

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.vanniktech.mavenPublish)
    alias(libs.plugins.signing)
}

kotlin {
    android {
        namespace = "com.arttttt.nav3router.test"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
    }

    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(project(":nav3-router"))
            implementation(libs.androidx.navigation3.runtime)
        }
    }
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()

    configure(
        KotlinMultiplatform(
            sourcesJar = SourcesJar.Sources(),
        )
    )

    coordinates("io.github.arttttt.nav3router", "nav3router-test", libs.versions.nav3router.get())

    pom {
        name.set("Nav3 Router Test")
        description.set("Test-support helpers for Nav3 Router — drive and assert navigation without Compose.")
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
