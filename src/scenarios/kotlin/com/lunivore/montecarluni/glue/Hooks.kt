package com.lunivore.montecarluni.glue

import com.lunivore.montecarluni.Montecarluni
import com.lunivore.stirry.Stirry
import cucumber.api.java.Before
import cucumber.api.java8.En

class Hooks constructor() : En {

    @Before
    fun initializeStirry() {
        Stirry.initialize()
        Stirry.startApp(Montecarluni())
    }
}
