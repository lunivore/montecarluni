package com.lunivore.montecarluni.app

import com.lunivore.montecarluni.model.CycleTime
import java.time.format.DateTimeFormatter

class CycleTimePresenter(private val model: CycleTime) {

    companion object {
        private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    }

    val id: String
        get() { return model.id }

    val  date : String
        get() { return formatter.format(model.date)}

    val gap: String
        get() {
            return if (model.gap == null) "-" else
                "${model.gap.toDays()}d ${(model.gap.toHours().rem(24))}h"
        }

}

