package com.tostado.cashier;

import com.jfoenix.controls.JFXComboBox;
import com.tostado.cashier.entities.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class EmployeeController extends Controller implements Initializable {

    @FXML private TableColumn<Employee, String> column1;
    @FXML private TableColumn<Employee, String> column2;
    @FXML private TableColumn<Employee, String> column3;
    @FXML private TableColumn<Employee, String> column4;
    @FXML private TableColumn<Employee, String> column5;
    @FXML private TableColumn<Employee, String> column6;

    @FXML private TableView<Employee> tableView;

    @FXML private TextField textfield1;
    @FXML private TextField textfield2;
    @FXML private TextField textfield3;
    @FXML private TextField textfield4;
    @FXML private TextField textfield5;
    @FXML private Label managerLabel;
    @FXML private CheckBox checkBox;
    @FXML private JFXComboBox<Store> comboBox1;

    private ObservableList<Employee> observableList;
    private ObservableList<Store> storeObservableList;

    private Employee selectedObject;

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

                textfield1.setText(selectedObject.getFirstName());
                textfield2.setText(selectedObject.getLastName());
                textfield3.setText(selectedObject.getEmail());
                textfield4.setText(selectedObject.getAddress());
                textfield5.setText(selectedObject.getPhoneNumber());
                checkBox.setSelected(selectedObject.getManagerFlag());

                for(Store store : storeObservableList) {
                    if (selectedObject.getStoreID() == store.getID()){
                        comboBox1.getSelectionModel().select(store);
                    }
                }
            }
        });

        column1.setCellValueFactory(new PropertyValueFactory<Employee, String>("firstName"));
        column2.setCellValueFactory(new PropertyValueFactory<Employee, String>("lastName"));
        column3.setCellValueFactory(new PropertyValueFactory<Employee, String>("email"));
        column4.setCellValueFactory(new PropertyValueFactory<Employee, String>("address"));
        column5.setCellValueFactory(new PropertyValueFactory<Employee, String>("phoneNumber"));
        column6.setCellValueFactory(new PropertyValueFactory<Employee, String>("storeName"));
    }

    public void manager(){
        if(!this.managerFlag){
            checkBox.setVisible(false);
            managerLabel.setVisible(false);
        }else {
            checkBox.setVisible(true);
            managerLabel.setVisible(true);
        }
    }

    public void addButtonPushed() {
        if(comboBox1.getSelectionModel().getSelectedIndex() != -1) {
            if(!textfield1.getText().isEmpty()) {
                if (!textfield2.getText().isEmpty()) {
                    if (!textfield5.getText().isEmpty()) {
                        String tempFirstName = textfield1.getText();
                        String tempLastName = textfield2.getText();
                        String tempEmail = textfield3.getText();
                        String tempAddress = textfield4.getText();
                        String tempPhoneNumber = textfield5.getText();
                        int tempStoreID = comboBox1.getSelectionModel().getSelectedItem().getID();
                        boolean tempManager = checkBox.isSelected();

                        Employee temp = new Employee(-1, tempFirstName, tempLastName, tempEmail, tempAddress, tempPhoneNumber, tempStoreID, tempManager);
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
                        failureAlert.setContentText("Please insert a valid phone number.");
                        failureAlert.showAndWait();
                    }
                } else {
                    failureAlert.setContentText("Please enter a last name.");
                    failureAlert.showAndWait();
                }
            } else {
                failureAlert.setContentText("Please enter a name.");
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
                        if (!textfield5.getText().isEmpty()) {
                            String tempFirstName = textfield1.getText();
                            String tempLastName = textfield2.getText();
                            String tempEmail = textfield3.getText();
                            String tempAddress = textfield4.getText();
                            String tempPhoneNumber = textfield5.getText();
                            int tempStoreID = comboBox1.getSelectionModel().getSelectedItem().getID();
                            boolean tempManager = checkBox.isSelected();

                            result = confirmationAlert.showAndWait();
                            if (result.get() == ButtonType.OK) {
                                if (selectedObject.edit(tempFirstName, tempLastName, tempEmail, tempAddress, tempPhoneNumber, tempStoreID, tempManager)) {
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
                            failureAlert.setContentText("Please insert a valid phone number.");
                            failureAlert.showAndWait();
                        }
                    } else {
                        failureAlert.setContentText("Please enter a last name.");
                        failureAlert.showAndWait();
                    }
                } else {
                    failureAlert.setContentText("Please enter a name.");
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
        textfield4.clear();
        textfield5.clear();
        comboBox1.getSelectionModel().clearSelection();
        checkBox.setSelected(false);
    }

    public void loadData() {
        ResultSet resultSet = null;

        try {
            // Load the User data to the table view
            statement = "SELECT Employee.ID, firstName, lastName, email, Employee.address, Employee.phoneNumber, storeID, isManager, Store.name FROM Employee JOIN Store on storeID = Store.ID";
            conn = DriverManager.getConnection(connectionURL, connectionUser, connectionPassword);
            stmt = conn.prepareStatement(statement);
            resultSet = stmt.executeQuery();
            if (resultSet != null) {
                observableList = FXCollections.observableArrayList();
                while (resultSet.next()) {
                    Employee temp = new Employee(resultSet.getInt("Employee.ID"), resultSet.getString("firstName"), resultSet.getString("lastName"), resultSet.getString("email"), resultSet.getString("Employee.address"), resultSet.getString("Employee.phoneNumber"), resultSet.getInt("storeID"), resultSet.getBoolean("isManager"));
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

