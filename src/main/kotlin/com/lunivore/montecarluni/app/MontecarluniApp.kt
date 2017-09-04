package com.lunivore.montecarluni.app

import com.lunivore.montecarluni.Events
import com.lunivore.montecarluni.model.Forecast
import com.lunivore.montecarluni.model.ForecastRequest
import com.lunivore.montecarluni.model.WeeklyDistribution
import javafx.application.Application
import javafx.application.Platform
import javafx.beans.property.StringProperty
import javafx.beans.value.ChangeListener
import javafx.collections.FXCollections
import javafx.collections.ListChangeListener
import javafx.event.ActionEvent
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.input.Clipboard
import javafx.scene.input.ClipboardContent
import javafx.stage.Stage
import org.apache.logging.log4j.LogManager
import org.reactfx.EventStreams
import java.time.format.DateTimeFormatter

class MontecarluniApp(var events: Events) : Application() {
    val logger = LogManager.getLogger()
    val errorHandler = ErrorHandler()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override fun start(primaryStage: Stage) {
        val root = FXMLLoader.load<Parent>(javaClass.classLoader.getResource(
                "com/lunivore/montecarluni/app/montecarluni_app.fxml"))

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
        val numOfStories = root.lookup("#numStoriesForecastInput") as TextField
        val startDate = root.lookup("#forecastStartDateInput") as DatePicker
        val forecastButton = root.lookup("#forecastButton") as Button
        EventStreams.eventsOf(forecastButton, ActionEvent.ACTION).subscribe({
            events.forecastRequest.push(
                    ForecastRequest(numOfStories.textProperty().value.toInt(), startDate.value))
        })

        val forecastOutput = root.lookup("#forecastOutput") as TableView<DataPointPresenter>
        events.forecastNotification.subscribe(updateForecastOutput(forecastOutput),
                { errorHandler.handleError(it) })
    }

    private fun initWeeklyDistribution(root: Parent) {
        val distributionOutput = root.lookup("#distributionOutput") as TableView<StoriesClosedInWeekPresenter>
        distributionOutput.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        events.weeklyDistributionChangeNotification.subscribe(updateDistributionOutput(distributionOutput),
                { errorHandler.handleError(it) })

        val useSelectionInput = root.lookup("#useSelectionInput") as CheckBox
        useSelectionInput.selectedProperty().addListener(ChangeListener {s, t, u ->
            pushSelectionRequest(useSelectionInput.isSelected, distributionOutput.selectionModel.selectedIndices)
        })
        distributionOutput.selectionModel.selectedCells.addListener(ListChangeListener {
            pushSelectionRequest(useSelectionInput.isSelected, distributionOutput.selectionModel.selectedIndices)
        })
    }

    private fun pushSelectionRequest(useSelectionInput: Boolean, selectedIndices: List<Int>) {
        if (useSelectionInput) {
            events.weeklyDistributionSelectionRequest.push(selectedIndices)
        } else {
            events.weeklyDistributionSelectionRequest.push(null)
        }
    }

    private fun initClipboardButton(root: Parent) {
        val clipboardButton = root.lookup("#clipboardButton") as Button
        EventStreams.eventsOf(clipboardButton, ActionEvent.ACTION).subscribe({
            events.clipboardCopyRequest.push(null)
        }, { errorHandler.handleError(it) })
    }

    private fun initFileImporting(root: Parent) {
        val importButton = (root.lookup("#importButton") as Button)
        val filenameInput = (root.lookup("#filenameInput") as TextField)
        EventStreams.eventsOf(importButton, ActionEvent.ACTION).subscribe({
            events.fileImportRequest.push(filenameInput.text)
        }, { errorHandler.handleError(it) })
    }

    private fun  updateForecastOutput(forecastOutput: TableView<DataPointPresenter>): ((Forecast) -> Unit)? {
        return {
            logger.debug("Forecast detected by app")
            forecastOutput.items = FXCollections.observableList(it.dataPoints.map { DataPointPresenter(it) })
        }
    }

    private fun updateDistributionOutput(distributionOutput: TableView<StoriesClosedInWeekPresenter>): (WeeklyDistribution) -> Unit {
        return {
            logger.debug("Weekly distribution change detected by app")
            distributionOutput.items = FXCollections.observableList(it.storiesClosed.map { StoriesClosedInWeekPresenter(it) })
        }
    }

    private fun requestClipboard(text: StringProperty) {
        val content = ClipboardContent()
        content.putString(text.get())
        Platform.runLater{Clipboard.getSystemClipboard().setContent(content)}
    }
}

