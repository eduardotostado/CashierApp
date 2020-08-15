package com.tostado.cashier;

import com.jfoenix.controls.JFXButton;
import com.tostado.cashier.entities.Controller;
import com.tostado.cashier.entities.ControllerType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController extends Controller implements Initializable {

    @FXML JFXButton usersButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void manager(){
        if(this.managerFlag){
            usersButton.setVisible(true);
        }else {
            usersButton.setVisible(false);
        }
    }

    public void customerPushed(ActionEvent event) throws IOException {
        loadScene(event, "Customer.fxml", this.employeeID, this.managerFlag, ControllerType.CUSTOMER);
    }

    public void employeePushed(ActionEvent event) throws IOException {
        loadScene(event, "Employee.fxml", this.employeeID, this.managerFlag, ControllerType.EMPLOYEE);
    }

    public void productPushed(ActionEvent event) throws IOException {
        loadScene(event, "Product.fxml", this.employeeID, this.managerFlag, ControllerType.PRODUCT);
    }

    public void storePushed(ActionEvent event) throws IOException {
        loadScene(event, "Store.fxml", this.employeeID, this.managerFlag, ControllerType.STORE);
    }

    public void transactionPushed(ActionEvent event) throws IOException {
        loadScene(event, "Transaction.fxml", this.employeeID, this.managerFlag, ControllerType.TRANSACTION);
    }

    public void userPushed(ActionEvent event) throws IOException {
        loadScene(event, "User.fxml", this.employeeID, this.managerFlag, ControllerType.USER);
    }

    public void logOutPushed(ActionEvent event) throws IOException {
        loadScene(event, "LogIn.fxml", this.employeeID, this.managerFlag, ControllerType.LOGIN);
    }
}
