/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.replGUI;

import objects.Vozidlo;
import simulation.MySimulation;

/**
 *
 * @author MarekPC
 */
public class VozidloStatTableItem {

    public Vozidlo vozidlo;
    public MySimulation.Sim_Replications_Stats stat;

    public VozidloStatTableItem(Vozidlo vozidlo, MySimulation.Sim_Replications_Stats stat) {
        this.vozidlo = vozidlo;
        this.stat = stat;
    }

    public int getID() {
        return vozidlo.getId();
    }

    public String getVarianta() {
        return vozidlo.varianta().toString();
    }

    public String getCelkovo() {
        return (String.format("%.3f", stat.poctyCestujucichVozidiel.get(vozidlo).mean()));
    }

}
