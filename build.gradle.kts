plugins {
    id("java")
    id("application")
    id("org.jetbrains.kotlin.jvm") version "2.1.10"
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("org.beryx.runtime") version "1.13.1"
    id("org.springframework.boot") version "3.4.3"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "ru.fku.app"
version = "0.0.1"

repositories {
    mavenCentral()
}

application {
    mainClass = "ru.fku.slacker.SlackerApplicationKt"
    applicationName = "Slacker"
}

kotlin {
    jvmToolchain(23)
}

val kotlin_version = "2.1.10"
val kotlinx_coroutines_version = "1.10.1"
val ktorm_version = "4.1.1"
val sqlite_jdbc_version = "3.49.1.0"
val controls_fx_version = "11.2.2"
val tiles_fx_version = "21.0.9"
val log4j_version = "2.24.3"
val disruptor_version = "4.0.0"
val gson_vesrion = "2.12.1"
val commons_io_version = "2.18.0"
val apache_poi_ooxml_version = "5.4.0"
val reflections_version = "4.16.0.Final"
val spring_version = "3.4.3"
val kotlin_reflect_version = "2.1.10"
val korge_version = "5.1.0"
val eclipse_collections = "11.1.0"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinx_coroutines_version")
    implementation("org.controlsfx:controlsfx:$controls_fx_version")
    implementation("eu.hansolo:tilesfx:$tiles_fx_version")
    implementation("org.ktorm:ktorm-core:$ktorm_version")
    implementation("org.ktorm:ktorm-support-sqlite:$ktorm_version")
    implementation("org.xerial:sqlite-jdbc:$sqlite_jdbc_version")
    implementation("org.apache.logging.log4j:log4j-api:$log4j_version")
    implementation("org.apache.logging.log4j:log4j-core:$log4j_version")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:$log4j_version")
    implementation("com.lmax:disruptor:$disruptor_version")
    implementation("com.google.code.gson:gson:$gson_vesrion")
    implementation("commons-io:commons-io:$commons_io_version")
    implementation("org.apache.poi:poi-ooxml:$apache_poi_ooxml_version")
    implementation("org.jboss.errai.reflections:reflections:$reflections_version")
    implementation("org.springframework.boot:spring-boot-starter:$spring_version") {
        exclude("org.springframework.boot","spring-boot-starter-logging")
    }
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlin_reflect_version")
    implementation("com.soywiz.korge:korge-foundation:$korge_version")
    implementation("com.soywiz.korge:korge-core:$korge_version")
    implementation("org.eclipse.collections:eclipse-collections-api:$eclipse_collections")
    implementation("org.eclipse.collections:eclipse-collections:$eclipse_collections")
}

javafx {
    version = "24"
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
