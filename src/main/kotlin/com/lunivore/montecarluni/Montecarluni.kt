package com.lunivore.montecarluni

import com.lunivore.montecarluni.app.MontecarluniApp
import javafx.application.Application

class Montecarluni {
    companion object {
        @JvmStatic fun main(args: Array<String>) {


            Application.launch(MontecarluniApp::class.java, *args)
        }
    }
}
