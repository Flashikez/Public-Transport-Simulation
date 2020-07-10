/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.variantaChooser;

import objects.Vozidlo;
import objects.Zastavka;
import objects.enums.Varianta_Vozidla;

/**
 *
 * @author MarekPC
 */
public class VariantaTableItem {

    Vozidlo vozidlo;
    Varianta_Vozidla varianta;
    Zastavka startZastavka;
    String startTime;

    public VariantaTableItem(Vozidlo vozidlo, Varianta_Vozidla varianta, Zastavka startZastavka, String startTime) {
        this.vozidlo = vozidlo;
        this.varianta = varianta;
        this.startZastavka = startZastavka;
        this.startTime = startTime;
    }

    public String getStartZastavka() {
        return startZastavka.nazov();
    }

    public String getStartTime() {
        return this.startTime;
    }

    public int getId() {
        return vozidlo.getId();
    }

    public String getVarianta() {
        return vozidlo.getVarianta();
    }
    
    public Zastavka getZastavka(){
        return startZastavka;
    }

    public void setVarianta(Varianta_Vozidla var) {
        this.varianta = var;
    }

    public void setZastavka(Zastavka start) {
        this.startZastavka = start;
    }

    public void setTime(String time) {
        this.startTime = time;
    }

}
