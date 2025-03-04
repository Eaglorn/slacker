package db

import javafx.beans.property.IntegerProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import org.ktorm.dsl.QueryRowSet
import org.ktorm.schema.BaseTable
import org.ktorm.schema.int
import org.ktorm.schema.text
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import utils.Identifiable
import java.sql.Connection

data class TypeOfHardware(val id : Int?, val name : String?) {
    @Suppress("unused")
    private val logger : Logger = LoggerFactory.getLogger(this.javaClass)

    companion object {
        fun createDatabase(conn : Connection) {
            val tableExists = conn.createStatement()
                .executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='type_of_hardware'").next()
            if (! tableExists) {
                conn.createStatement().executeUpdate(
                    """
                    CREATE TABLE type_of_hardware (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        name TEXT NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }
    }
}

object TypeOfHardwares : BaseTable<TypeOfHardware>("type_of_hardware") {
    val id = int("id").primaryKey()
    val name = text("name")

    override fun doCreateEntity(row : QueryRowSet, withReferences : Boolean) = TypeOfHardware(
        id = row[id] ?: 0,
        name = row[name].orEmpty()
    )
}

class TypeOfHardwareTable(id : Int?, name : String?) : Identifiable {
    @Suppress("unused")
    private val logger : Logger = LoggerFactory.getLogger(this.javaClass)

    private var id : IntegerProperty = SimpleIntegerProperty(this, "id", 0)

    private fun setId(value : Int) {
        id.set(value)
    }

    override fun getId() : Int {
        return id.get()
    }

    private var name : StringProperty = SimpleStringProperty(this, "name", "")

    private fun setName(value : String) {
        name.set(value)
    }

    fun getName() : String {
        return name.get()
    }

    init {
        id?.let {
            this.setId(it)
        }
        name?.let {
            this.setName(it)
        }
    }
}