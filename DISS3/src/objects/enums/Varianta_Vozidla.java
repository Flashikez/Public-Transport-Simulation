/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects.enums;

/**
 *
 * @author MarekPC
 */
public enum Varianta_Vozidla {
    AUTOBUS_A, AUTOBUS_B, MIKROBUS;

    public int cena() {
        switch (this) {
            case AUTOBUS_A:
                return 545000;
            case AUTOBUS_B:
                return 320000;
            default:
                return 0;
        }
    }

    public int kapacita() {
        switch (this) {
            case AUTOBUS_A:
                return 186;
            case AUTOBUS_B:
                return 107;
            default:
                return 8;
        }
    }

    public int pocetDveri() {
        switch (this) {
            case AUTOBUS_A:
                return 4;
            case AUTOBUS_B:
                return 3;
            default:
                return 1;
        }
    }

    public String toString() {
        switch (this) {
            case AUTOBUS_A:
                return "Vacsi autobus";
            case AUTOBUS_B:
                return "Mensi autobus";
            default:
                return "Mikrobus";

        }
    }

}
