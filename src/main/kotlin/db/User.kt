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
import java.sql.Connection

data class User(val id: Int?, val address: String?) {
    companion object {
        fun createDatabase(conn: Connection) {
            val tableExists = conn.createStatement()
                .executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='user'").next()

            if (!tableExists) {
                conn.createStatement().executeUpdate(
                    """
                    CREATE TABLE user (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        address TEXT NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }
    }
}

object Users : BaseTable<User>("user") {
    val id = int("id").primaryKey()
    val address = text("address")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = User(
        id = row[id] ?: 0,
        address = row[address].orEmpty(),
    )
}

class UserTable() {
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

    private var address: StringProperty? = null

    fun setAddress(value: String) {
        addressProperty().set(value)
    }

    fun getAddress(): String {
        return addressProperty().get()
    }

    fun addressProperty(): StringProperty {
        if (address == null) address = SimpleStringProperty(this, "")
        return address as StringProperty
    }

    constructor(id: Int?, address: String?) : this() {
        if (id != null) {
            this.setId(id)
        }
        if (address != null) {
            this.setAddress(address)
        }
    }
}