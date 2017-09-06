package com.lunivore.montecarluni.app

import com.lunivore.montecarluni.Events
import com.lunivore.montecarluni.model.ClipboardRequest
import com.lunivore.montecarluni.model.Forecast
import com.lunivore.montecarluni.model.ForecastRequest
import com.lunivore.montecarluni.model.WeeklyDistribution
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.scene.control.*
import javafx.scene.layout.GridPane
import org.apache.logging.log4j.LogManager
import org.reactfx.EventStreams
import tornadofx.View

class MontecarluniView : View(){
    override val root: GridPane by fxml()
    val importButton: Button by fxid()
    val filenameInput : TextField by fxid()
    val numStoriesForecastInput : TextField by fxid()
    val forecastStartDateInput: DatePicker by fxid()
    val useSelectionInput : CheckBox by fxid()
    val distributionOutput : TableView<StoriesClosedInWeekPresenter> by fxid()
    val forecastButton : Button by fxid()
    val forecastOutput : TableView<DataPointPresenter> by fxid()
    val clipboardButton : Button by fxid()
    val clearButton : Button by fxid()


    val events : Events by di()
    val errorHandler = ErrorHandler()
    val logger = LogManager.getLogger()

    init {
        initFileImporting()
        initClipboardButton()
        initWeeklyDistribution()
        initForecasting()
        initClear()

        events.messageNotification.subscribe { errorHandler.handleNotification(it) }
    }

    private fun initClear() {
        EventStreams.eventsOf(clearButton, ActionEvent.ACTION).subscribe({
            filenameInput.textProperty().set("")
            useSelectionInput.selectedProperty().setValue(false)
            forecastStartDateInput.valueProperty().set(null)
            numStoriesForecastInput.textProperty().set("")
            events.clearRequest.push(null)
        })
    }

    private fun initForecasting() {
        EventStreams.eventsOf(forecastButton, ActionEvent.ACTION).subscribe({
            logger.debug("Requesting forecast; selection = ${useSelectionInput.isSelected}")
            events.forecastRequest.push(
                    ForecastRequest(numStoriesForecastInput.textProperty().value.toInt(), forecastStartDateInput.value,
                            useSelectionInput.isSelected, distributionOutput.selectionModel.selectedIndices))
        })
        events.forecastNotification.subscribe(updateForecastOutput(forecastOutput),
                { errorHandler.handleError(it) })
    }

    private fun initWeeklyDistribution() {
        distributionOutput.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        events.weeklyDistributionChangeNotification.subscribe(updateDistributionOutput(),
                { errorHandler.handleError(it) })
    }

    private fun initClipboardButton() {
        EventStreams.eventsOf(clipboardButton, ActionEvent.ACTION).subscribe({
            events.clipboardCopyRequest.push(
                    ClipboardRequest(useSelectionInput.isSelected, distributionOutput.selectionModel.selectedIndices))
        }, { errorHandler.handleError(it) })
    }

    private fun initFileImporting() {
        EventStreams.eventsOf(importButton, ActionEvent.ACTION).subscribe({
            events.fileImportRequest.push(filenameInput.text)
        }, { errorHandler.handleError(it) })
    }

    private fun  updateForecastOutput(forecastOutput: TableView<DataPointPresenter>): ((Forecast) -> Unit)? {
        return {
            logger.debug("Forecast detected by app")
            forecastOutput.items = FXCollections.observableList(
                    it.dataPoints.map { DataPointPresenter(it) })
        }
    }

    private fun updateDistributionOutput(): (WeeklyDistribution) -> Unit {
        return {
            logger.debug("Weekly distribution change detected by app")
            distributionOutput.items = FXCollections.observableList(
                    it.storiesClosed.map { StoriesClosedInWeekPresenter(it) })
        }
    }
}
