package com.lunivore.montecarluni.glue

import cucumber.api.java8.En
import javafx.application.Platform
import javafx.scene.input.Clipboard
import javafx.scene.input.DataFormat
import org.junit.Assert
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.TimeUnit

class ClipboardSteps(val world : World) : En {

    init {
//        When("^I copy it to the clipboard$", {
//            Stirry.buttonClick { it.text == "Copy to Clipboard" }
//        })

        Then("^pasting it elsewhere should result in$", {expectedDistributionAsOneLine : String ->
            var text = ""
            var queue = ArrayBlockingQueue<Boolean>(1)
            Platform.runLater {
                text = Clipboard.getSystemClipboard().getContent(DataFormat.PLAIN_TEXT).toString()
                queue.put(true)
            }
            queue.poll(1L, TimeUnit.SECONDS)

            val expectedDistribution = expectedDistributionAsOneLine.split(',')
                    .map { it.trim()}
                    .joinToString(separator = "\n")
            Assert.assertEquals(expectedDistribution, text)
        });
    }
}

