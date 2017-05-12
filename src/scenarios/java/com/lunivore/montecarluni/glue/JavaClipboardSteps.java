package com.lunivore.montecarluni.glue;

import com.lunivore.stirry.Stirry;
import cucumber.api.java8.En;
import javafx.scene.control.Button;

public class JavaClipboardSteps implements En {

    public JavaClipboardSteps(World world) {
        When("^I copy it to the clipboard$", () -> {
            Stirry.Companion.buttonClick((Button button) -> { return button.textProperty().get().equals("Copy to Clipboard"); });
        });
    }
}
