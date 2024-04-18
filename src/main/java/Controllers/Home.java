package Controllers;

import entites.Cabinet;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import services.CabinetServices;
import javafx.collections.ObservableList;
import entites.Cabinet;
public class Home implements Initializable {
    CabinetServices cabinet = new CabinetServices();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button ADD_RDV;

    @FXML
    private Button ADD_user;

    @FXML
    private TableColumn<?, ?> Age_afficher;

    @FXML
    private Button Ajouter_Rapport;

    @FXML
    private Button Clear_RDV;

    @FXML
    private Button Clear_user;

    @FXML
    private Button Delete_RDV;

    @FXML
    private Button Delete_user;

    @FXML
    private Button Home_page;

    @FXML
    private TextField ID_user;

    @FXML
    private ListView<?> Listview_RDV;

    @FXML
    private TableColumn<?, ?> NomAfficher;

    @FXML
    private TableColumn<?, ?> Prenom_afficher;

    @FXML
    private AnchorPane RDV_form;

    @FXML
    private Button Rendezvouspage;

    @FXML
    private TableColumn<?, ?> Role_afficher;

    @FXML
    private TextField Search_RDV;

    @FXML
    private TextField Searchuser;

    @FXML
    private Button Update_RDV;

    @FXML
    private Button Update_user;

    @FXML
    private AnchorPane User_form;

    @FXML
    private TextField age_ajou;

    @FXML
    private Button close;

    @FXML
    private TableColumn<?, ?> email_afficher;

    @FXML
    private TextField email_ajou;

    @FXML
    private TextField email_cabinet;

    @FXML
    private TextField heur_cabinet;

    @FXML
    private AnchorPane home_form;

    @FXML
    private TextField id_R;

    @FXML
    private Button logout;

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button minimize;

    @FXML
    private TextField nom_ajou;

    @FXML
    private TextField nom_cabinet;

    @FXML
    private TextField prenom_ajou;

    @FXML
    private TextField adress_cabinet;

    @FXML
    private TextField role_ajou;

    @FXML
    private TableColumn<?, ?> sexe_afficher;

    @FXML
    private TextField sexe_ajou;

    @FXML
    private Button user_page;

    @FXML
    private TableView<?> user_tableview;

    @FXML
    void close(ActionEvent event) {

    }

    @FXML
    void logout(ActionEvent event) {

    }

    @FXML
    void minimize(ActionEvent event) {

    }

    @FXML
    void switchForm(ActionEvent event) {

    }
    @FXML
    private ListView<String> cabinetListView;
/////////////////////////////////////////////////crud////


    @FXML
    private void addCabinet(ActionEvent event) {
        String adress = adress_cabinet.getText();
        String nom = nom_cabinet.getText();
        String horaires = heur_cabinet.getText();
        String email = email_cabinet.getText();

        Cabinet cabinet = new Cabinet(adress, nom, horaires, email);
        CabinetServices cabinetServices = new CabinetServices(); // Create an instance
        cabinetServices.addEntity2(cabinet); // Call the method on the instance
    }
    @FXML
    private void updateCabinet(ActionEvent event) {
        String idStr = id_R.getText();
        if (idStr.isEmpty()) {
            System.out.println("Veuillez saisir l'ID du cabinet à mettre à jour.");
            return;
        }
        int id = Integer.parseInt(idStr);
        String adress = adress_cabinet.getText();
        String nom = nom_cabinet.getText();
        String horaires = heur_cabinet.getText();
        String email = email_cabinet.getText();

        Cabinet cabinet = new Cabinet(adress, nom, horaires, email);
        cabinet.setId(id); // Set the ID of the cabinet to update

        CabinetServices cabinetServices = new CabinetServices();
        cabinetServices.updateEntity(cabinet);
    }

    @FXML
    private void deleteCabinet(ActionEvent event) {
        String idStr = id_R.getText();
        if (idStr.isEmpty()) {
            System.out.println("Veuillez saisir l'ID du cabinet à supprimer.");
            return;
        }
        int id = Integer.parseInt(idStr);

        Cabinet cabinet = new Cabinet();
        cabinet.setId(id); // Set the ID of the cabinet to delete

        CabinetServices cabinetServices = new CabinetServices();
        cabinetServices.deleteEntity(cabinet);
    }


