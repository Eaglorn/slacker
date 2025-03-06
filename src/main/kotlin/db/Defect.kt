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

class Defect(
    val id : Int?,
    val hardware : String?,
    val result_view : String?,
    val detect : String?,
    val reason : String?
) {
    @Suppress("unused")
    private val logger : Logger = LoggerFactory.getLogger(this.javaClass)
}

object Defects : BaseTable<Defect>("defect") {
    val id = int("id").primaryKey()
    val hardware = text("hardware")
    val result_view = text("result_view")
    val detect = text("detect")
    val reason = text("reason")

    override fun doCreateEntity(row : QueryRowSet, withReferences : Boolean) = Defect(
        id = row[id] ?: 0,
        hardware = row[hardware].orEmpty(),
        result_view = row[result_view].orEmpty(),
        detect = row[detect].orEmpty(),
        reason = row[reason].orEmpty()
    )
}

class DefectTable(id : Int?, hardware : String?, result_view : String?, detect : String?, reason : String?) :
    Identifiable {

    @Suppress("unused")
    private val logger : Logger = LoggerFactory.getLogger(this.javaClass)

    private var id : IntegerProperty = SimpleIntegerProperty(this, "id", 0)

    private fun setId(value : Int) {
        id.set(value)
    }

    override fun getId() : Int {
        return id.get()
    }

    private var hardware : StringProperty = SimpleStringProperty(this, "hardware", "")

    private fun setHardware(value : String) {
        hardware.set(value)
    }

    fun getHardware() : String {
        return hardware.get()
    }

    private var result_view : StringProperty = SimpleStringProperty(this, "result_view", "")

    private fun setResultView(value : String) {
        result_view.set(value)
    }

    fun getResultView() : String {
        return result_view.get()
    }

    private var detect : StringProperty = SimpleStringProperty(this, "detect", "")

    private fun setDetect(value : String) {
        detect.set(value)
    }

    fun getDetect() : String {
        return detect.get()
    }

    private var reason : StringProperty = SimpleStringProperty(this, "reason", "")

    private fun setReason(value : String) {
        reason.set(value)
    }

    fun getReason() : String {
        return reason.get()
    }

    init {
        id?.let {
            this.setId(it)
        }
        hardware?.let {
            this.setHardware(it)
        }
        result_view?.let {
            this.setResultView(it)
        }
        detect?.let {
            this.setDetect(it)
        }
        reason?.let {
            this.setReason(it)
        }
    }
}