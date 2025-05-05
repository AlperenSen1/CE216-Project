package com.ieu.ce216.group7.artifactmanager;

import com.ieu.ce216.group7.artifactmanager.model.Artifact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ArtifactListController {

    @FXML
    private TableView<Artifact> artifactTable;

    @FXML
    private TableColumn<Artifact, String> idCol;
    @FXML
    private TableColumn<Artifact, String> nameCol;
    @FXML
    private TableColumn<Artifact, String> categoryCol;
    @FXML
    private TableColumn<Artifact, String> civilCol;
    @FXML
    private TableColumn<Artifact, String> compositionCol;
    @FXML
    private TableColumn<Artifact, String> disLocCol;
    @FXML
    private TableColumn<Artifact, String> currPlaceCol;

    @FXML
    private ListView<String> tagFilterList;
    private List<Artifact> allArtifacts;


    @FXML
    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("artifactId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("artifactName"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        civilCol.setCellValueFactory(new PropertyValueFactory<>("civilization"));
        compositionCol.setCellValueFactory(new PropertyValueFactory<>("composition"));
        disLocCol.setCellValueFactory(new PropertyValueFactory<>("discoveryLocation"));
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
        ObservableList<Artifact> data = FXCollections.observableArrayList(artifactList);
        artifactTable.setItems(data);
    }

    @FXML
    private void onDeleteArtifactBtnClick() {
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
        System.out.println("Search butonuna tıklandı (şu an işlevsiz).");
    }
}
