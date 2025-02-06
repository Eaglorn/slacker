package controllers

import Data
import javafx.scene.control.TextField
import javafx.stage.DirectoryChooser
import javafx.stage.FileChooser

class SettingController(val fieldLoadDatabase: TextField, val fieldLoadTemplates: TextField) {

    fun onButtonClickLoadDataBase() {
        val fileChooser = FileChooser()
        fileChooser.title = "Файл базы данных"
        fileChooser.extensionFilters.addAll(
            FileChooser.ExtensionFilter("DataBase", "*.db"),
        )

        val selectedFile = fileChooser.showOpenDialog(Data.companion.scene.window)
        if (selectedFile != null) {
            fieldLoadDatabase.text = selectedFile.absolutePath
        }
    }

    fun onButtonClickLoadTemplates() {
        val directoryChooser = DirectoryChooser()
        directoryChooser.title = "Каталог c шаблонами"

        val selectedDirectory = directoryChooser.showDialog(Data.companion.scene.window)
        if (selectedDirectory != null) {
            fieldLoadTemplates.text = selectedDirectory.absolutePath
        }
    }
}