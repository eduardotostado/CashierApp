package com.tostado.cashier;

import com.jfoenix.controls.JFXComboBox;
import com.tostado.cashier.entities.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

public class TransactionController extends Controller implements Initializable {



    @FXML private TableColumn<Transaction, String> column1;
    @FXML private TableColumn<Transaction, Integer> column2;
    @FXML private TableColumn<Transaction, BigDecimal> column3;
    @FXML private TableColumn<Transaction, String> column4;
    @FXML private TableColumn<Transaction, String> column5;
    @FXML private TableColumn<Transaction, String> column6;
    @FXML private TableColumn<Transaction, String> column7;

    @FXML private TableView<Transaction> tableView;

    @FXML private TextField textfield1;
    @FXML private JFXComboBox<Customer> comboBox2;
    @FXML private JFXComboBox<Product> comboBox1;

    private ObservableList<Transaction> observableList;
    private ObservableList<Product> productObservableList;
    private ObservableList<Customer> customerObservableList;

    private Transaction selectedObject;

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

        column1.setCellValueFactory(new PropertyValueFactory<Transaction, String>("productName"));
        column2.setCellValueFactory(new PropertyValueFactory<Transaction, Integer>("quantity"));
        column3.setCellValueFactory(new PropertyValueFactory<Transaction, BigDecimal>("total"));
        column4.setCellValueFactory(new PropertyValueFactory<Transaction, String>("date"));
        column5.setCellValueFactory(new PropertyValueFactory<Transaction, String>("employeeName"));
        column6.setCellValueFactory(new PropertyValueFactory<Transaction, String>("customerName"));
        column7.setCellValueFactory(new PropertyValueFactory<Transaction, String>("storeName"));
    }

    public void addButtonPushed() {
        if(comboBox1.getSelectionModel().getSelectedIndex() != -1) {
            if (!textfield1.getText().isEmpty()) {
                try {
                    conn = DriverManager.getConnection(connectionURL, connectionUser, connectionPassword);
                }catch (SQLException e){}

                int tempProductID = comboBox1.getSelectionModel().getSelectedItem().getID();
                int tempQuantity = Integer.parseInt(textfield1.getText());
                int tempEmployeeID = this.employeeID;
                int tempStoreID = getStoreID();
                int tempCustomerID;

                if(comboBox2.getSelectionModel().getSelectedIndex() != -1) {
                    tempCustomerID = comboBox2.getSelectionModel().getSelectedItem().getID();
                    try{conn.close();}catch(SQLException e){}

                    if (tempCustomerID != -1) {
                        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        LocalDateTime today = LocalDateTime.now();

                        String tempDate = dateTimeFormatter.format(today);

                        Transaction temp = new Transaction(-1, tempProductID, tempQuantity, new BigDecimal("0.00").setScale(2), comboBox1.getSelectionModel().getSelectedItem().getPrice(), tempEmployeeID, tempStoreID, tempCustomerID, tempDate);

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
                    }
                }else {
                    failureAlert.setContentText("Please select a customer.");
                    failureAlert.showAndWait();
                }
            } else {
                failureAlert.setContentText("Please insert quantity.");
                failureAlert.showAndWait();
            }
        }else {
            failureAlert.setContentText("Please select a product.");
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

    public int getStoreID(){
        ResultSet resultSet = null;
        int returnInt = 0;

        try {
            String tempStatement = "SELECT storeID FROM Employee JOIN Store on Employee.storeID = Store.ID WHERE Employee.ID = " + this.employeeID;
            statement = tempStatement;
            stmt = conn.prepareStatement(statement);
            resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                returnInt = resultSet.getInt("storeID");
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
        }

        return returnInt;
    }

    public String getStoreName(int ID){
        ResultSet resultSet = null;
        String resultString = "";

        try {
            String tempStatement = "SELECT name FROM Store WHERE ID = " + ID;
            statement = tempStatement;
            stmt = conn.prepareStatement(statement);
            resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                resultString = resultSet.getString("name");
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
        }

        return resultString;
    }

    public String getProductName(int ID){
        ResultSet resultSet = null;
        String resultString = "";

        try {
            String tempStatement = "SELECT name FROM Product WHERE ID = " + ID;
            statement = tempStatement;
            stmt = conn.prepareStatement(statement);
            resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                resultString = resultSet.getString("name");
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
        }

        return resultString;
    }

    public String getEmployeeName(int ID){
        ResultSet resultSet = null;
        String resultString = "";

        try {
            String tempStatement = "SELECT firstName, lastName FROM Employee WHERE ID = " + ID;
            statement = tempStatement;
            stmt = conn.prepareStatement(statement);
            resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                resultString = resultSet.getString("firstName") + " " + resultSet.getString("lastName");
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
        }

        return resultString;
    }

    public String getCustomerName(int ID){
        ResultSet resultSet = null;
        String resultString = "";

        try {
            String tempStatement = "SELECT firstName, lastName FROM Customer WHERE ID = " + ID;
            statement = tempStatement;
            stmt = conn.prepareStatement(statement);
            resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                resultString = resultSet.getString("firstName") + " " + resultSet.getString("lastName");
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
        }

        return resultString;
    }

    public void clearTextBox(){
        textfield1.clear();
        comboBox1.getSelectionModel().clearSelection();
        comboBox2.getSelectionModel().clearSelection();
    }

    public void loadData() {
        ResultSet resultSet = null;

        try {
            // Load the User data to the table view
            statement = "Select * FROM Transaction";
            conn = DriverManager.getConnection(connectionURL, connectionUser, connectionPassword);
            stmt = conn.prepareStatement(statement);
            resultSet = stmt.executeQuery();
            if (resultSet != null) {
                observableList = FXCollections.observableArrayList();
                while (resultSet.next()) {
                    int tempID = resultSet.getInt("ID");
                    int tempProductID = resultSet.getInt("productID");
                    int tempQuantity = resultSet.getInt("quantity");
                    int tempEmployeeID = resultSet.getInt("employeeID");
                    int tempStoreID = resultSet.getInt("storeID");
                    int tempCustomerID = resultSet.getInt("customerID");

                    Transaction temp = new Transaction(tempID, tempProductID, tempQuantity, resultSet.getBigDecimal("total"), null, tempEmployeeID, tempStoreID, tempCustomerID, resultSet.getString("date"));
                    temp.setProductName(getProductName(tempProductID));
                    temp.setEmployeeName(getEmployeeName(tempEmployeeID));
                    temp.setStoreName(getStoreName(tempStoreID));
                    temp.setCustomerName(getCustomerName(tempCustomerID));
                    observableList.add(temp);
                }
                if (!observableList.isEmpty())
                    tableView.setItems(observableList);
                else
                    tableView.getItems().clear();
            }

            // Load the Product data to the ComboBox

            statement = "SELECT * FROM Product";
            resultSet = null;
            stmt = conn.prepareStatement(statement);
            resultSet = stmt.executeQuery();
            if (resultSet != null) {
                productObservableList = FXCollections.observableArrayList();
                while (resultSet.next()) {
                    Product tempProduct = new Product(resultSet.getInt("ID"), resultSet.getString("name"), resultSet.getBigDecimal("price"), resultSet.getInt("stock"), resultSet.getInt("storeID"));
                    productObservableList.add(tempProduct);
                }
                if (!productObservableList.isEmpty())
                    comboBox1.setItems(productObservableList);
                else
                    comboBox1.getItems().clear();
            }

            statement = "SELECT * FROM Customer";
            resultSet = null;
            stmt = conn.prepareStatement(statement);
            resultSet = stmt.executeQuery();
            if (resultSet != null) {
                customerObservableList = FXCollections.observableArrayList();
                while (resultSet.next()) {
                    Customer tempCustomer = new Customer(resultSet.getInt("ID"), resultSet.getString("phoneNumber"), resultSet.getString("firstName"), resultSet.getString("lastName"), resultSet.getString("address"));
                    customerObservableList.add(tempCustomer);
                }
                if (!customerObservableList.isEmpty())
                    comboBox2.setItems(customerObservableList);
                else
                    comboBox2.getItems().clear();
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

