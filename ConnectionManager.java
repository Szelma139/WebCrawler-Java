package main;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Szelma
 */
public class ConnectionManager {
    //private static String url = "jdbc:sqlite:C:\\Users\\Szelma\\Documents\\sqlite-tools-win32-x86-3230000\\nodes.db";   
    private static String url = "jdbc:sqlite:src\\SQLITe\\nodes.db";
    private static String url2 = "jdbc:sqlite:src\\SQLITe\\edges.db";
    private static String driverName = "org.sqlite.JDBC";   
    private static Connection con;
    public static int stop=0;

    public static Connection getConnection() {
        try {
            Class.forName(driverName);
            try {
                con = DriverManager.getConnection(url);
            } catch (SQLException ex) {
                // log an exception. fro example:
                System.out.println("Failed to create the database connection."); 
            }
        } catch (ClassNotFoundException ex) {
            // log an exception. for example:
            System.out.println("Driver not found."); 
        }
        return con;
    }
    
    
    public static Connection getConnectiontab2() {
        try {
            Class.forName(driverName);
            try {
                con = DriverManager.getConnection(url2);
            } catch (SQLException ex) {
                // log an exception. fro example:
                System.out.println("Failed to create the database connection with table 2."); 
            }
        } catch (ClassNotFoundException ex) {
            // log an exception. for example:
            System.out.println("Driver not found."); 
        }
        return con;
    }
    
    
    
    
    
}