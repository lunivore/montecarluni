package com.lunivore.montecarluni.glue

import com.lunivore.stirry.Stirry
import cucumber.api.java8.En
import javafx.scene.control.TextField

class FileImportSteps(val world: World) : En {

    init {
        Given("^a typical JIRA export \"([^\"]*)\"$", {filename : String ->
            world.desiredFilename = filename
        })

        When("^I import it into Montecarluni$", {
            val file = this.javaClass.getResource(world.desiredFilename)
            Stirry.setText({it is TextField }, file.toURI().path)
            Stirry.buttonClick { it.text == "Import" }
        })
    }
}

