package controllers

import Config
import Data
import javafx.scene.control.TextField
import javafx.stage.DirectoryChooser
import javafx.stage.FileChooser
import org.apache.commons.io.FileUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

class SettingController(private val fieldLoadDatabase: TextField, private val fieldLoadTemplates: TextField) {
    @Suppress("unused") private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    fun onButtonClickLoadDataBase() {
        val fileChooser = FileChooser()
        fileChooser.title = "Файл базы данных"
        fileChooser.extensionFilters.addAll(FileChooser.ExtensionFilter("DataBase", "*.db"))
        val selectedFile = fileChooser.showOpenDialog(Data.scene.window)
        if (selectedFile != null) {
            fieldLoadDatabase.text = selectedFile.absolutePath
            Data.config.pathDB = selectedFile.absolutePath
            Data.config.save()
        }
        FileUtils.copyFile(File(Data.config.pathDB), File(Config.pathDBLocal))
    }

    fun onButtonClickLoadTemplates() {
        val directoryChooser = DirectoryChooser()
        directoryChooser.title = "Каталог c шаблонами"
        val selectedDirectory = directoryChooser.showDialog(Data.scene.window)
        if (selectedDirectory != null) {
            fieldLoadTemplates.text = selectedDirectory.absolutePath
            Data.config.pathTemplates = selectedDirectory.absolutePath
            Data.config.save()
        }
    }
}