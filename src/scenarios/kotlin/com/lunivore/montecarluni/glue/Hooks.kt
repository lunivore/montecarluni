package com.lunivore.montecarluni.glue

import com.lunivore.montecarluni.Montecarluni
import com.lunivore.stirry.Stirry
import cucumber.api.java.After
import cucumber.api.java.Before

class Hooks constructor() {

    lateinit var montecarluni : Montecarluni

    @Before
    fun initializeStirry() {
        Stirry.initialize()
        montecarluni = Montecarluni()
        Stirry.launchApp(montecarluni)

    }

    @After
    fun closeStirry() {
        Stirry.stop()
    }
}
