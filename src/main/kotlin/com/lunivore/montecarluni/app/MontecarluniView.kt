package com.lunivore.montecarluni.app

import com.lunivore.montecarluni.Events
import com.lunivore.montecarluni.model.ClipboardRequest
import com.lunivore.montecarluni.model.Distributions
import com.lunivore.montecarluni.model.Forecast
import com.lunivore.montecarluni.model.ForecastRequest
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.scene.control.TabPane
import javafx.scene.control.TableView
import org.apache.logging.log4j.LogManager
import tornadofx.*
import java.time.LocalDate

class MontecarluniView : View(){

    private val filename = SimpleStringProperty()
    private val numWorkItemsToForecast = SimpleObjectProperty<Int>()
    private val forecastStartDate = SimpleObjectProperty<LocalDate>()
    private val useSelection = SimpleBooleanProperty()

    private val weeklyDistribution = FXCollections.observableArrayList<WorkItemsDoneInWeekPresenter>()
    private val cycleTimes = FXCollections.observableArrayList<CycleTimePresenter>()
    private val forecast = FXCollections.observableArrayList<DataPointPresenter>()


    private val events : Events by di()
    private val errorHandler = ErrorHandler()
    private val logger = LogManager.getLogger()

    private var weeklyDistributionOutput by singleAssign<TableView<WorkItemsDoneInWeekPresenter>>()
    private var cycleTimesOutput by singleAssign<TableView<CycleTimePresenter>>()

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
            center = tabpane {
                id="distributionTabs"
                tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
                tab {
                    id="weeklyDistributionTab"
                    text = "Weekly distribution"
                    weeklyDistributionOutput = tableview<WorkItemsDoneInWeekPresenter>(weeklyDistribution) {
                        column("Date range", WorkItemsDoneInWeekPresenter::rangeAsString) { prefWidth = 400.0 }
                        column("# work items closed", WorkItemsDoneInWeekPresenter::count) { prefWidth = 100.0 }
                        id="weeklyDistributionOutput"
                        selectionModel.selectionMode= javafx.scene.control.SelectionMode.MULTIPLE
                        prefHeight=400.0
                    }
                }
                tab {
                    id="cycleTimesTab"
                    text = "Cycle times"
                    cycleTimesOutput = tableview<CycleTimePresenter>(cycleTimes) {
                        id="cycleTimesOutput"
                        column("Id", CycleTimePresenter::id) { prefWidth = 100.0 }
                        column("Date", CycleTimePresenter::date) { prefWidth = 200.0 }
                        column("Gap", CycleTimePresenter::gap) { prefWidth = 200.0 }
                    }
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
                    button("Copy weeklyDistribution to clipboard") {id="clipboardButton"}
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
                label("Number of work items to complete") {gridpaneConstraints { columnRowIndex(0, 1)}}
                textfield(numWorkItemsToForecast, IntObjectConverter()) {
                    id="numWorkItemsForecastInput"
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
        events.forecastNotification.subscribe({updateForecastOutput(it)},
                { errorHandler.handleError(it) })
        events.messageNotification.subscribe { errorHandler.handleNotification(it) }
        events.distributionChangeNotification.subscribe({updateDistributionOutput(it)},
                { errorHandler.handleError(it) })


    }

    private fun forecast() {
        events.forecastRequest.push(
                ForecastRequest(
                        numWorkItemsToForecast.value,
                        forecastStartDate.value,
                        useSelection.value,
                        weeklyDistributionOutput.selectionModel.selectedIndices))
    }

    private fun clearView() {
        filename.set("")
        useSelection.set(false)
        forecastStartDate.set(null)
        numWorkItemsToForecast.set(null)
        events.clearRequest.push(null)
    }

    private fun clipboard() {
        events.clipboardCopyRequest.push(
                ClipboardRequest(useSelection.value, weeklyDistributionOutput.selectionModel.selectedIndices))
    }

    private fun import() {
        events.fileImportRequest.push(filename.value)
    }

    private fun  updateForecastOutput(it: Forecast){
        forecast.clear()
        forecast.addAll(it.dataPoints.map { DataPointPresenter(it) })
    }

    private fun updateDistributionOutput(it: Distributions){
        weeklyDistribution.clear()
        weeklyDistribution.addAll(it.workItemsDone.map { WorkItemsDoneInWeekPresenter(it) })

        cycleTimes.clear()
        cycleTimes.addAll(it.cycleTimes.map { CycleTimePresenter(it)})
    }

}

