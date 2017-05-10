package com.lunivore.montecarluni.app

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.lunivore.montecarluni.engine.MontecarluniController
import javafx.application.Application
import javafx.application.Platform
import javafx.beans.property.StringProperty
import javafx.event.ActionEvent
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.input.Clipboard
import javafx.scene.input.ClipboardContent
import javafx.stage.Stage


class MontecarluniApp(var kodein: Kodein) : Application() {

    val lineSeparatorForExcel = "\n"

    override fun start(primaryStage: Stage) {
        val controller : MontecarluniController = kodein.instance()

        val root = FXMLLoader.load<Parent>(javaClass.classLoader.getResource(
                "com/lunivore/montecarluni/app/montecarluni_app.fxml"))

        val importButton = (root.lookup("#importButton") as Button)
        importButton.addEventHandler(
            ActionEvent.ACTION, { controller.requestImport() })

        val filenameInput = (root.lookup("#filenameInput") as TextField)
        filenameInput.textProperty().addListener(
                { s, t, u -> controller.filenameChanged(filenameInput.text)})

        val distributionOutput = root.lookup("#distributionOutput") as Label
        val clipboardButton = root.lookup("#clipboardButton") as Button
        clipboardButton.addEventHandler(
                ActionEvent.ACTION, { requestClipboard(distributionOutput.textProperty()) })

        controller.setDistributionHandler({ ints ->
            val formatted = ints.joinToString(lineSeparatorForExcel)
            Platform.runLater {distributionOutput.textProperty().set(formatted)}
        })

        primaryStage.scene = Scene(root)
        primaryStage.show()
    }

    private fun requestClipboard(text: StringProperty) {
        val content = ClipboardContent()
        content.putString(text.get())
        Platform.runLater{Clipboard.getSystemClipboard().setContent(content)}
    }
}

