package com.lunivore.montecarluni.steps

import com.lunivore.montecarluni.glue.Scenario
import com.lunivore.montecarluni.glue.World
import com.lunivore.stirry.Stirry.Companion.findInRoot
import com.lunivore.stirry.fireAndStir
import com.lunivore.stirry.setTextAndStir
import javafx.scene.control.Button
import javafx.scene.control.TextField

class FileImportSteps(val world: World) : Scenario(world) {

    init {
        Given("^a typical JIRA export \"([^\"]*)\"$", {filename : String ->
            world.desiredFilename = filename
        })

        When("^I import it into Montecarluni$", {
            val file = javaClass.getResource(world.desiredFilename)
            findInRoot<TextField> { true }.value.setTextAndStir(file.toURI().path)
            findInRoot<Button> { it.text == "Import" }.value.fireAndStir()
        })

        Given("^\"([^\"]*)\" is imported$", {filename : String ->
            world.desiredFilename = filename
            val file = javaClass.getResource(world.desiredFilename)
            findInRoot<TextField> { it is TextField }.value.setTextAndStir(file.toURI().path)
            findInRoot<Button> { it.text == "Import" }.value.fireAndStir()
        })

        Given("^a file that doesn't exist \"([^\"]*)\"$", {filename : String ->
            world.desiredFilename = filename
        })

        When("^I try to import that file$", {
            findInRoot<TextField> { true }.value.setTextAndStir(world.desiredFilename!!)
            findInRoot<Button> { it.text == "Import" }.value.fireAndStir()
        })
    }

}

