package ru.fku.slacker

import javafx.application.Application
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext
import ru.fku.slacker.SlackerApplication.Companion.applicationContext
import ru.fku.slacker.controllers.SlackerController

@SpringBootApplication(scanBasePackages = ["ru.fku.slacker"])
open class SlackerApplication {
    @Suppress("unused")
    private val logger : Logger = LoggerFactory.getLogger(this.javaClass)

    init {
        Data.run {
            dictMap.put("DB.Create", "Создание записи ({par0}).")
            dictMap.put("DB.Edit", "Редактирование записи ({par0}).")
            dictMap.put("DB.Delete", "Удаление записи ({par0}).")
            dictMap.put("DB.IsIndentFields", "В ({par0}) базе данных найдена запись с идентичными значениями полей.")
            dictMap.put("DB.IsEmptyFields", "Все поля должны быть заполнены.")
            dictMap.put("DB.IsSelectRecord", "Отсутсвует выбор записи в таблице.")
            dictMap.put("DB.IsSelectId", "В базе не существует записи с указанным идентификатором.")
        }
    }

    companion object {
        var templateCreate : Boolean = false
        var databaseCreate : Boolean = true
        lateinit var applicationContext : ApplicationContext
        lateinit var controller : SlackerController
    }
}

val logger : Logger = LoggerFactory.getLogger("Main")

fun main() {
    applicationContext = runApplication<SlackerApplication>()
    Application.launch(SlackerFX::class.java)
}