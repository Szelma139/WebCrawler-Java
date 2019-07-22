/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;

import java.sql.Connection;
import java.sql.ResultSet;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Callback;
import static javax.management.Query.gt;
import static javax.management.Query.lt;
/**
 *
 * @author Szelma
 */
public class Model {
    String url;
    String ip;
    String p;   
    String nextUrl;
        public Statement statement;
     // private static Connection conn=null;
      public Connection con = null;
public ResultSet rs = null;


            
            
    public Model(){}
    public Model(String url, String ip) 
    {
        this.url = url;
        this.ip = ip;
        
    }
      private ObservableList lt;
      ObservableList data;
      
      
      
      
      public void addToVisited(){   
       con = ConnectionManager.getConnection();
          try
          {    
                 
          statement=con.createStatement();    
          rs = statement.executeQuery("SELECT  nazwa,domena,adresip FROM nodes");
         while(rs.next())
{
  System.out.println("Porownuje  " + url + " oraz " + rs.getString("nazwa"));
  //if (nextUrl!=null || (rs.getString("nazwa")!=null))
          
               nextUrl = rs.getString("nazwa");   
               Crawler crawler = new Crawler();
               crawler.pagesVisited.add(nextUrl);

}
           statement.close();
           con.close();
          
          } catch(SQLException e ) {
                                     System.out.println("Blad dodawania do listy");
                                     System.out.println("Czyzby gdzies byl null.. " + e.getMessage());
                        
                                   }  

      }
      public boolean compareUrl(String url){
          
       con = ConnectionManager.getConnection();
          try
          {    
                 
          statement=con.createStatement();    
          rs = statement.executeQuery("SELECT  nazwa,domena,adresip FROM nodes");
         while(rs.next())
{
  System.out.println("Porownuje  " + url + " oraz " + rs.getString("nazwa"));
               nextUrl = rs.getString("nazwa");   
               Crawler crawler = new Crawler();
               crawler.pagesVisited.add(nextUrl);
               //nextUrl = rs.getString("domena");
 //if (nextUrl==url) //break;
// {
  //       return false;

//}
 //else
 if (nextUrl==url)
 {
     return false;
 }
}
     
             
          //  String s = rs.getString(2);
         
         
      
           statement.close();
           con.close();
          
          } catch(SQLException e ) {
                                     System.out.println("Statement insertion Error");
                                     System.out.println("Inserting Domain and Url unsuccesfull" + e.getMessage());
                        
                                   }  
   //System.out.println(true);
                  return true ;
      }
      
      public void insertFirst(String domena,String url,String ip)
      {
        //  FXMLDocumentController controller = new FXMLDocumentController();
        //  controller.loadDataFromDatabase();
          con = ConnectionManager.getConnection();
          try{    
                 
          statement=con.createStatement();    
          statement.execute("CREATE TABLE IF NOT EXISTS nodes" + "(id integer PRIMARY KEY,domena TEXT, nazwa TEXT, adresip TEXT)");
          statement.execute("INSERT INTO nodes (domena,nazwa,adresip) VALUES ('"+domena+"', '"+url+"','"+ip+"')");
           statement.close();
           con.close();
           //System.out.println()
          } catch(SQLException e ) {
                                     System.out.println("Error with data insertion" + e.getMessage());
                                     System.out.println("Information Dialog");
                                   }  
          
      }
      
      
      
      
      
      
       public void insertToEdges(String url)
      {
        //  FXMLDocumentController controller = new FXMLDocumentController();
        //  controller.loadDataFromDatabase();
          con = ConnectionManager.getConnectiontab2();
          try{    
                 
          statement=con.createStatement();    
          statement.execute("CREATE TABLE IF NOT EXISTS edges" + "(id integer PRIMARY KEY, nazwa TEXT)");
          statement.execute("INSERT INTO edges (nazwa) VALUES ('"+url+"')");
           statement.close();
           con.close();
           //System.out.println()
          } catch(SQLException e ) {
                                     System.out.println("Error with insertion to edges table" + e.getMessage());
                                     System.out.println("EDGES FAILURE");
                                   }  
          
      }
      
      
   
