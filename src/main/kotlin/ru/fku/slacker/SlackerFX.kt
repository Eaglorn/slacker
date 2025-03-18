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
import java.io.FileInputStream
import java.io.FileOutputStream

class SlackerFX : Application() {
    @Suppress("unused")
    private val logger : Logger = LoggerFactory.getLogger(this.javaClass)

    private fun beforeShow() {
        SlackerApplication.controller = Data.controller
        SlackerApplication.controller.beforeShow()
        val templatePath = Data.config.pathTemplates + "\\template.docx"
        val outputPath = "c:\\results\\result.docx"
        val variables = mapOf(
            "{name}" to "Алексей",
            "{date}" to "1 июня 1994 года"
        )
        if (SlackerApplication.templateCreate)
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
                val fxmlLoader =
                    FXMLLoader(SlackerApplication::class.java.getResource("SlackerApplication.fxml"))
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