import org.ktorm.database.Database
import org.ktorm.dsl.QueryRowSet
import org.ktorm.logging.Slf4jLoggerAdapter
import org.ktorm.schema.BaseTable
import org.slf4j.LoggerFactory
import org.slf4j.Logger
import org.ktorm.schema.int
import org.ktorm.schema.varchar

data class Maker(val id: Int, val name: String)

object Makers : BaseTable<Maker>("maker") {
    val id = int("id").primaryKey()
    val name = varchar("name")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = Maker(
        id = row[id] ?: 0,
        name = row[name].orEmpty()
    )
}

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