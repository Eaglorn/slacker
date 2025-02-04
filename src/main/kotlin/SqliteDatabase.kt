import org.ktorm.database.Database
import org.ktorm.logging.Slf4jLoggerAdapter
import org.slf4j.LoggerFactory
import org.slf4j.Logger

class SqliteDatabase {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    fun connect(): Database {
        return Database.connect(
            url = "jdbc:sqlite:slacker.db",
            logger = Slf4jLoggerAdapter(logger.name)
        )
    }

    fun execSqlScript(filename: String, database: Database) {
        database.useConnection { conn ->
            conn.createStatement().use { statement ->
                javaClass.classLoader
                    ?.getResourceAsStream(filename)
                    ?.bufferedReader()
                    ?.use { reader ->
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