      public void insertDomainAndUrl(String domena,String url){
          FXMLDocumentController controller = new FXMLDocumentController();
        //   controller.loadDataFromDatabase();
       //controller.tableUrl.refresh();
       con = ConnectionManager.getConnection();
          try
          {    
                 
         statement=con.createStatement();    
         statement.execute("CREATE TABLE IF NOT EXISTS nodes" + "(id integer PRIMARY KEY,domena TEXT, nazwa TEXT, adresip TEXT)");
         statement.execute("INSERT INTO nodes (domena,nazwa) VALUES ('"+domena+"', '"+url+"')");
           statement.close();
           con.close();
          } catch(SQLException e ) {
                                     System.out.println("Statement insertion Error");
                                     System.out.println("Inserting Domain and Url unsuccesfull" + e.getMessage());
                       
                                   }  

          }
      
  
      
   public String returnFirstNullIp()
   {
           
       con = ConnectionManager.getConnection();
          try
          {    
                 
          statement=con.createStatement();    
          statement.execute("CREATE TABLE IF NOT EXISTS nodes" + "(id integer PRIMARY KEY,domena TEXT, nazwa TEXT, adresip TEXT)");
          rs = statement.executeQuery("SELECT  nazwa,domena,adresip FROM nodes WHERE adresip IS NULL");
                 if (!rs.isBeforeFirst() ) {    
    return "brak";
}
             System.out.println("Url bez ip to " + rs.getString("nazwa"));
               nextUrl = rs.getString("nazwa");          
            String p = rs.getString(2);
          //  String s = rs.getString(2);
         
 
      
           statement.close();
           con.close();
          
          } catch(SQLException e ) {
                                     System.out.println("Statement insertion Error");
                                     System.out.println("Inserting Domain and Url unsuccesfull" + e.getMessage());
                        
                                   }  
   
                  return nextUrl ;
   }   
   
      
    public void insertAll(String domena,String url,String ip){
     FXMLDocumentController controller = new FXMLDocumentController();
         // controller.loadDataFromDatabase();
           con = ConnectionManager.getConnection();
        try{
            //Connection conn = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\Szelma\\Documents\\sqlite-tools-win32-x86-3230000\\nodes.db");
            Statement statement = con.createStatement();     
            statement.execute("CREATE TABLE IF NOT EXISTS nodes" + "(id integer PRIMARY KEY,domena TEXT, nazwa TEXT, adresip TEXT)");
        //   System.out.println(url);
           
          // statement.execute();
           ResultSet results = statement.executeQuery("SELECT  nazwa,adresip FROM nodes WHERE domena=" +domena+ "AND"+ "nazwa=" + url);
      // while(results.next()) {
             //  System.out.println(results.getString("nazwa"));
             //       this.p=results.getString("adresip");
             
          //}
          // if (this.p!=ip)
            statement.execute("INSERT INTO nodes (domena,nazwa,adresip) VALUES ('"+domena+"', '"+url+"','"+ip+"')");
               

            System.out.println("Information Dialog");
            System.out.println("Success");
            System.out.println("Succesfully connected to database!");          
            System.out.println("Podłączono do bazy");
               String sql= "SELECT * from nodes";
            Statement s  = con.createStatement();
ResultSet rs=s.executeQuery(sql);
while(rs.next())
{
  System.out.println( rs.getString(1));
}
  // checkTabNames();
            statement.close();
      
        } catch(SQLException e ) {
            System.out.println("Error with connection to database" + e.getMessage());
                  System.out.println("Information Dialog");
                 System.out.println("Error");
                 System.out.println("Error while trying to connect to database!");

        }
    
    // Model model = new Model();
    // model.Connect();
    }
    
    
    
    
    
 
    
    
     
     
     
   
      public void insertAll(String domena,String url){
  con = ConnectionManager.getConnection();
        try{
           // Connection conn = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\Szelma\\Documents\\sqlite-tools-win32-x86-3230000\\nodes.db");
            Statement statement = con.createStatement();     
            statement.execute("CREATE TABLE IF NOT EXISTS nodes" + "(id integer PRIMARY KEY,domena TEXT, nazwa TEXT, adresip TEXT)");
           System.out.println(url);
           
            statement.execute("INSERT INTO nodes (domena,nazwa) VALUES ('"+domena+"', '"+url+"')");

            System.out.println("Information Dialog");
            System.out.println("Success");
          //  System.out.println("Succesfully connected to database!");          
            //System.out.println("Podłączono do bazy");
            
            statement.close();
      
        } catch(SQLException e ) {
            System.out.println("Error with connection to database" + e.getMessage());
                  System.out.println("Blad insertall domena url");
                 System.out.println("Error");
                 System.out.println("Error while trying to connect to database!");

        }

    }
    
