package com.lunivore.montecarluni

import com.google.inject.Guice
import com.lunivore.montecarluni.app.MontecarluniView
import com.lunivore.montecarluni.app.Styles
import javafx.application.Application
import javafx.stage.Stage
import org.apache.logging.log4j.LogManager
import tornadofx.App
import tornadofx.DIContainer
import tornadofx.FX
import kotlin.reflect.KClass

class Montecarluni : App(MontecarluniView::class, Styles::class) {

    override fun start(stage: Stage) {
        val guice = Guice.createInjector(MModule())

        FX.dicontainer = object : DIContainer {
            override fun <T : Any> getInstance(type: KClass<T>)
                    = guice.getInstance(type.java)
        }
        super.start(stage)
    }

    companion object {
        val logger = LogManager.getLogger()

        @JvmStatic fun main(args: Array<String>) {
            Application.launch(Montecarluni::class.java, *args)
        }
    }
}

