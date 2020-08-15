package com.tostado.cashier.entities;

import com.tostado.cashier.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller extends QueryObject{
    protected int employeeID;
    protected boolean managerFlag;

    public void initData(int employeeID, boolean isManager){
        this.employeeID = employeeID;
        this.managerFlag = isManager;
    }

    public void mainMenuPushed(ActionEvent event) throws IOException {
        loadScene(event, "MainMenu.fxml", this.employeeID, this.managerFlag, ControllerType.MAINMENU);
    }

    public void loadScene(ActionEvent event, String file, int employeeID, boolean isManager, ControllerType type) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(file));
        Parent parent = loader.load();
        Scene scene;

        switch (type){
            case CUSTOMER: {
                CustomerController controller = loader.getController();
                controller.initData(employeeID, isManager);
                scene = new Scene(parent, 900, 900);
                break;
            }
            case EMPLOYEE: {
                EmployeeController controller = loader.getController();
                controller.initData(employeeID, isManager);
                controller.manager();
                scene = new Scene(parent, 900, 900);

                break;
            }
            case PRODUCT: {
                ProductController controller = loader.getController();
                controller.initData(employeeID, isManager);
                scene = new Scene(parent, 900, 900);
                break;
            }
            case STORE: {
                StoreController controller = loader.getController();
                controller.initData(employeeID, isManager);
                scene = new Scene(parent, 900, 900);
                break;
            }
            case TRANSACTION: {
                TransactionController controller = loader.getController();
                controller.initData(employeeID, isManager);
                scene = new Scene(parent, 900, 900);
                break;
            }
            case USER: {
                UserController controller = loader.getController();
                controller.initData(employeeID, isManager);
                scene = new Scene(parent, 900, 900);
                break;
            }
            case MAINMENU: {
                MainMenuController controller = loader.getController();
                controller.initData(employeeID, isManager);
                controller.manager();
                scene = new Scene(parent, 600, 600);
                break;
            }
            case LOGIN: {
                LogInController controller = loader.getController();
                controller.initData(employeeID, isManager);
                scene = new Scene(parent, 600, 600);
                break;
            }
            default:
                scene = new Scene(parent, 900, 900);
                break;
        }

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public boolean isManagerFlag() {
        return managerFlag;
    }

    public void setManagerFlag(boolean managerFlag) {
        this.managerFlag = managerFlag;
    }
}
