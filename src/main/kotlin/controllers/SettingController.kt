package controllers

import Data
import javafx.scene.control.TextField
import javafx.stage.DirectoryChooser
import javafx.stage.FileChooser
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class SettingController(private val fieldLoadDatabase: TextField, private val fieldLoadTemplates: TextField) {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)
    private var data: Data.companion = Data.companion

    fun onButtonClickLoadDataBase() {
        val fileChooser = FileChooser()
        fileChooser.title = "Файл базы данных"
        fileChooser.extensionFilters.addAll(
            FileChooser.ExtensionFilter("DataBase", "*.db"),
        )

        val selectedFile = fileChooser.showOpenDialog(data.scene.window)
        if (selectedFile != null) {
            fieldLoadDatabase.text = selectedFile.absolutePath
            data.config.pathDB = selectedFile.absolutePath
            data.config.save()
        }
    }

    fun onButtonClickLoadTemplates() {
        val directoryChooser = DirectoryChooser()
        directoryChooser.title = "Каталог c шаблонами"

        val selectedDirectory = directoryChooser.showDialog(data.scene.window)
        if (selectedDirectory != null) {
            fieldLoadTemplates.text = selectedDirectory.absolutePath
            data.config.pathTemplates = selectedDirectory.absolutePath
            data.config.save()
        }
    }
}