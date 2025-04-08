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

@Component("DB.Class.Manager")
data class Manager(val id : Int?, val name : String?, val post : String?) {
    @Suppress("unused")
    private val logger : Logger = LoggerFactory.getLogger(this.javaClass)

    companion object {
        fun getRows(row : QueryRowSet) : Manager {
            return Manager(row[Managers.id], row[Managers.name], row[Managers.post])
        }
    }

    @Suppress("unused")
    @Bean(name = ["DB.Create.Manager"])
    fun createDatabase() : Boolean {
        if (Data.config.pathDB.isNotEmpty()) {
            val database = SqliteDatabase.connect(Data.config.pathDB)
            database.useConnection { conn ->
                val tableExists = conn.createStatement()
                    .executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='manager'").next()
                if (! tableExists) {
                    val sql = """
                            CREATE TABLE manager (
                                id INTEGER PRIMARY KEY AUTOINCREMENT,
                                name TEXT NOT NULL,
                                post TEXT NOT NULL
                            );
                        """
                    conn.createStatement().executeUpdate(sql.trimIndent())
                }
            }
        }
        return false
    }
}

object Managers : BaseTable<Manager>("manager") {
    val id = int("id").primaryKey()
    val name = text("name")
    val post = text("post")

    override fun doCreateEntity(row : QueryRowSet, withReferences : Boolean) = Manager(
        id = row[id] ?: 0,
        name = row[name].orEmpty(),
        post = row[post].orEmpty(),
    )
}

class ManagerTable(id : Int?, name : String?, post : String?) : Identifiable {
    @Suppress("unused")
    private val logger : Logger = LoggerFactory.getLogger(this.javaClass)

    private var id : IntegerProperty = SimpleIntegerProperty(this, "id", 0)

    fun setId(value : Int) {
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

    private var post : StringProperty = SimpleStringProperty(this, "post", "")

    private fun setPost(value : String) {
        post.set(value)
    }

    fun getPost() : String {
        return post.get()
    }

    init {
        id?.let {
            this.setId(it)
        }
        name?.let {
            this.setName(it)
        }
        post?.let {
            this.setPost(it)
        }
    }
}