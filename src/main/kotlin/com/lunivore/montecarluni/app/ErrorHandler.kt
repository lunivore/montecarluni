package com.lunivore.montecarluni.app

import com.lunivore.montecarluni.model.UserNotification
import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType.ERROR
import javafx.scene.control.Alert.AlertType.INFORMATION
import javafx.scene.control.ButtonType
import org.apache.logging.log4j.LogManager


class ErrorHandler() {
    val logger = LogManager.getLogger()

    fun  handleNotification(it: UserNotification) {
        Alert(INFORMATION, it.message, ButtonType.CLOSE).show()
    }

    fun handleError(it: Throwable?) {
        Alert(ERROR, "An unexpected error occurred!\n${it?.message}", ButtonType.CLOSE).show()
        logger.error(it)
    }
}