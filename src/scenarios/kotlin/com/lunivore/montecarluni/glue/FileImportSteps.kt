package com.lunivore.montecarluni.glue

import com.lunivore.stirry.Stirry
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
            Stirry.findInRoot<TextField>{ true }.value.setTextAndStir(file.toURI().path)
            Stirry.findInRoot<Button> { it.text == "Import" }.value.fireAndStir()
        })

        Given("^\"([^\"]*)\" is imported$", {filename : String ->
            world.desiredFilename = filename
            val file = javaClass.getResource(world.desiredFilename)
            Stirry.findInRoot<TextField>{ it is TextField }.value.setTextAndStir(file.toURI().path)
            Stirry.findInRoot<Button> {it.text == "Import" }.value.fireAndStir()
        })

        Given("^a file that doesn't exist \"([^\"]*)\"$", {filename : String ->
            world.desiredFilename = filename
        })

        When("^I try to import that file$", {
            Stirry.findInRoot<TextField>{ true }.value.setTextAndStir(world.desiredFilename!!)
            Stirry.findInRoot<Button> { it.text == "Import" }.value.fireAndStir()
        })
    }

}

