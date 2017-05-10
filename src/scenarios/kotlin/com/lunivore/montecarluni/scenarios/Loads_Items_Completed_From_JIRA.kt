package com.lunivore.montecarluni.scenarios

import com.lunivore.montecarluni.Montecarluni
import com.lunivore.stirry.Stirry
import javafx.application.Platform
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.input.Clipboard
import javafx.scene.input.DataFormat
import org.junit.Assert.assertEquals
import org.junit.BeforeClass
import org.junit.Test
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.TimeUnit

class Loads_Items_Completed_From_JIRA {

    val ln = System.getProperty("line.separator")

    companion object {
        @JvmStatic
        @BeforeClass
        fun initializeStirry() {
            Stirry.initialize()
            Stirry.startApp(Montecarluni())
        }
    }

    // By the way, this is a terrible scenario but the outcome isn't quite working yet, so I'm all good with that.
    // It's just here to run the prototype.
    @Test
    fun JIRA_of_completed_only_loads_fine() {
        // Given a JIRA file with only completed items
        val filename = "/Closed Only JIRA.csv"
        val file = this.javaClass.getResource(filename)

        // When Montecarluni imports it
        Stirry.setText({it is TextField }, file.toURI().path)
        Stirry.buttonClick { it.text == "Import" }

        // Then it should automatically work out the number of
        // completed items for each week
        val distributionControl = Stirry.find<Label> {it.id == "distributionOutput"}
        val distributions = distributionControl?.text

        val expected = listOf<Int>(6, 15, 3, 14, 2, 5, 6, 8, 5, 10, 15, 4, 2, 1).joinToString("\n")
        assertEquals(expected, distributions)
    }

    @Test
    fun can_copy_results_to_clipboard_for_excel() {
        // Given a JIRA file with only completed items
        val filename = "/Closed Only JIRA.csv"
        val file = this.javaClass.getResource(filename)

        // When we import it and copy it to the clipboard
        Stirry.setText({it is TextField }, file.toURI().path)
        Stirry.buttonClick { it.text == "Import" }
        Stirry.buttonClick { it.text == "Copy to Clipboard" }

        var text = ""
        var queue = ArrayBlockingQueue<Boolean>(1)
        Platform.runLater {
            text = Clipboard.getSystemClipboard().getContent(DataFormat.PLAIN_TEXT).toString()
            queue.put(true)
        }
        queue.poll(1L, TimeUnit.SECONDS)

        // Then we should have something we can paste into Excel

        val expected = listOf<Int>(6, 15, 3, 14, 2, 5, 6, 8, 5, 10, 15, 4, 2, 1).joinToString("\n")
        assertEquals(expected, text)
    }

}

