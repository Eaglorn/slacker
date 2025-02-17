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

data class Model(val id: Int?, val name: String?, val maker: Int?, val typeofhardware: Int?) {
    companion object {
        fun createDatabase(conn: Connection) {
            val tableExists = conn.createStatement()
                .executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='model'").next()

            if (!tableExists) {
                conn.createStatement().executeUpdate(
                    """
                    CREATE TABLE model (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    name TEXT NOT NULL,
                    maker INT,
                    typeofhardware INT,
                    FOREIGN KEY (maker) REFERENCES maker(id),
                    FOREIGN KEY (typeofhardware) REFERENCES typeofhardware(id)
                    )
                    """.trimIndent()
                )
            }
        }
    }
}

object Models : BaseTable<Model>("model") {
    val id = int("id").primaryKey()
    val name = text("name")
    val maker = int("maker")
    val typeofhardware = int("typeofhardware")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = Model(
        id = row[id] ?: 0,
        name = row[name].orEmpty(),
        maker = row[maker] ?: 0,
        typeofhardware = row[typeofhardware] ?: 0
    )
}

class ModelTable() {
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

    private var maker: IntegerProperty? = null

    fun setMaker(value: Int) {
        makerProperty().set(value)
    }

    fun getMaker(): Int {
        return makerProperty().get()
    }

    fun makerProperty(): IntegerProperty {
        if (maker == null) maker = SimpleIntegerProperty(this, "0")
        return maker as IntegerProperty
    }

    private var typeofhardware: IntegerProperty? = null

    fun setTypeofhardware(value: Int) {
        typeofhardwareProperty().set(value)
    }

    fun getTypeofhardware(): Int {
        return typeofhardwareProperty().get()
    }

    fun typeofhardwareProperty(): IntegerProperty {
        if (typeofhardware == null) typeofhardware = SimpleIntegerProperty(this, "0")
        return typeofhardware as IntegerProperty
    }

    constructor(id: Int?, name: String?, maker: Int?, typeofhardware: Int?) : this() {
        if (id != null) {
            this.setId(id)
        }
        if (name != null) {
            this.setName(name)
        }
        if (maker != null) {
            this.setMaker(maker)
        }
        if (typeofhardware != null) {
            this.setTypeofhardware(typeofhardware)
        }
    }
}