package com.lunivore.montecarluni.app

import com.lunivore.montecarluni.Events
import com.lunivore.montecarluni.model.ClipboardRequest
import com.lunivore.montecarluni.model.Forecast
import com.lunivore.montecarluni.model.ForecastRequest
import com.lunivore.montecarluni.model.WeeklyDistribution
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.scene.control.TableView
import javafx.util.StringConverter
import org.apache.logging.log4j.LogManager
import tornadofx.*
import java.time.LocalDate

class MontecarluniView : View(){

    val filename = SimpleStringProperty()
    val numStoriesToForecast = SimpleObjectProperty<Int>()
    val forecastStartDate = SimpleObjectProperty<LocalDate>()
    val useSelection = SimpleBooleanProperty()

    val distribution = FXCollections.observableArrayList<StoriesClosedInWeekPresenter>()
    val forecast = FXCollections.observableArrayList<DataPointPresenter>()

    val events : Events by di()
    val errorHandler = ErrorHandler()
    val logger = LogManager.getLogger()

    var distributionOutput by singleAssign<TableView<StoriesClosedInWeekPresenter>>()


    override val root = hbox {
        title="Montecarluni"

        borderpane {
            addClass(Styles.borderBox)
            top = hbox {
                addClass(Styles.buttonBox)
                label("Filename for import:")
                textfield(filename){ id = "filenameInput"}
                button("Import"){id = "importButton"}.setOnAction { import() }
            }
            center = hbox {
                distributionOutput = tableview<StoriesClosedInWeekPresenter>(distribution) {
                    column("Date range", StoriesClosedInWeekPresenter::rangeAsString) { prefWidth = 400.0 }
                    column("# stories closed", StoriesClosedInWeekPresenter::count) { prefWidth = 100.0 }
                    id="distributionOutput"
                    selectionModel.selectionMode= javafx.scene.control.SelectionMode.MULTIPLE
                    prefHeight=400.0
                }
            }
            bottom = vbox {
                hbox {
                    addClass(Styles.buttonBox)
                    label("Use / copy selection only")
                    checkbox() {id="useSelectionInput"}.bind(useSelection)
                }
                hbox {
                    addClass(Styles.buttonBox)
                    button("Copy distribution to clipboard") {id="clipboardButton"}
                            .setOnAction { clipboard() }
                    button("Clear") { id="clearButton"}.setOnAction { clearView() }
                }
            }
        }
        borderpane {
            addClass(Styles.borderBox)
            top = gridpane {
                addClass(Styles.buttonBox)
                label("Start date (optional)") {gridpaneConstraints {columnRowIndex(0, 0)}}
                datepicker(forecastStartDate) {
                    id="forecastStartDateInput"
                    gridpaneConstraints {columnRowIndex(1, 0)}
                }
                label("Number of stories to complete") {gridpaneConstraints { columnRowIndex(0, 1)}}
                textfield(numStoriesToForecast, IntObjectConverter()) {
                    id="numStoriesForecastInput"
                    gridpaneConstraints {columnRowIndex(1, 1)}
                }
                button("Run forecast"){
                    id="forecastButton"
                    gridpaneConstraints { columnRowIndex(2, 1) }
                }.setOnAction { forecast() }
            }
            center=hbox {
                tableview<DataPointPresenter>(forecast){
                    column("Probability", DataPointPresenter::probabilityAsPercentageString) { prefWidth = 400.0 }
                    column("Forecast Date", DataPointPresenter::dateAsString) { prefWidth = 100.0 }
                    id="forecastOutput"
                }
            }
        }
    }


    init {
        events.forecastNotification.subscribe(updateForecastOutput(),
                { errorHandler.handleError(it) })
        events.messageNotification.subscribe { errorHandler.handleNotification(it) }
        events.weeklyDistributionChangeNotification.subscribe(updateDistributionOutput(),
                { errorHandler.handleError(it) })


    }

    private fun forecast() {
        events.forecastRequest.push(
                ForecastRequest(
                        numStoriesToForecast.value,
                        forecastStartDate.value,
                        useSelection.value,
                        distributionOutput.selectionModel.selectedIndices))
    }

    private fun clearView() {
        filename.set("")
        useSelection.set(false)
        forecastStartDate.set(null)
        numStoriesToForecast.set(null)
        events.clearRequest.push(null)
    }

    private fun clipboard() {
        events.clipboardCopyRequest.push(
                ClipboardRequest(useSelection.value, distributionOutput.selectionModel.selectedIndices))
    }

    private fun import() {
        events.fileImportRequest.push(filename.value)
    }

    private fun  updateForecastOutput(): ((Forecast) -> Unit)? {
        return {
            forecast.clear()
            forecast.addAll(it.dataPoints.map { DataPointPresenter(it) })
        }
    }

    private fun updateDistributionOutput(): (WeeklyDistribution) -> Unit {
        return {
            distribution.clear()
            distribution.addAll(it.storiesClosed.map { StoriesClosedInWeekPresenter(it) })
        }
    }
}

class IntObjectConverter : StringConverter<Int>() {
    override fun fromString(string: String?): Int? {
        return string?.toInt()
    }

    override fun toString(input: Int?): String {
        return input?.toString() ?: ""
    }

}
