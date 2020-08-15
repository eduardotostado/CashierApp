package com.tostado.cashier;

import com.jfoenix.controls.JFXComboBox;
import com.tostado.cashier.entities.Controller;
import com.tostado.cashier.entities.Employee;
import com.tostado.cashier.entities.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class UserController extends Controller implements Initializable {

    @FXML private TableColumn<User, String> column1;
    @FXML private TableColumn<User, String> column2;
    @FXML private TableColumn<User, String> column3;

    @FXML private TableView<User> tableView;

    @FXML private TextField textfield1;
    @FXML private PasswordField passwordField1;
    @FXML private PasswordField passwordField2;
    @FXML private JFXComboBox<Employee> comboBox1;

    private ObservableList<User> observableList;
    private ObservableList<Employee> employeeObservableList;

    private User selectedObject;

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

        // Loads the data into the TableView and ComboBox
        loadData();

        // When a row is selected, populates the text fields with the information from the row.
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                //populate the textfields with the info for edit
                selectedObject = tableView.getSelectionModel().getSelectedItem();

                // String text field:      textfield1.setText(selectedObject.getAtributte());
                // Integer text field:     textfield2.setText(Integer.toString(selectedObject.getAtributte()));
                // BigDecimal text field:  textfield3.setText(selectedObject.getAtributte().toString());

                textfield1.setText(selectedObject.getUsername());
                for(Employee employee : employeeObservableList) {
                    if (selectedObject.getEmployeeID() == employee.getID()){
                        comboBox1.getSelectionModel().select(employee);
                    }
                }
            }
        });

        column1.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        column2.setCellValueFactory(new PropertyValueFactory<User, String>("employeeName"));
        column3.setCellValueFactory(new PropertyValueFactory<User, String>("employeeEmail"));
    }

    public void addButtonPushed() {
        if(comboBox1.getSelectionModel().getSelectedIndex() != -1) {
            if(!textfield1.getText().isEmpty()) {
                if (!passwordField1.getText().equals("") && !passwordField2.getText().equals("")) {
                    if (passwordField1.getText().equals(passwordField2.getText())) {
                        String tempUsername = textfield1.getText();
                        int tempEmployeeID = comboBox1.getSelectionModel().getSelectedItem().getID();
                        String tempPassword = passwordField1.getText();
                        User temp = new User(tempUsername, tempPassword, tempEmployeeID);

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
                        failureAlert.setContentText("Passwords do not match.");
                        failureAlert.showAndWait();
                    }
                } else {
                    failureAlert.setContentText("Please enter a password.");
                    failureAlert.showAndWait();
                }
            } else {
                failureAlert.setContentText("Please insert a username.");
                failureAlert.showAndWait();
            }
        } else {
            failureAlert.setContentText("Please select an employee.");
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
                if(textfield1.getText().equals(selectedObject.getUsername())) {
                    if (!passwordField1.getText().equals("") && !passwordField2.getText().equals("")) {
                        if(passwordField1.getText().equals(passwordField2.getText())){
                            int tempEmployeeID = comboBox1.getSelectionModel().getSelectedItem().getID();
                            String tempPassword = passwordField1.getText();
                            result = confirmationAlert.showAndWait();
                            if (result.get() == ButtonType.OK) {
                                if (selectedObject.edit(tempPassword, tempEmployeeID)) {
                                    clearTextBox();
                                    loadData();

                                    successAlert.setContentText("Record successfully updated");
                                    successAlert.showAndWait();
                                } else {
                                    failureAlert.setContentText("There was an error updating the record to the database.");
                                    failureAlert.showAndWait();
                                }
                            }
                        }else {
                            failureAlert.setContentText("Passwords do not match.");
                            failureAlert.showAndWait();
                        }
                    } else
                        {
                            failureAlert.setContentText("Please enter a password.");
                            failureAlert.showAndWait();
                        }
                } else {
                    failureAlert.setContentText("You can't edit the username for an account. Please enter the same username to the text field.");
                    failureAlert.showAndWait();
                }
            } else {
                failureAlert.setContentText("Please select an employee.");
                failureAlert.showAndWait();
            }
        } else {
            failureAlert.setContentText("Please select a valid record to update.");
            failureAlert.showAndWait();
        }
    }

    public void clearTextBox(){
        textfield1.clear();
        passwordField1.clear();
        passwordField2.clear();
        comboBox1.getSelectionModel().clearSelection();
    }

    public void loadData() {
        ResultSet resultSet = null;

        try {
            // Load the User data to the table view
            statement = "SELECT username, employeeID, firstName, lastName, email FROM User JOIN Employee on employeeID = ID";
            conn = DriverManager.getConnection(connectionURL, connectionUser, connectionPassword);
            stmt = conn.prepareStatement(statement);
            resultSet = stmt.executeQuery();
            if (resultSet != null) {
                observableList = FXCollections.observableArrayList();
                while (resultSet.next()) {
                    User temp = new User(resultSet.getString("username"), resultSet.getInt("employeeID"));
                    temp.setEmployeeName(resultSet.getString("firstName") + " " + resultSet.getString("lastName"));
                    temp.setEmployeeEmail(resultSet.getString("email"));
                    observableList.add(temp);
                }
                if (!observableList.isEmpty())
                    tableView.setItems(observableList);
                else
                    tableView.getItems().clear();
            }

            // Load the Employee data to the ComboBox

            statement = "SELECT ID, firstName, lastName, email FROM Employee";
            resultSet = null;
            stmt = conn.prepareStatement(statement);
            resultSet = stmt.executeQuery();
            if (resultSet != null) {
                employeeObservableList = FXCollections.observableArrayList();
                while (resultSet.next()) {
                    Employee tempEmployee = new Employee(resultSet.getInt("ID"), resultSet.getString("firstName"), resultSet.getString("lastName"), resultSet.getString("email"));
                    employeeObservableList.add(tempEmployee);
                }
                if (!employeeObservableList.isEmpty())
                    comboBox1.setItems(employeeObservableList);
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

