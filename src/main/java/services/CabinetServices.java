package services;
import entites.Cabinet;

import org.apache.pdfbox.pdmodel.font.PDType1Font;
import outils.MyDB;

import java.awt.*;
import java.io.IOException;
import java.sql.ResultSet;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;




import java.sql.PreparedStatement;
import java.io.File;


import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class CabinetServices implements IservicesCabinet<Cabinet> {
    private static final String PDF_PATH = "src/main/resources/PDF/";


    public boolean entityExists(Cabinet cabinet) {
        try {
            // Vérifier si le rendez-vous existe dans la base de données en utilisant une requête SELECT
            String requete = "SELECT * FROM cabinet WHERE adress = ? AND nom = ? AND horaires = ? AND email = ?";
            PreparedStatement pst = MyDB.getInstance().getConnection().prepareStatement(requete);
            pst.setString(1, cabinet.getAdress());
            pst.setString(2, cabinet.getNom());
            pst.setString(3, cabinet.getHoraires());
            pst.setString(4, cabinet.getEmail());
            ResultSet rs = pst.executeQuery();
            return rs.next(); // Renvoie true si le résultat de la requête n'est pas vide (le rendez-vous existe déjà), sinon false
        } catch (SQLException e) {
            // Gérer les exceptions SQLException
            System.out.println("Erreur lors de la vérification de l'existence du cabinet : " + e.getMessage());
            return false; // En cas d'erreur, retourner false par défaut
        }
    }


    public  boolean isValidEmail(String email) {
        // Expression régulière pour valider un email
        String regex = "^(.+)@(.+)$";
        return email.matches(regex);
    }

    public  boolean isValidDateTime(String dateTime) {
        // Format de date/heure attendu
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setLenient(false);
        try {
            // Tentative de parsing de la chaîne en date/heure
            sdf.parse(dateTime);
            return true;
        } catch (ParseException e) {
            return false; // Si le parsing échoue, la date/heure est invalide
        }
    }
    public void addEntity(Cabinet cabinet) {


    }



    public  void addEntity2(Cabinet cabinet) {
        try {
            // Vérifier si le rendez-vous existe déjà dans la base de données
            if (entityExists(cabinet)) {
                System.out.println("Le Cabinet existe déjà dans la base de données.");
            } else {
                // Vérifier si l'email est valide
                if (!isValidEmail(cabinet.getEmail())) {
                    System.out.println("Email invalide.");
                    return;
                }
                // Vérifier si les horaires sont valides
                if (!isValidDateTime(cabinet.getHoraires())) {
                    System.out.println("Horaires invalides.");
                    return;
                }

                // Ajouter le rendez-vous à la base de données s'il n'existe pas déjà
                String requete = "INSERT INTO cabinet (adress,nom,horaires,email) VALUES (?,?,?,?)";
                PreparedStatement pst = MyDB.getInstance().getConnection().prepareStatement(requete);
                pst.setString(1, cabinet.getAdress());
                pst.setString(2, cabinet.getNom());
                pst.setString(3, cabinet.getHoraires());
                pst.setString(4, cabinet.getEmail());
                pst.executeUpdate();
                System.out.println("Cabinet ajouté avec succès !");
            }
        } catch (SQLException e) {
            // Gérer les exceptions SQLException
            System.out.println("Erreur lors de l'ajout du Cabinet : " + e.getMessage());
        }
    }





    public void updateEntity(Cabinet cabinet) {
        String requete = "UPDATE cabinet SET adress = ?, nom = ?, horaires = ?, email = ? WHERE id= ?";

        try {


                // Vérifier si l'email est valide
                if (!isValidEmail(cabinet.getEmail())) {
                    System.out.println("Email invalide.");
                    return;
                }
                // Vérifier si les horaires sont valides
                if (!isValidDateTime(cabinet.getHoraires())) {
                    System.out.println("Horaires invalides.");
                    return;
                }

            PreparedStatement pst = MyDB.getInstance().getConnection().prepareStatement(requete);

            pst.setString(1, cabinet.getAdress());
            pst.setString(2, cabinet.getNom());
            pst.setString(3, cabinet.getHoraires());
            pst.setString(4, cabinet.getEmail());
            pst.setInt(5, cabinet.getId());



            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Cabinet Updated");
            } else {
                System.out.println("Cabinet not found");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void deleteEntity(Cabinet cabinet) {
        String requete = "DELETE FROM cabinet WHERE id=?";
        try {
            PreparedStatement pst = MyDB.getInstance().getConnection().prepareStatement(requete);
            pst.setInt(1, cabinet.getId());

            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Cabinet Deleted");
            } else {
                System.out.println("Cabinet not found");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }




     public static List<Cabinet> getAllDataCabinet() {
        List<Cabinet> data = new ArrayList<>();
        String requete = "SELECT * FROM cabinet";
        try {
            Statement st = MyDB.getInstance().getConnection().createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()) {
                Cabinet c = new Cabinet();


                int id = rs.getInt("id");
                String address = rs.getString("Adress");
                String nom = rs.getString("nom");
                String horaires = rs.getString("horaires");
                String email = rs.getString("email");
                System.out.println("ID: " + id + ", Address: " + address + ", Nom: " + nom + ", Horaires: " + horaires + ", Email: " + email);

                c.setId(id);
                c.setAdress(address);
                c.setNom(nom);
                c.setHoraires(horaires);
                c.setEmail(email);
                data.add(c);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return data;
    }

    public static void savePDF(String fileName) {
        String filePath = PDF_PATH + fileName + ".pdf";
        File file = new File(filePath);
        if (file.exists()) {
            // Générer un nom de fichier unique si le fichier existe déjà
            fileName = generateUniqueFileName(fileName);
            filePath = PDF_PATH + fileName + ".pdf";
            file = new File(filePath);
        }
    }

    public static void generatePDF(List<Cabinet> cabinets, String fileName) throws IOException {
        String filePath = PDF_PATH + fileName + ".pdf";
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Ajouter la date et l'heure actuelles au contenu du document
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                LocalDateTime now = LocalDateTime.now();
                String currentDate = "Date: " + dtf.format(now);
                float xDate = 50; // Position X de la date
                float yDate = page.getMediaBox().getHeight() - 200; // Position Y de la date
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(xDate, yDate);
                contentStream.showText(currentDate);
                contentStream.endText();

                // Dessiner une ligne pour séparer la date du reste du contenu
                float lineY = yDate - 10;
                contentStream.moveTo(xDate, lineY);
                contentStream.lineTo(xDate + 150, lineY);
                contentStream.stroke();

                // Définir les coordonnées pour le début du tableau
                float xTable = 50; // Position X du coin supérieur gauche du tableau
                float yTable = page.getMediaBox().getHeight() - 250; // Position Y du coin supérieur gauche du tableau

                // Créer un tableau pour afficher les détails des cabinets
                float tableWidth = page.getMediaBox().getWidth() - 2 * xTable;
                float tableHeight = 100;
                float cellMargin = 10;
                float cellWidth = (tableWidth - 4 * cellMargin) / 4;
                float cellHeight = 20;

                // Dessiner une ligne pour séparer l'en-tête du contenu du tableau
                float headerLineY = yTable + cellHeight + cellMargin / 2;
                contentStream.moveTo(xTable, headerLineY);
                contentStream.lineTo(xTable + tableWidth, headerLineY);
                contentStream.stroke();

                // Ajouter les en-têtes de colonne avec du texte vert
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                float yText = yTable - cellHeight / 2;
                contentStream.setNonStrokingColor(Color.GREEN);
                contentStream.beginText();
                contentStream.newLineAtOffset(xTable + cellMargin, yText);
                contentStream.showText("Adresse");
                contentStream.endText();
                contentStream.beginText();
                contentStream.newLineAtOffset(xTable + cellMargin + cellWidth + cellMargin, yText);
                contentStream.showText("Nom");
                contentStream.endText();
                contentStream.beginText();
                contentStream.newLineAtOffset(xTable + cellMargin + 2 * (cellWidth + cellMargin), yText);
                contentStream.showText("Horaires");
                contentStream.endText();
                contentStream.beginText();
                contentStream.newLineAtOffset(xTable + cellMargin + 3 * (cellWidth + cellMargin), yText);
                contentStream.showText("Email");
                contentStream.endText();

                // Remplir le tableau avec les détails des cabinets
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                for (Cabinet cabinet : cabinets) {
                    contentStream.beginText();
                    yText -= cellHeight;
                    contentStream.newLineAtOffset(xTable + cellMargin, yText);
                    contentStream.showText(cabinet.getAdress());
                    contentStream.endText();
                    contentStream.beginText();
                    contentStream.newLineAtOffset(xTable + cellMargin + cellWidth + cellMargin, yText);
                    contentStream.showText(cabinet.getNom());
                    contentStream.endText();
                    contentStream.beginText();
                    contentStream.newLineAtOffset(xTable + cellMargin + 2 * (cellWidth + cellMargin), yText);
                    contentStream.showText(cabinet.getHoraires());
                    contentStream.endText();
                    contentStream.beginText();
                    contentStream.newLineAtOffset(xTable + cellMargin + 3 * (cellWidth + cellMargin), yText);
                    contentStream.showText(cabinet.getEmail());
                    contentStream.endText();
                }
            }
            document.save(filePath);
        }
    }

    private static String generateUniqueFileName(String fileName) {
        // Ajouter une logique pour générer un nom de fichier unique
        // Par exemple, vous pouvez ajouter un timestamp à la fin du nom de fichier
        return fileName + "_" + System.currentTimeMillis();
    }
















}
