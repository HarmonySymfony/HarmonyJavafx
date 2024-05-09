package controllers;

import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import entities.Analyse;
import entities.Laboratoire;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import services.AnalyseService;
import services.LaboratoireService;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

public class AfficherAnalyse {

    @FXML
    private TableView<Analyse> analyseTable;

    @FXML
    private TableColumn<Analyse, Integer> idColumn;

    @FXML
    private TableColumn<Analyse, String> typeColumn;

    @FXML
    private TableColumn<Analyse, Float> prixColumn;

    private AnalyseService analyseService = new AnalyseService();
    private LaboratoireService laboratoireService = new LaboratoireService();
    @FXML
    private TableColumn<Analyse, Void> actionColumn;


    public void initialize() {
        // Initialize columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        prixColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));
        actionColumn.setCellFactory(createButtonCellFactory());

        //  afficherAnalayse();
    }
    private Callback<TableColumn<Analyse, Void>, TableCell<Analyse, Void>> createButtonCellFactory() {
        return new Callback<TableColumn<Analyse, Void>, TableCell<Analyse, Void>>() {
            @Override
            public TableCell<Analyse, Void> call(final TableColumn<Analyse, Void> param) {
                return new TableCell<Analyse, Void>() {
                    private final Button btnPDF = new Button("PDF");
                    private final Button btnMail = new Button("Mail");

                    {
                        btnPDF.setOnAction(event -> {
                            Analyse analyse = getTableView().getItems().get(getIndex());
                            generatePDF(analyse);
                        });

                        btnMail.setOnAction(event -> {
                            Analyse analyse = getTableView().getItems().get(getIndex());
                            sendEmailWithPDF("Sujet de l'e-mail", "Contenu de l'e-mail", analyse);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox buttonBox = new HBox(btnPDF, btnMail);
                            setGraphic(buttonBox);
                        }
                    }
                };
            }
        };
    }
    private void sendEmailWithPDF(String subject, String body, Analyse analyse) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Saisir l'adresse e-mail");
        dialog.setHeaderText(null);
        dialog.setContentText("Entrez l'adresse e-mail du destinataire:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(recipientEmail -> {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("bilelchamkhi78@gmail.com", "eomc bzpu fvep tupj");
                }
            });

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("bilelchamkhi78@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
                message.setSubject(subject);
                message.setText(body);

                // Create a PDF file with the analysis details
                generatePDF(analyse);

                // Attach the PDF file to the e-mail
                File pdfFile = new File("C:/Users/bilel/Desktop/Bilel/fichier.pdf");
                MimeBodyPart attachment = new MimeBodyPart();
                attachment.attachFile(pdfFile);
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(attachment);
                message.setContent(multipart);

                Transport.send(message);
                System.out.println("E-mail with PDF sent successfully to " + recipientEmail);

            } catch (MessagingException | IOException e) {
                e.printStackTrace();
            }
        });
    }
    private void generatePDF(Analyse analyse) {
        Document document = new Document();

        try {
            // Spécifiez le chemin et le nom du fichier PDF à générer
            String filePath = "C:/Users/bilel/Desktop/Bilel/fichier.pdf";

            // Initialize a PdfWriter object to write the content to the PDF file
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));

            // Open the document to start writing
            document.open();

            // Add a table to display the attributes of the analysis
            PdfPTable table = new PdfPTable(2); // 2 columns
            table.setWidthPercentage(100);
            table.setSpacingBefore(10);
            table.setSpacingAfter(10);

            // Add table headers
            PdfPCell header1 = new PdfPCell(new Phrase("Attribute"));
            PdfPCell header2 = new PdfPCell(new Phrase("Value"));
            table.addCell(header1);
            table.addCell(header2);

            // Add table rows with attribute-value pairs
            table.addCell("ID de l'analyse:");
            table.addCell(String.valueOf(analyse.getId()));
            table.addCell("Type d'analyse:");
            table.addCell(analyse.getType());
            table.addCell("Prix de l'analyse:");
            table.addCell(String.valueOf(analyse.getPrix()));

            // Add the table to the document
            document.add(table);

            // Close the document
            document.close();

            System.out.println("PDF generated successfully!");

            // Open the generated PDF file automatically
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(new File(filePath));
            }
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }
//    @FXML
//    public void getAnalyse() {
//        Analyse analyse = analyseTable.getSelectionModel().getSelectedItem();
//        if (analyse != null) {
//            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
//            typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
//            prixColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));
//!
//            idColumn.setText(String.valueOf(analyse.getId()));
//            typeColumn.setText(analyse.getType());
//            prixColumn.setText(String.valueOf(analyse.getPrix()));
//        } else {
//            // Gérer le cas où aucune analyse n'est sélectionnée
//            // Vous pouvez par exemple effacer le texte des colonnes ou afficher un message à l'utilisateur
//        }
//    }

//    private void afficherAnalayse() {
//        ObservableList<Analyse> analyses = null;
//        try {
//            analyses = FXCollections.observableList(analyseService.Display());
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        analyseTable.setItems(analyses);
//    }


    // Méthode pour initialiser les données d'analyse à afficher
//    public void setAnalyses(List<Analyse> analyses) {
//        // Crée une liste observable à partir de la liste d'analyses
//        ObservableList<Analyse> analyseData = FXCollections.observableArrayList(analyses);
//        // Ajoute les données à la table
//        analyseTable.setItems(analyseData);
//    }
    public void setAnalyses(Laboratoire lab) throws SQLException {
        System.out.println((lab));
        Set<Analyse> la = this.laboratoireService.getAnalysesForLaboratoire(lab);
        System.out.println(la);
        ObservableList<Analyse> analyseObservableList = FXCollections.observableArrayList(la);
        analyseTable.setItems(analyseObservableList);
    }
}
