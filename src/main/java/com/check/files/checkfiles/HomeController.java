package com.check.files.checkfiles;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;


public class HomeController {


    @FXML
    private Button btnFolderA;
    @FXML
    private Button btnFolderB;

    private List<String> listA;
    private List<String> listB;
    @FXML
    private TextField extensionA;
    @FXML
    private TextField extensionB;
    @FXML
    private TableView<Information> tableB;
    @FXML
    private TableView<Information> tableA;
    @FXML
    private Button checkBtn;
    @FXML
    private TableColumn<Information, String> columnA;
    @FXML
    private TableColumn<Information, String> columnB;

    @FXML
    private Label labelA;
    @FXML
    private Label labelB;

    private boolean checkBtnA = false;
    private boolean checkBtnB = false;


    @FXML
    public void chooseFolderA(ActionEvent actionEvent) {
        listA = getListofFiles(extensionA);
        checkBtnA = listA != null;
    }

    @FXML
    public void chooseFolderB(ActionEvent actionEvent) {
        listB = getListofFiles(extensionB);
        checkBtnB = listB != null;
    }

    private List<String> getListofFiles(TextField extension) {
        DirectoryChooser directoryChooserB = new DirectoryChooser();
        File fileB = directoryChooserB.showDialog(new Stage());
        try (Stream<Path> files = Files.walk(Paths.get(fileB.getAbsolutePath()))) {
            return files.filter(f -> f.getFileName().toString().endsWith(extension.getText().trim()))
                    .map(f -> {
                        int position = f.getFileName().toString().lastIndexOf(".");
                        return f.getFileName().toString().substring(0, position);
                    })
                    .toList();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Collections.emptyList();
        }

    }

    @FXML
    public void onCheckBtn(ActionEvent actionEvent) {

        if (checkBtnA) {

            List<Information> checkA = listA.stream()
                    .filter(e -> !listB.contains(e))
                    .map(Information::new)
                    .toList();

            if (checkA != null) {

                ObservableList<Information> itemsA = FXCollections.observableArrayList();
                itemsA.setAll(checkA);

                columnA.setCellValueFactory(new PropertyValueFactory<Information, String>("name"));
                tableA.setItems(itemsA);

                labelA.setText("Files count: " + listA.size());
            } else {
                labelA.setText("No Files which are not in the next folder");
            }

        } else {
            labelA.setText("You didnt choose the folder");
        }

        if (checkBtnB) {
            List<Information> checkB = listB.stream()
                    .filter(e -> !listA.contains(e))
                    .map(Information::new)
                    .toList();

            if (checkB != null) {
                ObservableList<Information> itemsB = FXCollections.observableArrayList();
                itemsB.setAll(checkB);

                columnB.setCellValueFactory(new PropertyValueFactory<Information, String>("name"));

                tableB.setItems(itemsB);
                labelB.setText("Files count: " + listB.size());
            } else {
                labelB.setText("No Files which are not in the next folder");
            }
        } else {
            labelB.setText("You didnt choose the folder");
        }

    }
}