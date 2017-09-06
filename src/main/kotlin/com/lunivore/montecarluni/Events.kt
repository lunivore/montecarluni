package com.lunivore.montecarluni

import com.lunivore.montecarluni.model.*
import org.apache.logging.log4j.LogManager
import org.reactfx.EventSource
import java.io.InputStream

class Events {

    val logger = LogManager.getLogger()

    var fileImportRequest = EventSource<String>()
    val clipboardCopyRequest = EventSource<ClipboardRequest>()
    val weeklyDistributionChangeNotification = EventSource<WeeklyDistribution>()
    val inputLoadedNotification = EventSource<InputStream>()
    val recordsParsedNotification = EventSource<List<Record>>()
    val messageNotification = EventSource<UserNotification>()
    val forecastRequest = EventSource<ForecastRequest>()
    val forecastNotification = EventSource<Forecast>()
    val  clearRequest = EventSource<Unit>()

    val  All = listOf(
            fileImportRequest,
            clipboardCopyRequest,
            weeklyDistributionChangeNotification,
            inputLoadedNotification,
            recordsParsedNotification,
            messageNotification,
            forecastRequest,
            forecastNotification,
            clearRequest)

    init {
        fileImportRequest.subscribe { logger.debug("File import requested: $it") }
        clipboardCopyRequest.subscribe { logger.debug("Clipboard copy requested") }
        weeklyDistributionChangeNotification.subscribe { logger.debug("Weekly distribution changed") }
        inputLoadedNotification.subscribe { logger.debug("Input loaded") }
        recordsParsedNotification.subscribe { logger.debug("Records parsed") }
        messageNotification.subscribe { logger.debug("Message created: ${it.message}") }
        forecastRequest.subscribe { logger.debug("Forecast requested") }
        forecastNotification.subscribe { logger.debug("Forecast created") }
        clearRequest.subscribe { logger.debug("Cleardown requested") }
    }


}

