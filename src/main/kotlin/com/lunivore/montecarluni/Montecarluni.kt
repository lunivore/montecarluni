package com.lunivore.montecarluni

import com.lunivore.montecarluni.app.ClipboardCopier
import com.lunivore.montecarluni.app.ErrorHandler
import com.lunivore.montecarluni.app.MontecarluniApp
import com.lunivore.montecarluni.engine.*
import javafx.application.Application
import javafx.stage.Stage
import org.apache.logging.log4j.LogManager

class Montecarluni : Application() {
    override fun start(primaryStage: Stage) {
        logger.debug("Starting Montecarluni...")
        var events = Events()
        var inputProvider = FileInputStreamProvider(events)
        var distributionCalculator = DistributionCalculator(events)
        var recordCreator = MultiFormatDateRecordCreator(events)
        var clipboardCopier = ClipboardCopier(events)
        MontecarluniApp(events).start(primaryStage)
        logger.debug("...Montecarluni started successfully.")
    }

    companion object {
        val logger = LogManager.getLogger()

        @JvmStatic fun main(args: Array<String>) {
            Application.launch(Montecarluni::class.java, *args)
        }
    }
}
