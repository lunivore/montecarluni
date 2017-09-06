package com.lunivore.montecarluni.app

import com.lunivore.montecarluni.Events
import com.lunivore.montecarluni.model.ClipboardRequest
import com.lunivore.montecarluni.model.Forecast
import com.lunivore.montecarluni.model.ForecastRequest
import com.lunivore.montecarluni.model.WeeklyDistribution
import javafx.application.Application
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.stage.Stage
import org.apache.logging.log4j.LogManager
import org.reactfx.EventStreams
import java.time.format.DateTimeFormatter

class MontecarluniApp(var events: Events) : Application() {
    val logger = LogManager.getLogger()
    val errorHandler = ErrorHandler()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    private lateinit var filenameInput: TextField
    private lateinit var importButton: Button
    private lateinit var numOfStories: TextField
    private lateinit var  startDate: DatePicker
    private lateinit var  useSelection: CheckBox
    private lateinit var  distributionOutput: TableView<StoriesClosedInWeekPresenter>
    private lateinit var clipboardButton: Button
    private lateinit var  forecastButton: Button
    private lateinit var forecastOutput: TableView<DataPointPresenter>

    override fun start(primaryStage: Stage) {
        val root = FXMLLoader.load<Parent>(javaClass.classLoader.getResource(
                "com/lunivore/montecarluni/app/MontecarluniView.fxml"))

        importButton = (root.lookup("#importButton") as Button)
        filenameInput = (root.lookup("#filenameInput") as TextField)
        numOfStories = root.lookup("#numStoriesForecastInput") as TextField
        startDate = root.lookup("#forecastStartDateInput") as DatePicker
        useSelection = root.lookup("#useSelectionInput") as CheckBox
        distributionOutput = root.lookup("#distributionOutput") as TableView<StoriesClosedInWeekPresenter>
        forecastButton = root.lookup("#forecastButton") as Button
        forecastOutput = root.lookup("#forecastOutput") as TableView<DataPointPresenter>
        clipboardButton = root.lookup("#clipboardButton") as Button

        initFileImporting(root)
        initClipboardButton(root)
        initWeeklyDistribution(root)
        initForecasting(root)

        events.messageNotification.subscribe { errorHandler.handleNotification(it) }

        primaryStage.scene = Scene(root)
        primaryStage.title = "Montecarluni"
        primaryStage.show()
    }

    private fun initForecasting(root: Parent) {
        EventStreams.eventsOf(forecastButton, ActionEvent.ACTION).subscribe({
            events.forecastRequest.push(
                    ForecastRequest(numOfStories.textProperty().value.toInt(), startDate.value,
                            useSelection.isSelected, distributionOutput.selectionModel.selectedIndices))
        })
        events.forecastNotification.subscribe(updateForecastOutput(forecastOutput),
                { errorHandler.handleError(it) })
    }

    private fun initWeeklyDistribution(root: Parent) {
        distributionOutput.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        events.weeklyDistributionChangeNotification.subscribe(updateDistributionOutput(),
                { errorHandler.handleError(it) })
    }

    private fun initClipboardButton(root: Parent) {
        EventStreams.eventsOf(clipboardButton, ActionEvent.ACTION).subscribe({
            events.clipboardCopyRequest.push(
                    ClipboardRequest(useSelection.isSelected, distributionOutput.selectionModel.selectedIndices))
        }, { errorHandler.handleError(it) })
    }

    private fun initFileImporting(root: Parent) {
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

