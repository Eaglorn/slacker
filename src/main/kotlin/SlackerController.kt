import db.Maker
import db.MakerTable
import db.Makers
import javafx.beans.property.SimpleStringProperty
import javafx.event.Event
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Tab
import javafx.scene.control.TextField
import javafx.stage.DirectoryChooser
import javafx.stage.FileChooser
import org.controlsfx.control.tableview2.TableColumn2
import org.controlsfx.control.tableview2.TableView2
import org.ktorm.dsl.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class SlackerController() {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @FXML lateinit var tabWriteOff: Tab
    @FXML lateinit var tabExpertise: Tab
    @FXML lateinit var tabDataBase: Tab

    @FXML lateinit var fieldLoadDatabase: TextField
    @FXML lateinit var buttonLoadDatabase: Button

    @FXML lateinit var fieldLoadTemplates: TextField
    @FXML lateinit var buttonLoadTemplates: Button

    @FXML lateinit var tableMaker: TableView2<MakerTable>
    @FXML lateinit var tableMakerColumnId: TableColumn2<MakerTable, String>
    @FXML lateinit var tableMakerColumnName: TableColumn2<MakerTable, String>

    init {
        Data.companion.controller = this
        Data.companion.config = Config.load()
    }

    @FXML
    private fun onButtonClickLoadDataBase(e: Event) {
        val fileChooser = FileChooser()
        fileChooser.title = "Файл базы данных"
        fileChooser.extensionFilters.addAll(
            FileChooser.ExtensionFilter("DataBase", "*.db"),
        )

        val selectedFile = fileChooser.showOpenDialog(buttonLoadDatabase.scene.window)
        if (selectedFile != null) {
            fieldLoadDatabase.text = selectedFile.absolutePath
        }
    }

    @FXML
    private fun onButtonClickLoadTemplates(e: Event) {
        val directoryChooser = DirectoryChooser()
        directoryChooser.title = "Каталог c шаблонами"

        val selectedDirectory = directoryChooser.showDialog(buttonLoadTemplates.scene.window)
        if (selectedDirectory != null) {
            fieldLoadTemplates.text = selectedDirectory.absolutePath
        }
    }

    @FXML
    private fun onButtonLoadApp() {
        tabWriteOff.disableProperty().set(false)
        tabExpertise.disableProperty().set(false)
        tabDataBase.disableProperty().set(false)
    }

    @FXML
    private fun onButtonClick() {
        tableMakerColumnId.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.getId().toString()) }
        tableMakerColumnName.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.getName()) }
        val database = SqliteDatabase().connect()
        database.useConnection { conn ->
            Maker.createDatabase(conn)
        }

        val query = database.from(Makers).select()

        query
            .map { row -> Maker(row[Makers.id], row[Makers.name]) }
            .forEach {
                tableMaker.items.add(MakerTable(it.id, it.name))
            }
        Config.load()
    }
}