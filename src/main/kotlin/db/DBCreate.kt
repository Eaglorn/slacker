package db

import org.intellij.lang.annotations.Language
import utils.DBCreateAnnotation
import java.sql.Connection

class DBCreate {
    @Suppress("unused")
    @DBCreateAnnotation
    fun createDatabaseDefect(conn : Connection) {
        val tableExists = conn.createStatement()
            .executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='defect'").next()
        if (! tableExists) {
            @Language("SQL")
            val sql = """
                CREATE TABLE defect (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    hardware TEXT NOT NULL,
                    result_view TEXT NOT NULL,
                    detect TEXT NOT NULL,
                    reason TEXT NOT NULL
                );
            """
            conn.createStatement().executeUpdate(sql.trimIndent())
        }
    }

    @Suppress("unused")
    @DBCreateAnnotation
    fun createDatabaseMaker(conn : Connection) {
        val tableExists = conn.createStatement()
            .executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='maker'").next()
        if (! tableExists) {
            val sql = """
                CREATE TABLE maker (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL
                );
            """
            conn.createStatement().executeUpdate(sql.trimIndent())
        }
    }

    @Suppress("unused")
    @DBCreateAnnotation
    fun createDatabaseModel(conn : Connection) {
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

    @Suppress("unused")
    @DBCreateAnnotation
    fun createDatabaseTypeOfHardware(conn : Connection) {
        val tableExists = conn.createStatement()
            .executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='type_of_hardware'").next()
        if (! tableExists) {
            val sql = """
                CREATE TABLE type_of_hardware (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL
                );
            """
            conn.createStatement().executeUpdate(sql.trimIndent())
        }
    }

    @Suppress("unused")
    @DBCreateAnnotation
    fun createDatabaseUser(conn : Connection) {
        val tableExists = conn.createStatement()
            .executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='user'").next()
        if (! tableExists) {
            val sql = """
                CREATE TABLE user (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    post TEXT NOT NULL,
                    address TEXT NOT NULL
                );
            """
            conn.createStatement().executeUpdate(sql.trimIndent())
        }
    }
}