package com.ieu.ce216.group7.artifactmanager;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ieu.ce216.group7.artifactmanager.model.Artifact;
import com.ieu.ce216.group7.artifactmanager.model.Dimension;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ArtifactManagerController {
    private static Properties properties;
    private static File dbFile;

    public TextField artifactNameTf;
    public TextField disLocationTf;
    public TextField civilizationTf;
    public ComboBox categoryCb;
    public ComboBox compositionCb;
    public DatePicker disDateDp;
    public TextField currPlaceTf;
    public TextField dimensionsTf;
    public TextArea weightTf;
    public ChoiceBox tagsCC;

    public TableColumn idCol;
    public TableColumn nameCol;
    public TableColumn categoryCol;
    public TableColumn civilCol;
    public TableColumn compositionCol;
    public TableColumn disLocCol;
    public TableColumn currPlsceCol;

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
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("artifactmanager-listartifacts-view.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                Stage stage = new Stage();
                //stage.initModality(Modality.APPLICATION_MODAL);
                //stage.initStyle(StageStyle.UNDECORATED);
                stage.setTitle("List Artifacts");
                stage.setScene(new Scene(root1));
                stage.show();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    @FXML
    protected void onNewArtifactBtnClick() {
        if(checkContextPath()){
            //TODO  open new artifact window
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("artifactmanager-newartifact-view.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                Stage stage = new Stage();
                //stage.initModality(Modality.APPLICATION_MODAL);
                //stage.initStyle(StageStyle.UNDECORATED);
                stage.setTitle("New Artifact");
                stage.setScene(new Scene(root1));
                stage.show();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


    @FXML
    protected void onSearchArtifactBtnClick() {
        List<Artifact> artifacts=JSONFileHandler.getArtifactsFromJSONFile(dbFile);
        List<Artifact> filteredArtifacts=artifacts
                .stream()
                .filter(a -> a.getArtifactName().contains(artifactNameTf.getText()))
                .collect(Collectors.toList());

    }

        @FXML
    protected void onSaveArtifactBtnClick() {

        List<Artifact> artifacts=JSONFileHandler.getArtifactsFromJSONFile(dbFile);

        Artifact artifact = new Artifact();
        artifact.setArtifactId(disDateDp.getValue().getYear()+currPlaceTf.getText().substring(0,3)+artifactNameTf.getText().substring(0,3));
        artifact.setArtifactName(artifactNameTf.getText());
        artifact.setCivilization(civilizationTf.getText());
        artifact.setCategory(categoryCb.getSelectionModel().getSelectedItem().toString());
        artifact.setDiscoveryLocation(disLocationTf.getText());
        artifact.setDiscoverydate(Date.from(disDateDp.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        artifact.setComposition(compositionCb.getSelectionModel().getSelectedItem().toString());
        artifact.setCurrentLocation(currPlaceTf.getText());
        artifact.setTags(tagsCC.getSelectionModel().getSelectedItem().toString());
        artifact.setWeight(Double.parseDouble(weightTf.getText()));

        Dimension dim=new Dimension();
        try {
            dim.setWidth(Double.parseDouble( dimensionsTf.getText().split(Pattern.quote(","))[0] ));
            dim.setHeight(Double.parseDouble( dimensionsTf.getText().split(Pattern.quote(","))[1] ));
            dim.setLength(Double.parseDouble( dimensionsTf.getText().split(Pattern.quote(","))[2] ));
        } catch (Exception ignored) {

        }
        artifact.setDimensions(dim);
        if(artifacts==null){
            artifacts=new ArrayList<Artifact>();
        }
        artifacts.add(artifact);

        if(JSONFileHandler.saveArtifacts(artifacts, dbFile).equals("1")){
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setContentText("Artifact Saved with Id: "+artifact.getArtifactId());
            a.show();
        }

    }

    @FXML
    protected void onSaveContextPathBtnClick() {
        properties = new Properties();
        Path propFile = Paths.get(getPropsFile());
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
        properties = new Properties();
        //Path propFile = Paths.get(ArtifactManagerApplication.class.getResource("application.properties").getPath().substring(1));

        Path propFile = Paths.get(getPropsFile());
        try {
            properties.load(Files.newBufferedReader(propFile));
        } catch (IOException e) {
            properties=null;
        }
        if(properties!=null && properties.getProperty("context.path")!=null) {
            //indexLbl.setText(properties.getProperty("context.path"));
            dbFile=new File(properties.getProperty("context.path")+"/ArtifactManagerDB.json");
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

    private String getPropsFile(){
        String proppath=System.getProperty("user.home")+"/.ArtifactManager/";
        proppath=proppath.replaceAll(Pattern.quote("\\"),"/");
        System.out.println(proppath);
        if(!new File(proppath).exists()){
            new File(proppath).mkdirs();
        }
        File pf=new File(proppath+"application.properties");
        try {
            pf.createNewFile();
            System.out.println(pf.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Path propFile = Paths.get(ArtifactManagerApplication.class.getResource("/").getFile()+"/application.properties");

        return proppath+"application.properties";
    }
}