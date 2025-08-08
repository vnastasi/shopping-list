import com.android.build.api.artifact.SingleArtifact
import md.vnastasi.plugin.abs.task.CopyAndroidArtifact
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("application.conventions")
    id("secrets")
    id("app-build-support")
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

android {
    namespace = "md.vnastasi.shoppinglist"

    signingConfigs {
        register("release") {
            storeType = "JKS"
            storeFile = secrets.keystore.file.get().asFile
            storePassword = secrets.keystore.storePassword.get()
            keyAlias = secrets.keystore.keyAlias.get()
            keyPassword = secrets.keystore.keyPassword.get()
        }
    }

    defaultConfig {
        applicationId = libs.versions.project.application.id.get()
        targetSdk = libs.versions.project.targetSdk.get().toInt()
        versionCode = libs.versions.project.version.code.get().toInt()
        versionName = libs.versions.project.version.name.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    sourceSets {
        getByName("androidTest") {
            assets {
                srcDir("${project(":database").layout.projectDirectory}/schemas")
            }
        }
    }
}

androidComponents {
    onVariants(selector().withBuildType("release")) { applicationVariant ->
        project.tasks.register<CopyAndroidArtifact>("copy${applicationVariant.name.toString().replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }}ApkArtifact") {
            artifactDirectory.set(applicationVariant.artifacts.get(SingleArtifact.APK))
            artifactLoader.set(applicationVariant.artifacts.getBuiltArtifactsLoader())
            targetDirectory.set(rootProject.layout.buildDirectory.dir("artifacts/apk"))
            fileName.set("shopping-list-v${libs.versions.project.version.name.get()}.apk")
        }
    }
}

dependencies {
    implementation(projects.database)
    implementation(projects.domain.implementation)
    implementation(projects.screen.addItems)
    implementation(projects.screen.listDetails)
    implementation(projects.screen.overview)
    implementation(projects.screen.shared)
    implementation(projects.support.theme)

    implementation(platform(libs.compose.bom))
    implementation(platform(libs.coroutines.bom))
    implementation(platform(libs.kotlin.bom))

    implementation(libs.activity)
    implementation(libs.activity.compose)
    implementation(libs.androidx.core)
    implementation(libs.androidx.core)
    implementation(libs.collections.immutable)
    implementation(libs.compose.animations)
    implementation(libs.compose.animations.core)
    implementation(libs.compose.runtime)
    implementation(libs.compose.ui)
    implementation(libs.koin.android)
    implementation(libs.koin.core)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.viewmodel.savedstate)
    implementation(libs.navigation.common)
    implementation(libs.navigation.compose)
    implementation(libs.navigation.runtime)
    implementation(libs.serialization.core)

    androidTestImplementation(projects.resources)

    androidTestImplementation(testFixtures(projects.database))

    androidTestImplementation(platform(libs.compose.bom))

    androidTestImplementation(libs.assertk)
    androidTestImplementation(libs.compose.test.junit4)
    androidTestImplementation(libs.compose.ui.test)
    androidTestImplementation(libs.coroutines.core)
    androidTestImplementation(libs.coroutines.test)
    androidTestImplementation(libs.espresso.idling.concurrent)
    androidTestImplementation(libs.koin.core)
    androidTestImplementation(libs.kotlin.reflect)
    androidTestImplementation(libs.room.runtime)
    androidTestImplementation(libs.room.test)
    androidTestImplementation(libs.sqlite)
    androidTestImplementation(libs.sqlite.framework)
    androidTestImplementation(libs.turbine)
    androidTestImplementation(libs.uiautomator)
    androidTestImplementation(libs.uitest.core)
    androidTestImplementation(libs.uitest.junit)
    androidTestImplementation(libs.uitest.monitor)
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs.add("-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi")
    }
}
