/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import objects.enums.Varianta_Vozidla;
import simulation.MyMessage;

/**
 *
 * @author MarekPC
 */
public class Mikrobus extends Vozidlo {

    public Mikrobus(int id, Varianta_Vozidla var, Zastavka startZ, double startT) {
        super(id, var, startZ, startT);
    }

    @Override
    public String toString() {
        return "Mikrobus";
    }

    @Override
    public void pridajCestujuceho(MyMessage msg) {
        super.pridajCestujuceho(msg);
        this.ziskMikrobusu += 1.0;
    }

}
