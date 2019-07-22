/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
/**
 *
 * @author Szelma
 */
public class UrlList {

    public String getNazwa() {
        return nazwa.get();
    }

    public String getDomena() {
        return domena.get();
    }

    public String getAdresip() {
        return adresip.get();
    }

    public UrlList(String domena, String nazwa, String adresip) {
        this.domena = new SimpleStringProperty(domena);
        this.nazwa = new SimpleStringProperty(nazwa);
        this.adresip = new SimpleStringProperty(adresip);
    }
    
   // public void set
    
    
   private final StringProperty domena;
   private final StringProperty nazwa;
   private final StringProperty adresip;
}
