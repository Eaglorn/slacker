import com.deepoove.poi.XWPFTemplate
import javafx.beans.Observable
import javafx.beans.property.IntegerProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.event.Event
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.stage.FileChooser
import javafx.stage.Window
import org.controlsfx.control.tableview2.TableColumn2
import org.controlsfx.control.tableview2.TableView2
import org.ktorm.dsl.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class SlackerController() {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @FXML
    private lateinit var buttonLoadDatabase: Button
    @FXML
    private lateinit var fieldLoadDatabase: TextField

    @FXML
    private lateinit var maker: TableView2<MakerTable>
    @FXML
    private lateinit var makerColumnId: TableColumn2<MakerTable, String>
    @FXML
    private lateinit var makerColumnName: TableColumn2<MakerTable, String>

    private var data: ObservableList<MakerTable>? = null

    @FXML
    private fun onButtonClickLoadSetting(e: Event) {
        openFileChooser(buttonLoadDatabase.scene.window)
    }

    private fun openFileChooser(stage: Window) {
        val fileChooser = FileChooser()
        fileChooser.title = "Выберите файл"
        fileChooser.extensionFilters.addAll(
            //FileChooser.ExtensionFilter("DB", "*.db"),
            FileChooser.ExtensionFilter("DOCX", "*.docx"),
        )

        val selectedFile = fileChooser.showOpenDialog(stage)
        if (selectedFile != null) {
            fieldLoadDatabase.text = selectedFile.absolutePath
            XWPFTemplate.compile(selectedFile.absolutePath).render(object : HashMap<String?, Any?>() {
                init {
                    put("id", "Идентификатор")
                    put("name", "Имя")
                }
            }).writeToFile("out_template.docx")
        }
    }

    @FXML
    private fun onButtonClick() {
        makerColumnId.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.getId().toString()) }
        makerColumnName.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.getName()) }
        data = generateData(1000)
        maker.items = data
        val database = SqliteDatabase().connect()
        database.useConnection { conn ->
            val tableExists = conn.createStatement().executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='maker'").next()

            if (!tableExists) {
                conn.createStatement().executeUpdate(
                    """
                CREATE TABLE maker (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL
                )
                """.trimIndent()
                )
                println("Table 'maker' create.")
            } else {
                println("Table 'maker' have.")
            }
        }

        database.insert(Makers) {
            set(Makers.name, "HP")
        }
        database.insert(Makers) {
            set(Makers.name, "Xerox")
        }
        database.insert(Makers) {
            set(Makers.name, "Phantum")
        }

        val query = database.from(Makers).select()

        query
            .where { Makers.name eq "HP" }
            .map { row -> Maker(row[Makers.id], row[Makers.name]) }
            .forEach { println(it) }
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