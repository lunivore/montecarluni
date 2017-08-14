package com.lunivore.montecarluni.engine

import com.lunivore.montecarluni.Events
import com.lunivore.montecarluni.model.UserNotification
import org.apache.logging.log4j.LogManager
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

class FileInputStreamProvider(val events: Events) {

    companion object {
        val logger = LogManager.getLogger()!!
    }

    init {
        events.fileImportRequest.subscribe {
            logger.debug("Loading file {}", it)
            val file = fetch(it)
            if (file != null) {events.inputLoadedNotification.push(file)}
        }
    }

    private fun fetch(filename: String): InputStream? {
        var candidate = trimQuotes(filename)
        if (!File(candidate).exists()) {
            events.messageNotification.push(UserNotification("The file \"$filename\" could not be found"))
            return null
        }
        return FileInputStream(candidate)
    }

    private fun trimQuotes(filename: String): String {
        var candidate = filename
        if (candidate.startsWith('"') && candidate.endsWith('"')) {
            candidate = filename.trim('"')
        }
        return candidate
    }
}

