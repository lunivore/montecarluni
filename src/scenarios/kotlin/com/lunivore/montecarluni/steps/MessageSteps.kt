package com.lunivore.montecarluni.steps

import com.lunivore.stirry.Stirry.Companion.findInModalDialog
import com.lunivore.stirry.fireAndStir
import cucumber.api.java8.En
import javafx.scene.control.Button
import javafx.scene.control.DialogPane
import org.junit.Assert.assertTrue

class MessageSteps : En
{
    init {
        Then("^I should see an error '(.*)'$", { message : String ->
            val foundDialog = findInModalDialog<DialogPane>({
                it is DialogPane && it.contentText.contains(message)
            })
            assertTrue(foundDialog.succeeded)

            findInModalDialog<Button> { it.text == "Close" }.value.fireAndStir()
        })
    }
}