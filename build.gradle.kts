val kotlin_version = "2.1.10"
val kotlinx_coroutines_version = "1.10.1"
val ktorm_version = "4.1.1"
val sqlite_jdbc_version = "3.48.0.0"
val controls_fx_version = "11.2.1"
val tiles_fx_version = "21.0.9"
val log4j_version = "2.24.3"
val disruptor_version = "4.0.0"
val poi_tl_version = "1.12.2"

plugins {
    id("java")
    id("application")
    id("org.jetbrains.kotlin.jvm") version "2.1.10"
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("org.beryx.runtime") version "1.13.1"
}

group = "ru.fku.app"
version = "0.0.1"

repositories {
    mavenCentral()
}

application {
    mainClass = "SlackerApplicationKt"
    applicationName = "Slacker"
}

kotlin {
    jvmToolchain(23)
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlin_version}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${kotlinx_coroutines_version}")
    implementation("org.controlsfx:controlsfx:${controls_fx_version}")
    implementation("eu.hansolo:tilesfx:${tiles_fx_version}")
    implementation("org.ktorm:ktorm-core:${ktorm_version}")
    implementation("org.ktorm:ktorm-support-sqlite:${ktorm_version}")
    implementation("org.xerial:sqlite-jdbc:${sqlite_jdbc_version}")
    implementation("org.apache.logging.log4j:log4j-api:${log4j_version}")
    implementation("org.apache.logging.log4j:log4j-core:${log4j_version}")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:${log4j_version}")
    implementation("com.lmax:disruptor:${disruptor_version}")
    implementation("com.deepoove:poi-tl:${poi_tl_version}")
}

javafx {
    version = "23.0.2"
    //modules("javafx.controls", "javafx.fxml", "javafx.web", "javafx.swing")
    modules = listOf("javafx.controls", "javafx.fxml")
}

runtime {
    options.add("--strip-debug")
    options.add("--compress")
    options.add("2")
    options.add("--no-header-files")
    options.add("--no-man-pages")

    targetPlatform("win") {
        jdkHome = jdkDownload("https://github.com/AdoptOpenJDK/semeru23-binaries/releases/download/jdk-23.0.1%2B11_openj9-0.48.0/ibm-semeru-open-jdk_x64_windows_23.0.1_11_openj9-0.48.0.zip")
    }
    launcher {
        noConsole = true
    }
    jpackage {
        val imgType = "png"
        imageOptions.add("--icon")
        imageOptions.add("src/main/resources/hellofx.$imgType")
        installerOptions.add("--resource-dir")
        installerOptions.add("src/main/resources")
        installerOptions.add("--vendor")
        installerOptions.add("Acme Corporation")
        installerOptions.add("--win-per-user-install")
        installerOptions.add("--win-dir-chooser")
        installerOptions.add("--win-menu")
        installerOptions.add("--win-shortcut")
    }
}
