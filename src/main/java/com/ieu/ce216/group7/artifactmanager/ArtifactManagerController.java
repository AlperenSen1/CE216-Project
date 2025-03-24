package com.ieu.ce216.group7.artifactmanager;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ArtifactManagerController {
    @FXML
    private Label artifactNameLbl;
    @FXML
    private TextField artifactNameTb;

    @FXML
    protected void onSaveFormBtnClick() {
        artifactNameTb.setText("Welcome to JavaFX Application!");
    }
}