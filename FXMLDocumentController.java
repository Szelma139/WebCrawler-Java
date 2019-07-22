/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.TimerTask;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JTable;
//import javax.swing.text.TabableView;
/**
 *
 * @author Szelma
 */
public class FXMLDocumentController {

    public FXMLDocumentController() {TableView<UrlList> tableUrl = new TableView();
    }
    
    @FXML
    public static TableView<UrlList> tableUrl;
    @FXML
    public static TableColumn<UrlList, String> columnDomena;
    @FXML
    public static TableColumn<UrlList, String> columnNazwa;
    @FXML
  public static TableColumn<UrlList, String> columnAdresip;
    public static ObservableList<UrlList> data;
    
    @FXML
    private Button cleanButton;
    
   // @FXML
    private TableView table;
    
    @FXML
    private Label label;
    @FXML
    private Button conTest;
    
    @FXML
    private Button stop;
    @FXML
    private TextField conPagesToVisit;
    @FXML
    private Button conLinkButton;
    @FXML
    private TextField conLink;
    @FXML
    private Button conButtonIgnore;
    @FXML
    private TextField conIgnore;
    @FXML
 
       Alert alert = new Alert(AlertType.INFORMATION);
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
    }
 
    
    @FXML
    private void goClean()
    {
        Model model = new Model();
        model.cleanDatabase();
    }
    @FXML
    private void addToIgnored(){
        Crawler crawler = new Crawler();
        String p = conIgnore.getText();
        crawler.addToIgnored(p);
        infoBox("Dodano adres do ignorowanych","Adres " + p + "zostanie pominiety","Sukces dodawania " + p + " do listy ignrowanych adresow");
    }
    
    @FXML
    private void goTestConnection()
    {
        Model model = new Model();
        model.testDBConnection();
    }
    
    @FXML
    private void stopProgram(){
        Crawler crawler = new Crawler();
       crawler.itsTimeToStop=1;
    }
    public void loadDataFromDatabase(){
      //  if(data!=null)
      // data.removeAll();
     // 
    //  ObservableList<UrlList> data = new FXCollections.ObservableList(UrlList);
          try
          {    
             Model model = new Model();
             data = FXCollections.observableArrayList();
         model.con = ConnectionManager.getConnection();     
          model.rs=model.con.createStatement().executeQuery("Select * from nodes");   
    while(model.rs.next())
    {
       // String a = model.rs.getString(2);
         //   String b = model.rs.getString(3);
                String c = model.rs.getString(4);
               // StringProperty var = new SimpleStringProperty((String) a);
               //   StringProperty var2 = new SimpleStringProperty((String) b);
                  //  StringProperty var3 = new SimpleStringProperty((String) c);
                // data.add(new UrlList(var,var2,var3));
              data.add(new UrlList(model.rs.getString(2),model.rs.getString(3),model.rs.getString(4)));
    }
      //   model.statement.close();
          model.con.close();
          } catch(SQLException e ) {
                                     System.out.println("Statement insertion Error");
                                     System.out.println("Inserting Domain and Url unsuccesfull" + e.getMessage());
                       
                                   }  
          TableColumn<UrlList, String> columnDomena = new TableColumn();
          TableColumn<UrlList, String> columnAdresip = new TableColumn();
          TableColumn<UrlList, String> columnNazwa = new TableColumn();
          
          
          
          columnDomena.setCellValueFactory(new PropertyValueFactory<>("domena"));
          columnNazwa.setCellValueFactory(new PropertyValueFactory<>("nazwa"));
          columnAdresip.setCellValueFactory(new PropertyValueFactory<>("adresip"));
          TableView tableUrl = new TableView(); 
          tableUrl.setItems(null);
          tableUrl.setItems(data);
     
    }
    
    

    @FXML
    public void  startCrawling() throws IOException, URISyntaxException{
    String currentUrl;
loadDataFromDatabase();
             currentUrl = conLink.getText(); 
       
             if (isValid(currentUrl)!=false){
             Crawler crawler = new Crawler(currentUrl);
             //crawler.processFirstUrl(currentUrl);
                   setCrawlerDepth();
             crawler.testProcessing(currentUrl);
             }
             else {
                       infoBox("Błąd adresu URL", "Niepoprawny adres " , "Prosze wpisac poprawny adres strony, POPRAWNY ADRES to NP.  http://www.interia.pl");
                  }
           // crawler.currentUrl = currentUrl;
    }
    
    
    

    
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public static boolean isValid(String url)
    {
        /* Try creating a valid URL */
        try {
            new URL(url).toURI();
            return true;
        }
         
        // If there was an Exception
        // while creating URL object
        catch (Exception e) {
            return false;
        }
    }
    
    
    @FXML
        public void setCrawlerDepth()
        {
         int i=0;
               //     if((conPageToVisit.getText().)))
               if ((conPagesToVisit.getText()!=null)) 
                    {
                        
                              String t = conPagesToVisit.getText();
                        try{
   i  = Integer.parseInt(t);
}catch(NumberFormatException ex){ // handle your exception
System.out.println("Exception number");
}
      
            Crawler crawler = new Crawler();
            crawler.crawlerDepth = i;
            System.out.println("Ustawilem wartosc linkow na " + t);
                    }
               else 
               {
                  Crawler crawler = new Crawler();
            crawler.crawlerDepth = 10;    
               }
    
        }
    
     public static void infoBox(String infoMessage, String titleBar, String headerMessage)
    {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(titleBar);
        alert.setHeaderText(headerMessage);
        alert.setContentText(infoMessage);
        alert.showAndWait();
    }
}
