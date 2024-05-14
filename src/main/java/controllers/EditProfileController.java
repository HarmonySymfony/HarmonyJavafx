package controllers;

import entities.Personne;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import services.PersonneServices;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Blob;
import java.sql.SQLException;

public class EditProfileController {

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField ageField;

    @FXML
    private TextField emailField;

    private Blob profilePicture;
    private Personne user;

    private PersonneServices personneServices = new PersonneServices();


    public void initData(Personne user) {
        this.user = user;
        if (user != null) {
            nomField.setText(user.getNom());
            prenomField.setText(user.getPrenom());
            emailField.setText(user.getEmail());
            passwordField.clear();
            ageField.setText(String.valueOf(user.getAge()));
        }
    }

    @FXML
    public void chooseFile() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try {
                FileInputStream fis = new FileInputStream(file);
                profilePicture = new javax.sql.rowset.serial.SerialBlob(fis.readAllBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void updateProfile() {
        Personne user = personneServices.getUserById(Home.userID);
        user.setNom(nomField.getText());
        user.setPrenom(prenomField.getText());
        user.setPassword(passwordField.getText());
        user.setAge(Integer.parseInt(ageField.getText()));
        user.setProfilePicture(profilePicture);

        personneServices.updateEntity(user);
    }

}