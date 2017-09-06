package com.lunivore.montecarluni.app

import javafx.scene.layout.BorderStrokeStyle.SOLID
import tornadofx.*

class Styles : Stylesheet() {

    companion object {
        val buttonBox by cssclass()
        val borderBox by cssclass()

        private val lightGrey = c("#f4f4f4")
        private val midGrey = c("#cfcfcf")

    }

    init {
        buttonBox {
            padding = box(10.px, 10.px, 10.px, 0.px)
            spacing = 10.px
            hgap = 10.px
            vgap = 10.px
        }

        borderBox {
            padding = box(10.px, 10.px, 10.px, 10.px)
        }
        tabHeaderBackground {
            backgroundColor += lightGrey
        }

        tabContentArea {
            borderColor += box(midGrey)
            borderWidth += box(1.px)
            borderStyle += SOLID
        }


    }
}