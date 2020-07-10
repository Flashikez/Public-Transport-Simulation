/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import objects.enums.Varianta_Vozidla;

/**
 *
 * @author MarekPC
 */
public class Autobus extends Vozidlo {

    public Autobus(int id, Varianta_Vozidla var,Zastavka startZ,double startT) {
        super(id, var,startZ,startT);
    }

    @Override
    public String toString() {
        return "Autobus" ;
    }

}
