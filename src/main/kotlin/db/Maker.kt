package db

import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.BaseTable
import org.ktorm.schema.int
import org.ktorm.schema.varchar
import java.sql.Connection
import kotlin.text.orEmpty

data class Maker(val id: Int?, val name: String?) {
    companion object

    companion {
         fun createDatabase (conn: Connection) {
            val tableExists = conn.createStatement().executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='maker'").next()

            if (!tableExists) {
                conn.createStatement().executeUpdate(
                    """
                    CREATE TABLE maker (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        name TEXT NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }
    }
}

object Makers : BaseTable<Maker>("maker") {
    val id = int("id").primaryKey()
    val name = varchar("name")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = Maker(
        id = row[id] ?: 0,
        name = row[name].orEmpty()
    )
}