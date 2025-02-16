package se.lu.ics.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import se.lu.ics.data.UserDao;

public class LoginDialogController {

    @FXML
    private TextField textFieldUsername;
    @FXML
    private TextField passwordFieldUserPassword;

    private AppController appController;
    private UserDao userDao;

    public LoginDialogController() {
        try {
            this.userDao = new UserDao();
        } catch (IOException e) {
            appController.showAlert("Could not connect to the server", "Error", AlertType.ERROR);
        }
    }

    @FXML
    public void handleButtonLoginAction(ActionEvent event) {
        String username = textFieldUsername.getText();
        String password = passwordFieldUserPassword.getText();

        if (userDao.authenticate(username, password)) {
            appController.showAlert("Login successful", "Login", AlertType.INFORMATION);
        } else {
            appController.showAlert("Login failed", "Login", AlertType.ERROR);
        }
    }

    public void setAppController(AppController appController) {
        this.appController = appController;
    }
}
