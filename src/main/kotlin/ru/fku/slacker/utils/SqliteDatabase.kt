package ru.fku.slacker.utils

import org.ktorm.database.Database
import org.ktorm.logging.Slf4jLoggerAdapter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import ru.fku.slacker.logger

class SqliteDatabase {
    @Suppress("unused")
    private val logger : Logger = LoggerFactory.getLogger(this.javaClass)

    companion object {
        @Bean
        fun connect(path : String) : Database {
            return Database.connect(
                url = "jdbc:sqlite:$path", logger = Slf4jLoggerAdapter(logger.name)
            )
        }

        fun execSqlScript(filename : String, database : Database) {
            database.useConnection { conn ->
                conn.createStatement().use { statement ->
                    javaClass.classLoader?.getResourceAsStream(filename)?.bufferedReader()?.use { reader ->
                        for (sql in reader.readText().split(';')) {
                            if (sql.any { it.isLetterOrDigit() }) {
                                statement.executeUpdate(sql)
                            }
                        }
                    }
                }
            }
        }
    }
}