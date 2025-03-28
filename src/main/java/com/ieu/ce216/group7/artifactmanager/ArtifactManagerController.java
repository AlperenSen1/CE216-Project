package com.ieu.ce216.group7.artifactmanager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class ArtifactManagerController {

    @FXML
    private HBox contextPathHbox;
    @FXML
    private Label contextPathLbl;
    @FXML
    private TextField contextPathTf;
    @FXML
    private Button saveContextPathBtn;

    @FXML
    private Button displayArtifactsBtn;

    @FXML
    protected void onDisplayArtifactsBtnClick() {
        if(checkContextPath()){
            //TODO  open dsiplay artifacts window
        }
    }
    @FXML
    protected void onSaveArtifactBtnClick() {
        if(checkContextPath()){
            //TODO  open new artifact window
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("artifactmanager-newartifact-view.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setTitle("New Artifact");
                stage.setScene(new Scene(root1));
                stage.show();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    protected void onSaveContextPathBtnClick() {
        Properties properties = new Properties();
        Path propFile = Paths.get(ArtifactManagerApplication.class.getResource("application.properties").getPath().substring(1));
        try {
            properties.load(Files.newBufferedReader(propFile));
            properties.setProperty("context.path", contextPathTf.getText());
            properties.store(Files.newBufferedWriter(propFile), null);

            Files.createDirectories(Paths.get(properties.getProperty("context.path")));
            Files.createFile(Paths.get(properties.getProperty("context.path")+"/ArtifactManagerDB.json"));

            contextPathLbl.setText("Context Path is set");
            contextPathTf.setVisible(false);
            saveContextPathBtn.setVisible(false);
        } catch (IOException e) {
            properties=null;
        }
        if(properties!=null){

        }
    }


    private boolean checkContextPath(){
        boolean result = true;
        Properties properties = new Properties();
        Path propFile = Paths.get(ArtifactManagerApplication.class.getResource("application.properties").getPath().substring(1));
        try {
            properties.load(Files.newBufferedReader(propFile));
        } catch (IOException e) {
            properties=null;
        }
        if(properties!=null && properties.getProperty("context.path")!=null) {
            //indexLbl.setText(properties.getProperty("context.path"));
        }else{
            contextPathLbl.setText("Context Path not set. Please enter the context path:");
            contextPathHbox.setVisible(true);
            contextPathLbl.setVisible(true);
            contextPathTf.setVisible(true);
            saveContextPathBtn.setVisible(true);
            result = false;
        }
        return result;
    }
}