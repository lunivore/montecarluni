package com.lunivore.montecarluni

import com.lunivore.montecarluni.model.UserNotification
import com.lunivore.montecarluni.model.Record
import com.lunivore.montecarluni.model.WeeklyDistribution
import org.reactfx.EventSource
import java.io.InputStream

class Events {
    var fileImportRequest = EventSource<String>()
    val clipboardCopyRequest = EventSource<Unit>()
    val weeklyDistributionChangeNotification = EventSource<WeeklyDistribution>()
    val inputLoadedNotification = EventSource<InputStream>()
    val recordsParsedNotification = EventSource<List<Record>>()
    val messageNotification = EventSource<UserNotification>()

    val  All = listOf(
            fileImportRequest,
            clipboardCopyRequest,
            weeklyDistributionChangeNotification,
            inputLoadedNotification,
            recordsParsedNotification,
            messageNotification)

}

