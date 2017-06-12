package com.lunivore.montecarluni.glue

import com.lunivore.montecarluni.Montecarluni
import com.lunivore.stirry.Stirry
import cucumber.api.java.After
import cucumber.api.java.Before
import cucumber.api.java8.En

class Hooks constructor() : En {

    lateinit var montecarluni : Montecarluni

    @Before
    fun initializeStirry() {
        System.out.println("Initializing Stirry...")
        Stirry.initialize()
        montecarluni = Montecarluni()
        Stirry.startApp(montecarluni)
    }

    @After
    fun closeStirry() {
        System.out.println("Stopping Stirry...")
        Stirry.stop()
    }
}
