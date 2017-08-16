package com.lunivore.montecarluni.app

import com.lunivore.montecarluni.Events
import javafx.application.Application
import javafx.application.Platform
import javafx.beans.property.ReadOnlyListWrapper
import javafx.beans.property.StringProperty
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.input.Clipboard
import javafx.scene.input.ClipboardContent
import javafx.stage.Stage
import org.apache.logging.log4j.LogManager
import org.reactfx.EventStreams

class MontecarluniApp(var events: Events) : Application() {
    val logger = LogManager.getLogger()

    val errorHandler = ErrorHandler()

    override fun start(primaryStage: Stage) {
        val root = FXMLLoader.load<Parent>(javaClass.classLoader.getResource(
                "com/lunivore/montecarluni/app/montecarluni_app.fxml"))

        val importButton = (root.lookup("#importButton") as Button)
        val filenameInput = (root.lookup("#filenameInput") as TextField)
        EventStreams.eventsOf(importButton, ActionEvent.ACTION).subscribe({
            events.fileImportRequest.push(filenameInput.text)
        }, { errorHandler.handleError(it) })

        val clipboardButton = root.lookup("#clipboardButton") as Button
        EventStreams.eventsOf(clipboardButton, ActionEvent.ACTION).subscribe({
            events.clipboardCopyRequest.push(null)
        }, { errorHandler.handleError(it) })

        val distributionOutput = root.lookup("#distributionOutput") as TableView<Map<String, Int>>
        events.weeklyDistributionChangeNotification.subscribe({
            logger.debug("Weekly distribution change detected by app")
            distributionOutput.items =
                ReadOnlyListWrapper<Map<String, Int>>(FXCollections.observableList<Map<String, Int>>(
                        it.countsByWeek.map {mapOf<String, Int>(Pair("numberOfStories", it)) }))
        }, { errorHandler.handleError(it) })

        events.messageNotification.subscribe { errorHandler.handleNotification(it) }

        primaryStage.scene = Scene(root)
        primaryStage.show()
    }

    private fun requestClipboard(text: StringProperty) {
        val content = ClipboardContent()
        content.putString(text.get())
        Platform.runLater{Clipboard.getSystemClipboard().setContent(content)}
    }
}

