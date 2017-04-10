package com.lunivore.montecarluni

import javafx.application.Application
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

class Montecarluni : Application() {

    val ln = System.getProperty("line.separator")

    override fun start(primaryStage: Stage) {
        val engine = DistributionCalculator()
        val filenameInput = createFilenameImput(engine)
        val importButton = createImportButton(engine)
        val distributionOutput = createDistribtionOutput(engine)
        val clipboardButton = createClipboardButton(distributionOutput.textProperty())

        val gridPane = GridPane()
        gridPane.add(filenameInput, 0, 0)
        gridPane.add(importButton, 0, 1)
        gridPane.add(distributionOutput, 0, 2)
        gridPane.add(clipboardButton, 0, 3)

        engine.setDistributionHandler({ ints ->
            val formatted = ints.fold("", {s, i -> s + ln + i})
            distributionOutput.textProperty().set(formatted)
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

    private fun  createDistribtionOutput(engine: DistributionCalculator): Label {
        val distributionOutput = Label()
        distributionOutput.id = "distributionOutput"
        distributionOutput.minWidth = 200.0
        distributionOutput.minHeight = 200.0
        return distributionOutput
    }

    private fun createFilenameImput(engine: DistributionCalculator): TextField {
        val textField = TextField()
        textField.id = "filenameInput"
        textField.textProperty().addListener(ChangeListener {s, t, u -> engine.filenameChanged(textField.text)})
        return textField
    }


    private fun  createImportButton(engine: DistributionCalculator): Button {
        val button = Button("Import")
        button.id = "importButton"
        button.addEventHandler(ActionEvent.ACTION, { engine.requestImport() })
        return button
    }


    companion object {
        @JvmStatic fun main(args: Array<String>) {
            launch(Montecarluni::class.java, *args)
        }
    }

}