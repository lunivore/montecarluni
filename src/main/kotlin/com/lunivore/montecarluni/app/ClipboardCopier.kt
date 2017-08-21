package com.lunivore.montecarluni.app

import com.lunivore.montecarluni.Events
import com.lunivore.montecarluni.model.WeeklyDistribution
import javafx.application.Platform
import javafx.scene.input.Clipboard
import javafx.scene.input.ClipboardContent

class ClipboardCopier(events: Events) {
    private var  lastDistribution: WeeklyDistribution? = null

    init {
        events.weeklyDistributionChangeNotification.subscribe { lastDistribution = it }

        events.clipboardCopyRequest.subscribe {
            val content = ClipboardContent()
            content.putString(lastDistribution?.distributionAsString)
            Platform.runLater{Clipboard.getSystemClipboard().setContent(content)}
        }
    }

}
