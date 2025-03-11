package ru.fku.slacker.controllers

import javafx.scene.control.TextField
import javafx.stage.DirectoryChooser
import javafx.stage.FileChooser
import org.apache.commons.io.FileUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.fku.slacker.Config
import ru.fku.slacker.Data
import java.io.File

class SettingController(private val fieldLoadDatabase : TextField, private val fieldLoadTemplates : TextField) {
    @Suppress("unused")
    private val logger : Logger = LoggerFactory.getLogger(this.javaClass)

    fun onButtonClickLoadDataBase() {
        val fileChooser = FileChooser()
        fileChooser.title = "Файл базы данных"
        fileChooser.extensionFilters.addAll(FileChooser.ExtensionFilter("DataBase", "*.db"))
        val selectedFile = fileChooser.showOpenDialog(Data.Companion.scene.window)
        if (selectedFile != null) {
            fieldLoadDatabase.text = selectedFile.absolutePath
            Data.Companion.config.pathDB = selectedFile.absolutePath
            Data.Companion.config.save()
        }
        FileUtils.copyFile(File(Data.Companion.config.pathDB), File(Config.Companion.pathDBLocal))
    }

    fun onButtonClickLoadTemplates() {
        val directoryChooser = DirectoryChooser()
        directoryChooser.title = "Каталог c шаблонами"
        val selectedDirectory = directoryChooser.showDialog(Data.Companion.scene.window)
        if (selectedDirectory != null) {
            fieldLoadTemplates.text = selectedDirectory.absolutePath
            Data.Companion.config.pathTemplates = selectedDirectory.absolutePath
            Data.Companion.config.save()
        }
    }
}