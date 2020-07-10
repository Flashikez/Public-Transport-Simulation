/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MarekPC
 */
class Pool_Vozidla_Zastavka {

    List<Vozidlo> vozidlaNaZastavke;

    public Pool_Vozidla_Zastavka() {
        vozidlaNaZastavke = new ArrayList<>();
    }

    public List<Vozidlo> dajList() {
        return vozidlaNaZastavke;
    }

    
    public boolean zastavkaPrazdna(){
        return vozidlaNaZastavke.isEmpty();
    }
            
    
    public void pridajVozidlo(Vozidlo voz) {
        if (!vozidlaNaZastavke.contains(voz)) {
            vozidlaNaZastavke.add(voz);
        }

    }

    public void odchodVozidla(Vozidlo voz) {
        if (vozidlaNaZastavke.contains(voz)) {
            vozidlaNaZastavke.remove(voz);
        }
    }

    public void reset() {
        vozidlaNaZastavke.clear();
    }

}
