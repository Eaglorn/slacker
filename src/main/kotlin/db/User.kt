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

data class User(val id: Int?, val name: String?, val post: String?, val address: String?) {
    companion object {
        fun createDatabase(conn: Connection) {
            val tableExists = conn.createStatement()
                .executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='user'").next()

            if (!tableExists) {
                conn.createStatement().executeUpdate(
                    """
                    CREATE TABLE user (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        name TEXT NOT NULL
                        post TEXT NOT NULL
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
    val name = text("name")
    val post = text("post")
    val address = text("address")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = User(
        id = row[id] ?: 0,
        name = row[name].orEmpty(),
        post = row[post].orEmpty(),
        address = row[address].orEmpty(),
    )
}

class UserTable() {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    private var id: IntegerProperty? = null
    private var name: StringProperty? = null
    private var post: StringProperty? = null
    private var address: StringProperty? = null

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

    fun setPost(value: String) {
        postProperty().set(value)
    }

    fun getPost(): String {
        return postProperty().get()
    }

    fun postProperty(): StringProperty {
        if (post == null) post = SimpleStringProperty(this, "")
        return post as StringProperty
    }

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

    constructor(id: Int?, name: String?, post: String?, address: String?) : this() {
        if (id != null) {
            this.setId(id)
        }
        if (name != null) {
            this.setName(name)
        }
        if (post != null) {
            this.setPost(post)
        }
        if (address != null) {
            this.setAddress(address)
        }
    }
}