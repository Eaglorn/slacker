package ru.fku.slacker.db

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
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import ru.fku.slacker.Data
import ru.fku.slacker.utils.Identifiable
import ru.fku.slacker.utils.SqliteDatabase

@Component("DB.Class.Model")
data class Model(val id : Int?, val name : String?, val maker_id : Int?, val type_of_hardware_id : Int?) {
    @Suppress("unused")
    private val logger : Logger = LoggerFactory.getLogger(this.javaClass)

    companion object {
        fun getRows(row : QueryRowSet) : Model {
            return Model(
                row[Models.id],
                row[Models.name],
                row[Models.maker_id],
                row[Models.type_of_hardware_id]
            )
        }
    }

    @Suppress("unused")
    @Bean(name = ["DB.Create.Model"])
    fun createDatabase() : Boolean {
        if (Data.config.pathDB.isNotEmpty()) {
            val database = SqliteDatabase.connect(Data.config.pathDB)
            database.useConnection { conn ->
                val tableExists = conn.createStatement()
                    .executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='model'").next()
                if (! tableExists) {
                    val sql = """
                            CREATE TABLE model (
                                id INTEGER PRIMARY KEY AUTOINCREMENT,
                                name TEXT NOT NULL,
                                maker_id INT,
                                type_of_hardware_id INT,
                                FOREIGN KEY (maker_id) REFERENCES maker(id),
                                FOREIGN KEY (type_of_hardware_id) REFERENCES type_of_hardware(id)
                            );
                        """
                    conn.createStatement().executeUpdate(sql.trimIndent())
                }
            }
        }
        return false
    }
}

object Models : BaseTable<Model>("model") {
    val id = int("id").primaryKey()
    val name = text("name")
    val maker_id = int("maker_id")
    val type_of_hardware_id = int("type_of_hardware_id")

    override fun doCreateEntity(row : QueryRowSet, withReferences : Boolean) = Model(
        id = row[id] ?: 0,
        name = row[name].orEmpty(),
        maker_id = row[maker_id] ?: 0,
        type_of_hardware_id = row[type_of_hardware_id] ?: 0
    )
}

class ModelTable(id : Int?, name : String?, maker : String?, type_of_hardware : String?) : Identifiable {
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

    private var maker : StringProperty = SimpleStringProperty(this, "maker", "")

    private fun setMaker(value : String) {
        maker.set(value)
    }

    fun getMaker() : String {
        return maker.get()
    }

    private var type_of_hardware : StringProperty = SimpleStringProperty(this, "type_of_hardware", "")

    private fun setTypeOfHardware(value : String) {
        type_of_hardware.set(value)
    }

    fun getTypeOfHardware() : String {
        return type_of_hardware.get()
    }

    init {
        id?.let {
            this.setId(it)
        }
        name?.let {
            this.setName(it)
        }
        maker?.let {
            this.setMaker(it)
        }
        type_of_hardware?.let {
            this.setTypeOfHardware(it)
        }
    }
}