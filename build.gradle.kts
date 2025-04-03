plugins {
    id("java")
    id("application")
    id("org.jetbrains.kotlin.jvm") version "2.1.20"
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

val kotlinVersion = "2.1.20"
val kotlinxCoroutinesVersion = "1.10.1"
val ktormVersion = "4.1.1"
val sqliteJdbcVersion = "3.49.1.0"
val controlsFxVersion = "11.2.2"
val log4jVersion = "2.24.3"
val gsonVersion = "2.12.1"
val commonsIoVersion = "2.18.0"
val apachePoiOoxmlVersion = "5.4.0"
val springVersion = "3.4.4"
val eclipseCollections = "11.1.0"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion")
    implementation("org.controlsfx:controlsfx:$controlsFxVersion")
    implementation("org.ktorm:ktorm-core:$ktormVersion")
    implementation("org.ktorm:ktorm-support-sqlite:$ktormVersion")
    implementation("org.xerial:sqlite-jdbc:$sqliteJdbcVersion")
    implementation("org.apache.logging.log4j:log4j-api:$log4jVersion")
    implementation("org.apache.logging.log4j:log4j-core:$log4jVersion")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:$log4jVersion")
    implementation("com.google.code.gson:gson:$gsonVersion")
    implementation("commons-io:commons-io:$commonsIoVersion")
    implementation("org.apache.poi:poi-ooxml:$apachePoiOoxmlVersion")
    implementation("org.springframework.boot:spring-boot-starter:$springVersion") {
        exclude("org.springframework.boot","spring-boot-starter-logging")
    }
    implementation("org.eclipse.collections:eclipse-collections-api:$eclipseCollections")
    implementation("org.eclipse.collections:eclipse-collections:$eclipseCollections")
}

javafx {
    version = "24"
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
