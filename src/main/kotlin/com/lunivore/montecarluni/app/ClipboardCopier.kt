package com.lunivore.montecarluni.app

import com.lunivore.montecarluni.Events
import com.lunivore.montecarluni.model.WeeklyDistribution
import javafx.application.Platform
import javafx.scene.input.Clipboard
import javafx.scene.input.ClipboardContent

class ClipboardCopier(events: Events) {
    private var  lastDistribution: WeeklyDistribution? = null
    private var  selectedIndices: List<Int>? = null

    init {
        events.weeklyDistributionChangeNotification.subscribe { lastDistribution = it }
        events.weeklyDistributionSelectionRequest.subscribe { selectedIndices = it }

        events.clipboardCopyRequest.subscribe {
            val selection = selectedIndices
            val toCopy = if(selection == null) {
                lastDistribution?.distributionAsString
            } else {
                lastDistribution?.distributionAsString(selection)
            }

            val content = ClipboardContent()
            content.putString(toCopy)
            Platform.runLater{Clipboard.getSystemClipboard().setContent(content)}
        }
    }

}
