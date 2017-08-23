package com.lunivore.montecarluni.scenarios

import cucumber.api.CucumberOptions
import cucumber.api.junit.Cucumber
import org.junit.runner.RunWith

@RunWith(Cucumber::class)
@CucumberOptions(
        format = arrayOf("pretty"),
        glue = arrayOf("com.lunivore.montecarluni.steps", "com.lunivore.montecarluni.glue"),
        features = arrayOf("."))
class Runner {
}
