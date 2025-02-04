import javafx.beans.Observable
import javafx.beans.property.IntegerProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.control.Button
import org.controlsfx.control.tableview2.TableColumn2
import org.controlsfx.control.tableview2.TableView2
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class SlackerController {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @FXML
    private var maker: TableView2<MakerTable>? = null
    private var data: ObservableList<MakerTable>? = null

    @FXML
    private lateinit var butt: Button

    @FXML
    private fun onButtonClick() {
        val columnId: TableColumn2<MakerTable, String> = TableColumn2("id")
        columnId.prefWidth = 100.0
        columnId.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.getId().toString()) }

        val columnName: TableColumn2<MakerTable, String> = TableColumn2("name")
        columnName.prefWidth = 100.0
        columnName.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.getName()) }

        maker?.columns?.add(columnId)
        maker?.columns?.add(columnName)
        data = generateData(1000)
        maker?.items = data
    }

    private fun generateData(value: Int): ObservableList<MakerTable> {
        val persons: ObservableList<MakerTable> =
            FXCollections.observableArrayList { e: MakerTable ->
                arrayOf<Observable>(
                    e.idProperty(),
                    e.nameProperty()
                )
            }

        for (i in 0..<value) {
            persons.add(
                    MakerTable(
                        i,
                        (i * 145).toString()
                    )
            )
        }

        return persons
    }
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

    constructor(id: Int, name: String) : this() {
        this.setId(id)
        this.setName(name)
    }
}