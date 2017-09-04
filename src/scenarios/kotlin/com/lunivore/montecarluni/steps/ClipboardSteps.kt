package com.lunivore.montecarluni.steps

import com.lunivore.montecarluni.glue.Scenario
import com.lunivore.montecarluni.glue.World
import com.lunivore.stirry.Stirry.Companion.findInRoot
import com.lunivore.stirry.Stirry.Companion.getClipboard
import com.lunivore.stirry.fireAndStir
import javafx.scene.control.Button
import org.junit.Assert.assertEquals

class ClipboardSteps(val world : World) : Scenario(world) {

    init {
        When("^I copy it to the clipboard$", {
            findInRoot<Button> {
                it.id == "clipboardButton"
            }.value.fireAndStir()
        })

        When("^I copy rows (\\d+) onwards to the clipboard$", {fromRow : Int ->
            SelectionSteps(world).select(fromRow)
            findInRoot<Button> {
                it.id == "clipboardButton"
            }.value.fireAndStir()
        })

        Then("^pasting it elsewhere should result in$", {expectedDistributionAsOneLine : String ->
            val text = getClipboard(javafx.scene.input.DataFormat.PLAIN_TEXT)

            val expectedDistribution = expectedDistributionAsOneLine.split(',')
                    .map { it.trim()}
                    .joinToString(separator = "\n")
            assertEquals(expectedDistribution, text)
        });
    }
}

