package controller;

import entities.Laboratoire;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import services.LaboratoireService;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class afficherLabFrontController implements Initializable {
    @javafx.fxml.FXML
    private ScrollPane scrollPane;

    private LaboratoireService laboratoireService = new LaboratoireService();




    public void afficherLabs(List<Laboratoire> labs) {

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setSpacing(20);
        vBox.setStyle("-fx-background-image: url(/back.jpg) ;");
        vBox.setPrefHeight(500);

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(100);//
        //hBox.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: black; -fx-border-width: 2px;");

        int count = 0;
        for (Laboratoire lab : labs) {
            VBox box = LaboratoireCard(lab);
            box.setPrefWidth(300);
            box.setPrefHeight(100);
            hBox.getChildren().add(box);
            count++;

            if (count == 3) {
                vBox.getChildren().add(hBox);
                hBox = new HBox();
                hBox.setAlignment(Pos.CENTER);
                hBox.setSpacing(100); //
                // hBox.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: black; -fx-border-width: 2px;");
                count = 0;
            }
        }
        if (count > 0) {
            vBox.getChildren().add(hBox);
        }
        scrollPane.setContent(vBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        List<Laboratoire> laboratoires;
        try {
            laboratoires = this.laboratoireService.Display();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        afficherLabs(laboratoires);

    }

    private VBox LaboratoireCard(Laboratoire laboratoire) {
        VBox box = new VBox();
        box.setStyle("-fx-background-color: #042069; -fx-background-radius: 20;");

        box.setMaxSize(500, 500);
        box.setSpacing(50);
        box.setAlignment(Pos.CENTER);

        Insets I = new Insets(20, 0, 20, 0);
        box.setPadding(I);
        box.setOpacity(0.7);
        Glow glow = new Glow();
        DropShadow shadow = new DropShadow();
        box.setEffect(shadow);
        box.setUserData(laboratoire); // set the ID as the user data for the VBox

        Label nom = new Label(laboratoire.getNom());
        Label emp = new Label(laboratoire.getEmplacement());
        nom.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        nom.setStyle("-fx-text-fill: #d9bde5;");
        nom.setWrapText(true);
        nom.setAlignment(Pos.CENTER);
        emp.setTextFill(Color.WHITE);
        emp.setFont(Font.font("Arial", FontWeight.BOLD, 19));

        box.getChildren().addAll(nom, emp);
        return box;
    }
}
