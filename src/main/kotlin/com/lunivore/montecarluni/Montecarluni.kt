package com.lunivore.montecarluni

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.singleton
import com.lunivore.montecarluni.app.MontecarluniApp
import com.lunivore.montecarluni.engine.*
import javafx.application.Application
import javafx.stage.Stage

class Montecarluni : Application() {
    override fun start(primaryStage: Stage) {
        val kodein = Kodein {
            bind<IProvideInputStreams>() with singleton { FileInputStreamProvider() }
            bind<ICalculateWeeklyDistribution>() with singleton { DistributionCalculator() }
            bind<ICreateRecordsFromCvs>() with singleton { MultiFormatDateRecordCreator() }
            bind<MontecarluniController>() with singleton { MontecarluniController(instance(), instance(), instance())}
        }

        MontecarluniApp(kodein).start(primaryStage)
    }

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            Application.launch(Montecarluni::class.java, *args)
        }
    }
}
