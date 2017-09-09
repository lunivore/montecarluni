package com.lunivore.montecarluni.app

import javafx.util.StringConverter

class IntObjectConverter : StringConverter<Int>() {
    override fun fromString(string: String?): Int? {
        return string?.toInt()
    }

    override fun toString(input: Int?): String {
        return input?.toString() ?: ""
    }

}