package com.lunivore.montecarluni.glue

import com.lunivore.stirry.Stirry
import com.lunivore.stirry.fireAndStir
import cucumber.api.java8.En
import javafx.scene.control.Button
import javafx.scene.input.DataFormat
import org.junit.Assert

class ClipboardSteps(val world : World) : En {

    init {
        When("^I copy it to the clipboard$", {
            Stirry.findInRoot<Button>{
                it.id == "clipboardButton"
            }.value.fireAndStir()
        })

        Then("^pasting it elsewhere should result in$", {expectedDistributionAsOneLine : String ->
            val text = Stirry.getClipboard(DataFormat.PLAIN_TEXT)

            val expectedDistribution = expectedDistributionAsOneLine.split(',')
                    .map { it.trim()}
                    .joinToString(separator = "\n")
            Assert.assertEquals(expectedDistribution, text)
        });
    }
}

