package com.ieu.ce216.group7.artifactmanager;

import com.ieu.ce216.group7.artifactmanager.model.Artifact; // Model class representing an artifact
import javafx.collections.FXCollections; // Utility class for creating ObservableList
import javafx.collections.ObservableList; // List that notifies listeners of changes
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*; // JavaFX UI controls like Button, TableView, etc.
import javafx.scene.control.cell.PropertyValueFactory; // JavaFX UI controls like Button, TableView, etc.
import javafx.stage.FileChooser; // Used to open file dialog for selecting files
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ArtifactListController {

    @FXML
    // Table view to display artifact items
    private TableView<Artifact> artifactTable;

    @FXML
    // Table column for displaying a specific artifact field
    private TableColumn<Artifact, String> idCol;
    @FXML
    // Table column for displaying a specific artifact field
    private TableColumn<Artifact, String> nameCol;
    @FXML
    // Table column for displaying a specific artifact field
    private TableColumn<Artifact, String> categoryCol;
    @FXML
    // Table column for displaying a specific artifact field
    private TableColumn<Artifact, String> civilCol;
    @FXML
    // Table column for displaying a specific artifact field
    private TableColumn<Artifact, String> compositionCol;
    @FXML
    // Table column for displaying a specific artifact field
    private TableColumn<Artifact, String> disLocCol;
    @FXML
    // Table column for displaying a specific artifact field
    private TableColumn<Artifact, String> currPlaceCol;

    @FXML
    private ListView<String> tagFilterList;
    private List<Artifact> allArtifacts;

    public TextField artifactIdTf;
    public TextField artifactNameTf;
    public TextField disLocationTf;
    public TextField civilizationTf;
    public ComboBox categoryCb;
    public ComboBox compositionCb;
    public DatePicker disDateDp;
    public TextField currPlaceTf;
    public TextField dimensionsTf;
    public TextArea weightTf;


    @FXML
    // Method called automatically to initialize the controller and load data
    public void initialize() {
        // Binds table columns to Artifact properties
        idCol.setCellValueFactory(new PropertyValueFactory<>("artifactId"));
        // Binds table columns to Artifact properties
        nameCol.setCellValueFactory(new PropertyValueFactory<>("artifactName"));
        // Binds table columns to Artifact properties
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        // Binds table columns to Artifact properties
        civilCol.setCellValueFactory(new PropertyValueFactory<>("civilization"));
        // Binds table columns to Artifact properties
        compositionCol.setCellValueFactory(new PropertyValueFactory<>("composition"));
        // Binds table columns to Artifact properties
        disLocCol.setCellValueFactory(new PropertyValueFactory<>("discoveryLocation"));
        // Binds table columns to Artifact properties
        currPlaceCol.setCellValueFactory(new PropertyValueFactory<>("currentLocation"));

        artifactTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        //dbFile = new File(System.getProperty("user.home") + "/.ArtifactManager/ArtifactManagerDB.json");
        allArtifacts = JSONFileHandler.getArtifactsFromJSONFile(Utils.dbFile);
        artifactTable.getItems().addAll(allArtifacts);

        tagFilterList.getItems().addAll("hellenistic", "neoclassical", "contemporary", "sword", "bow", "axe", "necklace", "ring");
        tagFilterList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public void populateTable(List<Artifact> artifactList) {
        this.allArtifacts = artifactList;
        // List to store and manage artifacts in the table
        ObservableList<Artifact> data = FXCollections.observableArrayList(artifactList);
        artifactTable.setItems(data);
    }

    @FXML
    private void onDeleteArtifactBtnClick() {
        // List to store and manage artifacts in the table
        ObservableList<Artifact> selected = artifactTable.getSelectionModel().getSelectedItems();

        if (selected.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Lütfen silmek için en az bir artifact seçin.", ButtonType.OK);
            alert.show();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Seçili artifact(ler) silinsin mi?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait();

        if (confirm.getResult() == ButtonType.YES) {
            List<Artifact> artifacts = JSONFileHandler.getArtifactsFromJSONFile(Utils.dbFile);
            for (Artifact a : selected) {
                artifacts.removeIf(x -> x.getArtifactId().equals(a.getArtifactId()));
            }
            JSONFileHandler.saveArtifacts(artifacts, Utils.dbFile);
            artifactTable.getItems().removeAll(selected);
        }
    }

    @FXML
    private void onEditArtifactBtnClick() {
        Artifact selectedArtifact = artifactTable.getSelectionModel().getSelectedItem();

        if (selectedArtifact == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Lütfen düzenlemek için bir artifact seçin.", ButtonType.OK);
            alert.show();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("artifactmanager-newartifact-view.fxml"));
            Parent root = loader.load();

            ArtifactManagerController controller = loader.getController();
            controller.fillArtifactFormForEdit(selectedArtifact);

            Stage stage = new Stage();
            stage.setTitle("Edit Artifact");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onListByTagBtnClick() {
        allArtifacts = JSONFileHandler.getArtifactsFromJSONFile(Utils.dbFile);
        ObservableList<String> selectedTags = tagFilterList.getSelectionModel().getSelectedItems();

        if (selectedTags.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please select at least one tag.", ButtonType.OK);
            alert.show();
            return;
        }

        List<Artifact> filtered = allArtifacts.stream()
                .filter(a -> selectedTags.contains(a.getTags()))
                .collect(Collectors.toList());

        populateTable(filtered);
    }

    @FXML
    private void onExportJsonBtnClick() {
        // List to store and manage artifacts in the table
        ObservableList<Artifact> selected = artifactTable.getSelectionModel().getSelectedItems();

        if (selected.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please select at least one artifact to export.", ButtonType.OK);
            alert.show();
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export JSON File");
        fileChooser.setInitialFileName("exported_artifacts.json");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON files", "*.json"));
        File selectedFile = fileChooser.showSaveDialog(artifactTable.getScene().getWindow());

        if (selectedFile != null) {
            JSONFileHandler.saveArtifacts(selected, selectedFile);
            Alert success = new Alert(Alert.AlertType.INFORMATION, "Artifacts exported successfully.", ButtonType.OK);
            success.show();
        }
    }

    @FXML
    private void onImportJsonBtnClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import JSON File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON files", "*.json"));
        File selectedFile = fileChooser.showOpenDialog(artifactTable.getScene().getWindow());

        if (selectedFile != null) {
            List<Artifact> imported = JSONFileHandler.getArtifactsFromJSONFile(selectedFile);
            List<Artifact> current = JSONFileHandler.getArtifactsFromJSONFile(Utils.dbFile);

            current.addAll(imported);
            JSONFileHandler.saveArtifacts(current, Utils.dbFile);

            populateTable(current);
        }
    }

    @FXML
    protected void onSearchArtifactBtnClick() {
        allArtifacts = JSONFileHandler.getArtifactsFromJSONFile(Utils.dbFile);
        String selCategory = null;
        try{
            selCategory=(String)categoryCb.getSelectionModel().getSelectedItem();
        } catch (Exception e) {
        }
        String selComposition = null;
        try{
            selComposition=(String)compositionCb.getSelectionModel().getSelectedItem();
        } catch (Exception e) {
        }
        String finalSelCategory = selCategory;
        String finalSelComposition = selComposition;
        List<Artifact> filtered = allArtifacts.stream()
                .filter(a -> (artifactIdTf.getText()!=null && artifactIdTf.getText().trim().length()>0?a.getArtifactId().contains(artifactIdTf.getText().trim()):true)
                        && (artifactNameTf.getText()!=null && artifactNameTf.getText().trim().length()>0?a.getArtifactName().contains(artifactNameTf.getText().trim()):true)
                        && (civilizationTf.getText()!=null && civilizationTf.getText().trim().length()>0?a.getCivilization().contains(civilizationTf.getText().trim()):true)
                        && (currPlaceTf.getText()!=null && currPlaceTf.getText().trim().length()>0?a.getCurrentLocation().contains(currPlaceTf.getText().trim()):true)
                        && (disLocationTf.getText()!=null && disLocationTf.getText().trim().length()>0?a.getDiscoveryLocation().contains(disLocationTf.getText().trim()):true)
                        && (finalSelCategory !=null && finalSelCategory.trim().length()>0?a.getCategory().contains(finalSelCategory.trim()):true)
                        && (finalSelComposition !=null && finalSelComposition.trim().length()>0?a.getComposition().contains(finalSelComposition.trim()):true)
                )
                .collect(Collectors.toList());

        populateTable(filtered);
    }
}
