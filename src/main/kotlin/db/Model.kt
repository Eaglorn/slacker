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

data class Model(val id: Int?, val name: String?, val maker_id: Int?, val type_of_hardware_id: Int?) {
    companion object {
        fun createDatabase(conn: Connection) {
            val tableExists = conn.createStatement()
                .executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='model'").next()

            if (!tableExists) {
                conn.createStatement().executeUpdate(
                    """
                    CREATE TABLE model (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    maker_id INT,
                    type_of_hardware_id INT,
                    FOREIGN KEY (maker_id) REFERENCES maker(id),
                    FOREIGN KEY (type_of_hardware_id) REFERENCES type_of_hardware(id)
                    )
                    """.trimIndent()
                )
            }
        }
    }
}

object Models : BaseTable<Model>("db/model") {
    val id = int("id").primaryKey()
    val name = text("name")
    val maker_id = int("maker_id")
    val type_of_hardware_id = int("type_of_hardware_id")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean) = Model(
        id = row[id] ?: 0,
        name = row[name].orEmpty(),
        maker_id = row[maker_id] ?: 0,
        type_of_hardware_id = row[type_of_hardware_id] ?: 0
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

    private var maker: StringProperty? = null

    fun setMaker(value: String) {
        makerProperty().set(value)
    }

    fun getMaker(): String {
        return makerProperty().get()
    }

    fun makerProperty(): StringProperty {
        if (maker == null) maker = SimpleStringProperty(this, "")
        return maker as StringProperty
    }

    private var type_of_hardware: StringProperty? = null

    fun setTypeOfHardware(value: String) {
        typeOfHardwareProperty().set(value)
    }

    fun getTypeOfHardware(): String {
        return typeOfHardwareProperty().get()
    }

    fun typeOfHardwareProperty(): StringProperty {
        if (type_of_hardware == null) type_of_hardware = SimpleStringProperty(this, "")
        return type_of_hardware as StringProperty
    }

    constructor(id: Int?, name: String?, maker: String?, type_of_hardware: String?) : this() {
        if (id != null) {
            this.setId(id)
        }
        if (name != null) {
            this.setName(name)
        }
        if (maker != null) {
            this.setMaker(maker)
        }
        if (type_of_hardware != null) {
            this.setTypeOfHardware(type_of_hardware)
        }
    }
}