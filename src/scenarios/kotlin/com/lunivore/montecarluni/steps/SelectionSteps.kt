package com.lunivore.montecarluni.steps

import com.lunivore.montecarluni.glue.World
import com.lunivore.stirry.Stirry
import com.lunivore.stirry.checkAndStir
import javafx.scene.control.CheckBox
import javafx.scene.control.TableView

class SelectionSteps(world: World) {
    fun  select(fromRow: Int) {
        val distributionControl = Stirry.findInRoot<TableView<Map<String, Int>>> {
            it.id == "distributionOutput"
        }.value
        distributionControl.selectionModel.selectRange(fromRow-1, distributionControl.items.count())
        Stirry.findInRoot<CheckBox> { it.id == "useSelectionInput" }.value.checkAndStir()
    }
}