package db

import javafx.beans.property.IntegerProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.BaseTable
import org.ktorm.schema.int
import org.ktorm.schema.text
import org.ktorm.schema.varchar
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.sql.Connection

data class Maker(val id: Int?, val name: String?) {
    companion object {
        fun createDatabase(conn: Connection) {
            val tableExists = conn.createStatement()
                .executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='maker'").next()

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
    val name = text("name")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = Maker(
        id = row[id] ?: 0,
        name = row[name].orEmpty()
    )
}

class MakerTable() {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    private var id: IntegerProperty? = null

    fun setId(value: Int) {
        idProperty().set(value)
    }

    fun getId(): Int {
        return idProperty().get()
    }

    fun idProperty(): IntegerProperty {
        if (id == null) id = SimpleIntegerProperty(this, "0")
        return id as IntegerProperty
    }

    private var name: StringProperty? = null

    fun setName(value: String) {
        nameProperty().set(value)
    }

    fun getName(): String {
        return nameProperty().get()
    }

    fun nameProperty(): StringProperty {
        if (name == null) name = SimpleStringProperty(this, "")
        return name as StringProperty
    }

    constructor(id: Int?, name: String?) : this() {
        if (id != null) {
            this.setId(id)
        }
        if (name != null) {
            this.setName(name)
        }
    }
}