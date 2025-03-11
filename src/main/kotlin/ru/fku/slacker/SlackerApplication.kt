package ru.fku.slacker

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext
import ru.fku.slacker.SlackerApplication.Companion.applicationContext
import ru.fku.slacker.controllers.SlackerController
import java.io.FileInputStream
import java.io.FileOutputStream

@SpringBootApplication(scanBasePackages = ["ru.fku.slacker"])
open class SlackerApplication : Application() {
    @Suppress("unused")
    private val logger : Logger = LoggerFactory.getLogger(this.javaClass)

    companion object {
        lateinit var applicationContext : ApplicationContext
    }

    private lateinit var controller : SlackerController

    private fun beforeShow() {
        controller = Data.controller
        controller.beforeShow()
        val templatePath = Data.config.pathTemplates + "\\template.docx"
        val outputPath = "c:\\results\\result.docx"
        val variables = mapOf(
            "{name}" to "Алексей",
            "{date}" to "1 июня 1994 года"
        )
        FileInputStream(templatePath).use { fis ->
            XWPFDocument(fis).use { document ->
                for (paragraph in document.paragraphs) {
                    for (run in paragraph.runs) {
                        var text = run.getText(0)
                        if (text != null) {
                            for ((key, value) in variables) {
                                text = text.replace(key, value)
                            }
                            run.setText(text, 0)
                        }
                    }
                }
                FileOutputStream(outputPath).use { fos ->
                    document.write(fos)
                }
            }
        }
    }

    override fun start(stage : Stage) {
        runBlocking {
            launch {
                val fxmlLoader = FXMLLoader(SlackerApplication::class.java.getResource("../../../SlackerApplication.fxml"))
                val scene = Scene(fxmlLoader.load())
                stage.title = "Slacker"
                stage.scene = scene
                Data.scene = scene
                beforeShow()
                stage.show()
            }
        }
    }
}

val logger : Logger = LoggerFactory.getLogger("Main")

fun main() {
    applicationContext = runApplication<SlackerApplication>()
    Application.launch(SlackerApplication::class.java)
}