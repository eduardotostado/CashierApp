package com.tostado.cashier;

import com.tostado.cashier.entities.Controller;
import com.tostado.cashier.entities.Store;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class StoreController extends Controller implements Initializable {

    @FXML private TableColumn<Store, String> column1;
    @FXML private TableColumn<Store, String> column2;
    @FXML private TableColumn<Store, String> column3;

    @FXML private TableView<Store> tableView;

    @FXML private TextField textfield1;
    @FXML private TextField textfield2;
    @FXML private TextField textfield3;

    private ObservableList<Store> observableList;

    private Store selectedObject;

    private Alert successAlert;
    private Alert failureAlert;
    private Alert confirmationAlert;
    Optional<ButtonType> result;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Success");
        successAlert.setHeaderText(null);
        failureAlert = new Alert(Alert.AlertType.ERROR);
        failureAlert.setTitle("Failure");
        failureAlert.setHeaderText(null);
        confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Are you sure you want to perform this operation?");

        loadData();

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {

                selectedObject = tableView.getSelectionModel().getSelectedItem();

                textfield1.setText(selectedObject.getName());
                textfield2.setText(selectedObject.getAddress());
                textfield3.setText(selectedObject.getPhoneNumber());
            }
        });

        column1.setCellValueFactory(new PropertyValueFactory<Store, String>("name"));
        column2.setCellValueFactory(new PropertyValueFactory<Store, String>("address"));
        column3.setCellValueFactory(new PropertyValueFactory<Store, String>("phoneNumber"));
    }

    public void addButtonPushed() {
        if(!textfield1.getText().isEmpty()) {
            String temp1 = textfield1.getText();
            String temp2 = textfield2.getText();
            String temp3 = textfield3.getText().replaceAll("[\\s\\-()]", "");

            Store temp = new Store(-1, temp1, temp2, temp3);

            result = confirmationAlert.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (temp.add()) {
                    clearTextBox();
                    loadData();

                    successAlert.setContentText("Record successfully added");
                    successAlert.showAndWait();
                } else {
                    failureAlert.setContentText("There was an error adding the record to the database.");
                    failureAlert.showAndWait();
                }
            }
        }else{
            failureAlert.setContentText("Please enter a store name.");
            failureAlert.showAndWait();
        }
    }

    public void deleteButtonPushed() {
        selectedObject = tableView.getSelectionModel().getSelectedItem();

        if (selectedObject != null) {
            result = confirmationAlert.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (selectedObject.delete()) {
                    loadData();

                    clearTextBox();

                    successAlert.setContentText("Record successfully deleted");
                    successAlert.showAndWait();
                } else {
                    failureAlert.setContentText("There was an error deleting the record from the database.");
                    failureAlert.showAndWait();
                }
            }
        } else {
            failureAlert.setContentText("Please select a valid record.");
            failureAlert.showAndWait();
        }
    }

    public void editButtonPushed(){
        if (tableView.getSelectionModel().getSelectedIndex() != -1) {
            if(!textfield1.getText().isEmpty()){
                result = confirmationAlert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    String temp1 = textfield1.getText();
                    String temp2 = textfield2.getText();
                    String temp3 = textfield3.getText();
                    if (selectedObject.edit(temp1, temp2, temp3)) {
                        clearTextBox();
                        loadData();

                        successAlert.setContentText("Record successfully updated");
                        successAlert.showAndWait();
                    } else {
                        failureAlert.setContentText("There was an error updating the record to the database.");
                        failureAlert.showAndWait();
                    }
                }
            }
        }
    }

    public void clearTextBox(){
        textfield1.clear();
        textfield2.clear();
        textfield3.clear();
    }

    public void loadData() {
        ResultSet resultSet = null;

        try {
            statement = "SELECT * FROM Store";
            conn = DriverManager.getConnection(connectionURL, connectionUser, connectionPassword);
            stmt = conn.prepareStatement(statement);
            resultSet = stmt.executeQuery();
            if (resultSet != null) {
                observableList = FXCollections.observableArrayList();
                while (resultSet.next()) {
                    Store temp = new Store(resultSet.getInt("ID"), resultSet.getString("name"), resultSet.getString("address"), resultSet.getString("phoneNumber"));
                    observableList.add(temp);
                }
                if (!observableList.isEmpty())
                    tableView.setItems(observableList);
                else
                    tableView.getItems().clear();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) { /* ignored */}
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) { /* ignored */}
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) { /* ignored */}
            }
        }
    }
}