      public void updateIP(String ip, String url){
          
            con = ConnectionManager.getConnection();
           try{
           // Connection con = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\Szelma\\Documents\\sqlite-tools-win32-x86-3230000\\nodes.db");
            Statement statement = con.createStatement();     
          
           
           // statement.execute("INSERT INTO nodes (domena,nazwa) VALUES ('"+domena+"', '"+url+"')");         
          statement.executeUpdate("UPDATE nodes set (adresip) = '"+ip+"' where adresip IS NULL AND (nazwa) IS '"+url+"'");

            statement.close();
      
        } catch(SQLException e ) {
            System.out.println("Blad updateIP" + e.getMessage());
                  System.out.println("Information Dialog");
                 System.out.println("Error");
                 System.out.println("Error while trying to connect to database!");

        }
          
          
          
      }
      
      public void testDBConnection(){
           
FXMLDocumentController controller = new FXMLDocumentController();

        try{
                Class.forName("org.sqlite.JDBC");
                System.out.println("Trying to connect");
                          con = ConnectionManager.getConnection();

                System.out.println("Polaczenie osiagalne nazwa bazy to"
                        + con.getMetaData().getDatabaseProductName());
                controller.infoBox("SUKCES", "POLACZENIE JEST OSIAGALNE", "Połączenie z bazą");
            } catch (Exception e) {
System.out.println("Nrak polaczenia");
controller.infoBox("NIEUDALO SIE", "POLACZENIE JEST NIE OSIAGALNE", "BRAK połączenia z bazą");

                e.printStackTrace();
            }
        
      }
      
      
      public void cleanDatabase(){
          
          con = ConnectionManager.getConnection();
        try{
           // Connection conn = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\Szelma\\Documents\\sqlite-tools-win32-x86-3230000\\nodes.db");
            Statement statement = con.createStatement();     
            statement.execute("CREATE TABLE IF NOT EXISTS nodes" + "(id integer PRIMARY KEY,domena TEXT, nazwa TEXT, adresip TEXT)");          
            statement.execute("DELETE FROM nodes");

            System.out.println("Usuwanie");
            System.out.println("Powiodlo sie");
          //  System.out.println("Succesfully connected to database!");          
            //System.out.println("Podłączono do bazy");
            
            statement.close();
      
        } catch(SQLException e ) {
            System.out.println("Error with connection to database" + e.getMessage());
                  System.out.println("Blad insertall domena url");
                 System.out.println("Error");
                 System.out.println("Error while trying to connect to database!");

        }
          
      }
      
//        public void buildData(){
//          Connection c ;
//          data = FXCollections.observableArrayList();
//          FXMLDocumentController controller = new FXMLDocumentController();
//          try{
//              Connection conn = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\Szelma\\Documents\\sqlite-tools-win32-x86-3230000\\nodes.db");
//            Statement statement = conn.createStatement(); 
//            
//             String quer="SELECT * FROM nodes";
//
//    Statement st=conn.createStatement();
//    ResultSet rs=st.executeQuery(quer);
//    ResultSetMetaData rsmd = rs.getMetaData();
//    int NumOfCol=0;
//    NumOfCol=rsmd.getColumnCount();
//    System.out.println("Number of columns="+NumOfCol);
//            //ResultSet      
//            quer=" SELECT COUNT(*) " + "FROM nodes;";
//          int NumOfRows=0;
//           st=conn.createStatement();
//              rs=st.executeQuery(quer);
//              while (rs.next())
//     {
//            NumOfRows = rs.getInt(1);
//            //SQL FOR SELECTING ALL OF CUSTOMER
//         System.out.println("Number of Rows="+NumOfRows);
  
  
    
  //  controller.col=NumOfCol;
   // controller.row=NumOfRows;
     }
             
 
              
              
//          }
//           catch(SQLException e ) {
//            System.out.println("Blad updateIP" + e.getMessage());
//                  System.out.println("Information Dialog");
//                 System.out.println("Error");
//                 System.out.println("Error while trying to connect to database!");
//
//        }
          
//  }
// }

