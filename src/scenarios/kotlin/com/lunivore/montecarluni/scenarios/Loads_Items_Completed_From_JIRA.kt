package com.lunivore.montecarluni.scenarios

import com.lunivore.montecarluni.app.MontecarluniApp
import com.lunivore.stirry.Stirry
import javafx.scene.control.Label
import javafx.scene.control.TextField
import org.junit.Assert.assertEquals
import org.junit.BeforeClass
import org.junit.Test

class Loads_Items_Completed_From_JIRA {

    companion object {
        @JvmStatic
        @BeforeClass
        fun initializeStirry() {
            Stirry.initialize()
            Stirry.startApp(MontecarluniApp())
        }
    }

    // By the way, this is a terrible scenario but the outcome isn't quite working yet, so I'm all good with that.
    // It's just here to run the prototype.
    @Test
    fun JIRA_of_completed_only_loads_fine() {
        // Given a JIRA file with only completed items
        val filename = "/Closed Only JIRA.csv"

        // When Montecarluni imports it
        Stirry.setText({it is TextField }, filename)
        Stirry.buttonClick { it.text == "Import" }

        // Then it should automatically work out the number of
        // completed items for each week
        val distributionControl = Stirry.find<Label> {it.id == "distributionOutput"}
        val distributions = distributionControl?.text?.lines()

        assertEquals(15, distributions?.size)
    }
}

