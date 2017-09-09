package com.lunivore.montecarluni.steps

import com.lunivore.montecarluni.glue.Scenario
import com.lunivore.montecarluni.glue.World
import com.lunivore.stirry.Stirry.Companion.findInRoot
import com.lunivore.stirry.fireAndStir
import javafx.scene.control.*
import org.junit.Assert.*

class CleardownSteps(val world: World) : Scenario(world) {
    init {
        When("^I clear down the data") {
            findInRoot<Button>({it.id == "clearButton"}).value.fireAndStir()
        }

        Then("^all the relevant fields should be empty") {
            assertEquals("", findInRoot<TextField> { it.id == "filenameInput" }.value.text)
            assertEquals(0, findInRoot<TableView<Any>>{it.id == "weeklyDistributionOutput"}.value.items.size)
            assertFalse(findInRoot<CheckBox>{it.id =="useSelectionInput"}.value.isSelected)
            assertNull(findInRoot<DatePicker>{ it.id == "forecastStartDateInput"}.value.value)
            assertEquals("", findInRoot<TextField> { it.id == "numWorkItemsForecastInput"}.value.text)
            assertEquals(0, findInRoot<TableView<Any>> {it.id == "forecastOutput"}.value.items.size)
        }
    }
}