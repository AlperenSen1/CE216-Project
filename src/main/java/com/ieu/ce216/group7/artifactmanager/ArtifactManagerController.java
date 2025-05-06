
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
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
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
    public TableColumn currPlaceCol;

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
    private Button selectImageBtn;
    @FXML
    private Label selectedImagePathLbl;

    private String selectedImagePath = null;

    @FXML
    private TableView<Artifact> artifactListTV;



    @FXML
    protected void onDisplayArtifactsBtnClick() {
        if (Utils.checkContextPath()) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("artifactmanager-listartifacts-view.fxml"));
                Parent root1 = fxmlLoader.load();

                // 🔥 Controller'a eriş
                com.ieu.ce216.group7.artifactmanager.ArtifactListController controller = fxmlLoader.getController();

                // ✅ JSON dosya yolunu veriyoruz
                //controller.setDbFile(Utils.dbFile);

                // JSON'dan verileri oku
                List<Artifact> artifacts = JSONFileHandler.getArtifactsFromJSONFile(Utils.dbFile);
                controller.populateTable(artifacts);

                // Pencereyi aç
                Stage stage = new Stage();
                stage.setTitle("List Artifacts");
                stage.setScene(new Scene(root1));
                stage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            contextPathLbl.setText("Context Path not set. Please enter the context path:");
            contextPathHbox.setVisible(true);
            contextPathLbl.setVisible(true);
            contextPathTf.setVisible(true);
            saveContextPathBtn.setVisible(true);
        }
    }




    @FXML
    protected void onNewArtifactBtnClick() {
        if(Utils.checkContextPath()){
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
        }else{
            contextPathLbl.setText("Context Path not set. Please enter the context path:");
            contextPathHbox.setVisible(true);
            contextPathLbl.setVisible(true);
            contextPathTf.setVisible(true);
            saveContextPathBtn.setVisible(true);
        }
    }


    @FXML
    protected void onSearchArtifactBtnClick() {
        if (Utils.dbFile == null || !Utils.dbFile.exists()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("DB File not found.");
            alert.show();
            return;
        }

        List<Artifact> artifacts = JSONFileHandler.getArtifactsFromJSONFile(Utils.dbFile);
        if (artifacts == null) artifacts = new ArrayList<>();

        String searchText = artifactNameTf.getText().toLowerCase().trim();

        List<Artifact> filteredArtifacts = artifacts.stream()
                .filter(a -> a.getArtifactName().toLowerCase().contains(searchText))
                .collect(Collectors.toList());

        artifactListTV.getItems().setAll(filteredArtifacts);
    }


    @FXML
    protected void onSaveArtifactBtnClick() {

        List<Artifact> artifacts=JSONFileHandler.getArtifactsFromJSONFile(Utils.dbFile);

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
        artifact.setImagePath(selectedImagePath);


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

        JSONFileHandler.saveArtifacts(artifacts, Utils.dbFile);
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setContentText("Artifact Saved with Id: "+artifact.getArtifactId());
        a.show();

        // open list screen after save
        onDisplayArtifactsBtnClick();


    }

    @FXML
    protected void onSaveContextPathBtnClick() {
        Utils.properties = new Properties();
        Path propFile = Paths.get(Utils.getPropsFile());
        try {
            Utils.properties.load(Files.newBufferedReader(propFile));
            Utils.properties.setProperty("context.path", contextPathTf.getText());
            Utils.properties.store(Files.newBufferedWriter(propFile), null);

            Files.createDirectories(Paths.get(Utils.properties.getProperty("context.path")));
            Files.createFile(Paths.get(Utils.properties.getProperty("context.path")+"/ArtifactManagerDB.json"));

            contextPathLbl.setText("Context Path is set");
            contextPathTf.setVisible(false);
            saveContextPathBtn.setVisible(false);
        } catch (IOException e) {
            Utils.properties=null;
        }
        if(Utils.properties!=null){

        }

    }





    @FXML
    protected void onSelectImageBtnClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Artifact Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            selectedImagePath = selectedFile.getAbsolutePath();
            selectedImagePathLbl.setText(selectedImagePath);
        }
    }

    public void fillArtifactFormForEdit(Artifact artifact) {
        artifactNameTf.setText(artifact.getArtifactName());
        civilizationTf.setText(artifact.getCivilization());
        disLocationTf.setText(artifact.getDiscoveryLocation());
        currPlaceTf.setText(artifact.getCurrentLocation());
        weightTf.setText(String.valueOf(artifact.getWeight()));

        compositionCb.getSelectionModel().select(artifact.getComposition());
        categoryCb.getSelectionModel().select(artifact.getCategory());
        tagsCC.getSelectionModel().select(artifact.getTags());

        // Date
        disDateDp.setValue(artifact.getDiscoverydate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

        // Dimensions
        if (artifact.getDimensions() != null) {
            Dimension dim = artifact.getDimensions();
            dimensionsTf.setText(dim.getWidth() + "," + dim.getHeight() + "," + dim.getLength());
        }

        selectedImagePath = artifact.getImagePath();
        if (selectedImagePathLbl != null) selectedImagePathLbl.setText(selectedImagePath);

        // delete artifact before save (to save with Same Id)
        List<Artifact> artifacts = JSONFileHandler.getArtifactsFromJSONFile(Utils.dbFile);
        artifacts.removeIf(a -> a.getArtifactId().equals(artifact.getArtifactId()));
        JSONFileHandler.saveArtifacts(artifacts, Utils.dbFile);
    }

    @FXML
    private void onHelpBtnClick() {
        String manual =
                "📘 Artifact Manager – User Manual\n\n" +
                        "🏁 Getting Started:\n" +
                        "- Click 'New Artifact' to add a new entry.\n" +
                        "- Click 'Display Artifacts' to view, edit, or delete artifacts.\n\n" +

                        "➕ Adding a New Artifact:\n" +
                        "- Fill in all the fields: name, civilization, location, date, etc.\n" +
                        "- Use commas for dimensions (e.g. 10,20,30).\n" +
                        "- Click 'Save' to store the artifact.\n\n" +

                        "📋 Viewing Artifacts:\n" +
                        "- See all artifacts in a table view.\n" +
                        "- Use 'Edit' to modify the selected item.\n" +
                        "- Use 'Delete' to remove the selected item.\n\n" +

                        "🆘 Need Help?\n" +
                        "- Ask Mr. Çağkan 😎";

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Help Manual");
        alert.setHeaderText("How to use Artifact Manager");
        alert.setContentText(manual);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE); // Tüm içeriği göstermek için
        alert.showAndWait();
    }

    @FXML
    private void onListByTagBtnClick() {
        try {
            File dbFile = new File(System.getProperty("user.home") + "/.ArtifactManager/ArtifactManagerDB.json");
            List<Artifact> artifacts = JSONFileHandler.getArtifactsFromJSONFile(dbFile);

            // Seçilen tag'leri al (örneğin ChoiceBox ile)
            List<String> selectedTags = List.of("sword", "necklace"); // örnek olarak sabit

            List<Artifact> filtered = artifacts.stream()
                    .filter(a -> selectedTags.contains(a.getTags()))
                    .collect(Collectors.toList());

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("artifactmanager-listartifacts-view.fxml"));
            Parent root = fxmlLoader.load();
            com.ieu.ce216.group7.artifactmanager.ArtifactListController controller = fxmlLoader.getController();
            controller.populateTable(filtered);

            Stage stage = new Stage();
            stage.setTitle("Filtered Artifacts");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }








}
