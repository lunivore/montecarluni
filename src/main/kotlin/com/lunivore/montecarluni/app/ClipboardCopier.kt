package com.lunivore.montecarluni.app

import com.lunivore.montecarluni.Events
import com.lunivore.montecarluni.model.Distributions
import com.lunivore.montecarluni.model.UserNotification
import javafx.application.Platform
import javafx.scene.input.Clipboard
import javafx.scene.input.ClipboardContent

class ClipboardCopier(events: Events) {
    private val lineSeparatorForExcel = "\n"
    private var  lastDistribution: Distributions? = null

    init {
        events.distributionChangeNotification.subscribe { lastDistribution = it }

        events.clipboardCopyRequest.subscribe {
            val distributionToUse = lastDistribution
            if (distributionToUse == null) {
                events.messageNotification.push(UserNotification("Please import a file first!"))
            } else {
                val selection = it.selectedIndices
                val toCopy = if (it.useSelection) {
                    distributionToUse.workItemsDone.filterIndexed { index, content -> selection.contains(index) }
                            .map { it.count }.joinToString(separator = lineSeparatorForExcel)
                } else {
                    distributionToUse.workItemsDone.map { it.count }.joinToString(separator = lineSeparatorForExcel)
                }

                val content = ClipboardContent()
                content.putString(toCopy)
                Platform.runLater { Clipboard.getSystemClipboard().setContent(content) }
            }
        }
    }

}
