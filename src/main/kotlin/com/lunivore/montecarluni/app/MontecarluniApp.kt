package com.lunivore.montecarluni.app

import com.lunivore.montecarluni.engine.MontecarluniController
import javafx.application.Application
import javafx.application.Platform
import javafx.beans.property.StringProperty
import javafx.beans.value.ChangeListener
import javafx.event.ActionEvent
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.input.Clipboard
import javafx.scene.input.ClipboardContent
import javafx.scene.layout.GridPane
import javafx.stage.Stage

class MontecarluniApp : Application() {

    val ln = System.getProperty("line.separator")

    override fun start(primaryStage: Stage) {
        val controller = MontecarluniController()
        val filenameInput = createFilenameImput(controller)
        val importButton = createImportButton(controller)
        val distributionOutput = createDistribtionOutput(controller)
        val clipboardButton = createClipboardButton(distributionOutput.textProperty())

        val gridPane = GridPane()
        gridPane.add(filenameInput, 0, 0)
        gridPane.add(importButton, 0, 1)
        gridPane.add(distributionOutput, 0, 2)
        gridPane.add(clipboardButton, 0, 3)

        controller.setDistributionHandler({ ints ->
            val formatted = ints.fold("", {s, i -> s + ln + i})
            Platform.runLater {distributionOutput.textProperty().set(formatted)}
        })

        primaryStage.scene = Scene(gridPane)
        primaryStage.show()
    }

    private fun createClipboardButton(text: StringProperty): Button {
        val button = Button("Copy to Clipboard")
        button.id = "clipboardButton"
        button.addEventHandler(ActionEvent.ACTION, { requestClipboard(text) })
        return button
    }

    private fun requestClipboard(text: StringProperty) {
        val content = ClipboardContent()
        content.putString(text.get())
        Clipboard.getSystemClipboard().setContent(content)
    }

    private fun  createDistribtionOutput(controller: MontecarluniController): Label {
        val distributionOutput = Label()
        distributionOutput.id = "distributionOutput"
        distributionOutput.minWidth = 200.0
        distributionOutput.minHeight = 200.0
        return distributionOutput
    }

    private fun createFilenameImput(controller: MontecarluniController): TextField {
        val textField = TextField()
        textField.id = "filenameInput"
        textField.textProperty().addListener(ChangeListener { s, t, u -> controller.filenameChanged(textField.text)})
        return textField
    }


    private fun  createImportButton(controller: MontecarluniController): Button {
        val button = Button("Import")
        button.id = "importButton"
        button.addEventHandler(ActionEvent.ACTION, { controller.requestImport() })
        return button
    }
}

