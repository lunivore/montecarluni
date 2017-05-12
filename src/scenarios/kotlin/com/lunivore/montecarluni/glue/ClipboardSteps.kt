package com.lunivore.montecarluni.glue

import com.lunivore.stirry.Stirry
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

        Then("^I should be able to paste it somewhere else$", {
            var text = ""
            var queue = ArrayBlockingQueue<Boolean>(1)
            Platform.runLater {
                text = Clipboard.getSystemClipboard().getContent(DataFormat.PLAIN_TEXT).toString()
                queue.put(true)
            }
            queue.poll(1L, TimeUnit.SECONDS)

            Assert.assertEquals(world.expectedDistribution, text)
        });
    }
}

