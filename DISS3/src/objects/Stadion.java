/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

/**
 *
 * @author MarekPC
 */
public class Stadion extends Zastavka {

    public Stadion(String nazov, double dobaK,Pool_Ludia_Zastavka poolL, Pool_Vozidla_Zastavka poolV) {
        super(nazov, dobaK, poolL, poolV);
    }

    @Override
    public String getID() {
        return "Š";
    }

}
