package com.lunivore.montecarluni

import com.lunivore.montecarluni.model.*
import org.apache.logging.log4j.LogManager
import org.reactfx.EventSource
import java.io.InputStream

class Events {

    val logger = LogManager.getLogger()

    var fileImportRequest = EventSource<String>()
    val clipboardCopyRequest = EventSource<Unit>()
    val weeklyDistributionChangeNotification = EventSource<WeeklyDistribution>()
    val inputLoadedNotification = EventSource<InputStream>()
    val recordsParsedNotification = EventSource<List<Record>>()
    val messageNotification = EventSource<UserNotification>()
    val forecastRequest = EventSource<ForecastRequest>()
    val forecastNotification = EventSource<Forecast>()

    val  All = listOf(
            fileImportRequest,
            clipboardCopyRequest,
            weeklyDistributionChangeNotification,
            inputLoadedNotification,
            recordsParsedNotification,
            messageNotification)

    init {
        fileImportRequest.subscribe { logger.debug("File import requested: $it") }
        clipboardCopyRequest.subscribe { logger.debug("Clipboard copy requested") }
        weeklyDistributionChangeNotification.subscribe { logger.debug("Weekly distribution changed") }
        inputLoadedNotification.subscribe { logger.debug("Input loaded") }
        recordsParsedNotification.subscribe { logger.debug("Records parsed") }
        messageNotification.subscribe { logger.debug("Message created: ${it.message}") }
        forecastNotification.subscribe { logger.debug("Forecast created") }
    }
}

