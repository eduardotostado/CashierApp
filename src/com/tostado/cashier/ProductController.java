package com.tostado.cashier;

import com.jfoenix.controls.JFXComboBox;
import com.tostado.cashier.entities.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProductController extends Controller implements Initializable {

    @FXML private TableColumn<Product, String> column1;
    @FXML private TableColumn<Product, BigDecimal> column2;
    @FXML private TableColumn<Product, Integer> column3;
    @FXML private TableColumn<Product, String> column4;

    @FXML private TableView<Product> tableView;

    @FXML private TextField textfield1;
    @FXML private TextField textfield2;
    @FXML private TextField textfield3;
    @FXML private JFXComboBox<Store> comboBox1;

    private ObservableList<Product> observableList;
    private ObservableList<Store> storeObservableList;

    private Product selectedObject;

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
                textfield2.setText(selectedObject.getPrice().toString());
                textfield3.setText(Integer.toString(selectedObject.getStock()));

                for(Store store : storeObservableList) {
                    if (selectedObject.getStoreID() == store.getID()){
                        comboBox1.getSelectionModel().select(store);
                    }
                }
            }
        });

        column1.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        column2.setCellValueFactory(new PropertyValueFactory<Product, BigDecimal>("price"));
        column3.setCellValueFactory(new PropertyValueFactory<Product, Integer>("stock"));
        column4.setCellValueFactory(new PropertyValueFactory<Product, String>("storeName"));
    }

    public void addButtonPushed() {
        if(comboBox1.getSelectionModel().getSelectedIndex() != -1) {
            if(!textfield1.getText().isEmpty()) {
                if (!textfield2.getText().isEmpty()) {
                    if (!textfield3.getText().isEmpty()) {
                        String tempName = textfield1.getText();
                        BigDecimal tempPrice = new BigDecimal(textfield2.getText());
                        int tempStock = Integer.parseInt(textfield3.getText());
                        int tempStoreID = comboBox1.getSelectionModel().getSelectedItem().getID();
                        Product temp = new Product(-1, tempName, tempPrice, tempStock, tempStoreID);
                        temp.setStoreName(comboBox1.getSelectionModel().getSelectedItem().getName());

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
                    } else {
                        failureAlert.setContentText("Please insert a valid stock.");
                        failureAlert.showAndWait();
                    }
                } else {
                    failureAlert.setContentText("Please insert a valid price.");
                    failureAlert.showAndWait();
                }
            } else {
                failureAlert.setContentText("Please insert a product name.");
                failureAlert.showAndWait();
            }
        } else {
            failureAlert.setContentText("Please select a store.");
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

    public void editButtonPushed() throws NoSuchAlgorithmException {
        if (tableView.getSelectionModel().getSelectedIndex() != -1) {
            if(comboBox1.getSelectionModel().getSelectedIndex() != -1) {
                if(!textfield1.getText().isEmpty()) {
                    if (!textfield2.getText().isEmpty()) {
                        if (!textfield3.getText().isEmpty()) {
                            String tempName = textfield1.getText();
                            BigDecimal tempPrice = new BigDecimal(textfield2.getText());
                            int tempStock = Integer.parseInt(textfield3.getText());
                            int tempStoreID = comboBox1.getSelectionModel().getSelectedItem().getID();

                            result = confirmationAlert.showAndWait();
                            if (result.get() == ButtonType.OK) {
                                if (selectedObject.edit(tempName, tempPrice, tempStock, tempStoreID)) {
                                    clearTextBox();
                                    loadData();

                                    successAlert.setContentText("Record successfully updated");
                                    successAlert.showAndWait();
                                } else {
                                    failureAlert.setContentText("There was an error updating the record to the database.");
                                    failureAlert.showAndWait();
                                }
                            }
                        } else {
                            failureAlert.setContentText("Please insert a valid stock.");
                            failureAlert.showAndWait();
                        }
                    } else {
                        failureAlert.setContentText("Please insert a valid price.");
                        failureAlert.showAndWait();
                    }
                } else {
                    failureAlert.setContentText("Please insert a product name.");
                    failureAlert.showAndWait();
                }
            } else {
                failureAlert.setContentText("Please select a store.");
                failureAlert.showAndWait();
            }
        } else {
            failureAlert.setContentText("Please select a valid record to update.");
            failureAlert.showAndWait();
        }
    }

    public void clearTextBox(){
        textfield1.clear();
        textfield2.clear();
        textfield3.clear();
        comboBox1.getSelectionModel().clearSelection();
    }

    public void loadData() {
        ResultSet resultSet = null;

        try {
            // Load the User data to the table view
            statement = "SELECT Product.ID, Product.name, price, stock, storeID, Store.name FROM Product JOIN Store on storeID = Store.ID";
            conn = DriverManager.getConnection(connectionURL, connectionUser, connectionPassword);
            stmt = conn.prepareStatement(statement);
            resultSet = stmt.executeQuery();
            if (resultSet != null) {
                observableList = FXCollections.observableArrayList();
                while (resultSet.next()) {
                    Product temp = new Product(resultSet.getInt("Product.ID"), resultSet.getString("Product.name"), resultSet.getBigDecimal("price"), resultSet.getInt("stock"), resultSet.getInt("storeID"));
                    temp.setStoreName(resultSet.getString("Store.name"));
                    observableList.add(temp);
                }
                if (!observableList.isEmpty())
                    tableView.setItems(observableList);
                else
                    tableView.getItems().clear();
            }

            // Load the Employee data to the ComboBox

            statement = "SELECT ID, name FROM Store";
            resultSet = null;
            stmt = conn.prepareStatement(statement);
            resultSet = stmt.executeQuery();
            if (resultSet != null) {
                storeObservableList = FXCollections.observableArrayList();
                while (resultSet.next()) {
                    Store tempStore = new Store(resultSet.getInt("ID"), resultSet.getString("name"));
                    storeObservableList.add(tempStore);
                }
                if (!storeObservableList.isEmpty())
                    comboBox1.setItems(storeObservableList);
                else
                    comboBox1.getItems().clear();
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

