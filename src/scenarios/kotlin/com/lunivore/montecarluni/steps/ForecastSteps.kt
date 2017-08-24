package com.lunivore.montecarluni.steps

import com.lunivore.montecarluni.glue.Scenario
import com.lunivore.montecarluni.glue.World
import com.lunivore.stirry.Stirry.Companion.findInRoot
import com.lunivore.stirry.Stirry.Companion.findRoot
import com.lunivore.stirry.Stirry.Companion.runOnPlatform
import com.lunivore.stirry.fireAndStir
import com.lunivore.stirry.setTextAndStir
import javafx.scene.control.*
import org.junit.Assert
import org.junit.Assert.assertEquals
import java.time.LocalDate

class ForecastSteps(val world: World) : Scenario(world) {

    init {
        When("^I ask for a forecast for the next (\\d+) stories$", { number: Int ->
            requestForecast(number)
        })

        When("^I ask for a forecast for (\\d+) stories starting on (.*)$", {number: Int, date : String ->
            findInRoot<DatePicker>({it.id == "forecastStartDateInput"}).value.value = toDate(date)
            requestForecast(number)
        })

        Then("^I should see a percentage forecast$", { expectedForecast: String ->
            val forecast = findInRoot<TableView<Map<String, String>>>({it.id == "forecastOutput"}).value

            val forecastAsString = forecast.items.map { "${it["probability"]} | ${it["forecastDate"]}" }
                    .joinToString(separator = "\n")

            assertEquals(expectedForecast, forecastAsString)
        })
    }

    private fun toDate(date: String): LocalDate? {
        val match = Regex("(\\d+)-(\\d+)-(\\d+)").matchEntire(date)
        if (match == null) {
            Assert.fail("Failed to match provided date to pattern yyyy-MM-dd")
            return null
        } else {
            return LocalDate.of(match.groupValues[1].toInt(), match.groupValues[2].toInt(), match.groupValues[3].toInt())
        }
    }

    private fun requestForecast(number: Int) {
        val tabs = findRoot() as TabPane
        runOnPlatform { tabs.selectionModel.select(tabs.tabs.first { it.id == "forecastTab" }) }

        findInRoot<TextField>({ it.id == "numStoriesForecastInput" }).value.setTextAndStir(number.toString())
        findInRoot<Button>({ it.id == "forecastButton" }).value.fireAndStir()
    }
}