  /*  private void loadCabinetData() {

        List<Cabinet> cabinets = CabinetServices.getAllDataCabinet();


        cabinetListView.getItems().clear();


        for (Cabinet cabinet : cabinets) {
            String cabinetDetails = String.format("ID: %d, Address: %s, Name: %s, Horaire: %s, Email: %s",
                    cabinet.getId(), cabinet.getAdress(), cabinet.getNom(), cabinet.getHoraires(), cabinet.getEmail());
            cabinetListView.getItems().add(cabinetDetails);
        }
    }*/

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        assert ADD_RDV != null : "fx:id=\"ADD_RDV\" was not injected: check your FXML file 'SAHTEK.fxml'.";
        assert ADD_user != null : "fx:id=\"ADD_user\" was not injected: check your FXML file 'SAHTEK.fxml'.";
        assert Age_afficher != null : "fx:id=\"Age_afficher\" was not injected: check your FXML file 'SAHTEK.fxml'.";
        assert Ajouter_Rapport != null : "fx:id=\"Ajouter_Rapport\" was not injected: check your FXML file 'SAHTEK.fxml'.";
        assert Clear_RDV != null : "fx:id=\"Clear_RDV\" was not injected: check your FXML file 'SAHTEK.fxml'.";
        assert Clear_user != null : "fx:id=\"Clear_user\" was not injected: check your FXML file 'SAHTEK.fxml'.";
        assert Delete_RDV != null : "fx:id=\"Delete_RDV\" was not injected: check your FXML file 'SAHTEK.fxml'.";
        assert Delete_user != null : "fx:id=\"Delete_user\" was not injected: check your FXML file 'SAHTEK.fxml'.";
        assert Home_page != null : "fx:id=\"Home_page\" was not injected: check your FXML file 'SAHTEK.fxml'.";
        assert ID_user != null : "fx:id=\"ID_user\" was not injected: check your FXML file 'SAHTEK.fxml'.";
        assert Listview_RDV != null : "fx:id=\"Listview_RDV\" was not injected: check your FXML file 'SAHTEK.fxml'.";
        assert NomAfficher != null : "fx:id=\"NomAfficher\" was not injected: check your FXML file 'SAHTEK.fxml'.";
        assert Prenom_afficher != null : "fx:id=\"Prenom_afficher\" was not injected: check your FXML file 'SAHTEK.fxml'.";
        assert RDV_form != null : "fx:id=\"RDV_form\" was not injected: check your FXML file 'SAHTEK.fxml'.";
        assert Rendezvouspage != null : "fx:id=\"Rendezvouspage\" was not injected: check your FXML file 'SAHTEK.fxml'.";
        assert Role_afficher != null : "fx:id=\"Role_afficher\" was not injected: check your FXML file 'SAHTEK.fxml'.";
        assert Search_RDV != null : "fx:id=\"Search_RDV\" was not injected: check your FXML file 'SAHTEK.fxml'.";
        assert Searchuser != null : "fx:id=\"Searchuser\" was not injected: check your FXML file 'SAHTEK.fxml'.";
        assert Update_RDV != null : "fx:id=\"Update_RDV\" was not injected: check your FXML file 'SAHTEK.fxml'.";
        assert Update_user != null : "fx:id=\"Update_user\" was not injected: check your FXML file 'SAHTEK.fxml'.";
        assert User_form != null : "fx:id=\"User_form\" was not injected: check your FXML file 'SAHTEK.fxml'.";
        assert age_ajou != null : "fx:id=\"age_ajou\" was not injected: check your FXML file 'SAHTEK.fxml'.";
        assert close != null : "fx:id=\"close\" was not injected: check your FXML file 'SAHTEK.fxml'.";
        assert email_afficher != null : "fx:id=\"email_afficher\" was not injected: check your FXML file 'SAHTEK.fxml'.";
        assert email_ajou != null : "fx:id=\"email_ajou\" was not injected: check your FXML file 'SAHTEK.fxml'.";
        assert email_cabinet != null : "fx:id=\"email_cabinet\" was not injected: check your FXML file 'SAHTEK.fxml'.";
        assert heur_cabinet != null : "fx:id=\"heur_cabinet\" was not injected: check your FXML file 'SAHTEK.fxml'.";
        assert home_form != null : "fx:id=\"home_form\" was not injected: check your FXML file 'SAHTEK.fxml'.";
        assert id_R != null : "fx:id=\"id_R\" was not injected: check your FXML file 'SAHTEK.fxml'.";
        assert logout != null : "fx:id=\"logout\" was not injected: check your FXML file 'SAHTEK.fxml'.";
        assert main_form != null : "fx:id=\"main_form\" was not injected: check your FXML file 'SAHTEK.fxml'.";
        assert minimize != null : "fx:id=\"minimize\" was not injected: check your FXML file 'SAHTEK.fxml'.";
        assert nom_ajou != null : "fx:id=\"nom_ajou\" was not injected: check your FXML file 'SAHTEK.fxml'.";
        assert nom_cabinet != null : "fx:id=\"nom_cabinet\" was not injected: check your FXML file 'SAHTEK.fxml'.";
        assert prenom_ajou != null : "fx:id=\"prenom_ajou\" was not injected: check your FXML file 'SAHTEK.fxml'.";
        assert adress_cabinet != null : "fx:id=\"prenom_cabinet\" was not injected: check your FXML file 'SAHTEK.fxml'.";
        assert role_ajou != null : "fx:id=\"role_ajou\" was not injected: check your FXML file 'SAHTEK.fxml'.";
        assert sexe_afficher != null : "fx:id=\"sexe_afficher\" was not injected: check your FXML file 'SAHTEK.fxml'.";
        assert sexe_ajou != null : "fx:id=\"sexe_ajou\" was not injected: check your FXML file 'SAHTEK.fxml'.";
        assert user_page != null : "fx:id=\"user_page\" was not injected: check your FXML file 'SAHTEK.fxml'.";
        assert user_tableview != null : "fx:id=\"user_tableview\" was not injected: check your FXML file 'SAHTEK.fxml'.";

       // loadCabinetData();

    }

}
