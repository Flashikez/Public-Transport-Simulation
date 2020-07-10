/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import java.util.List;

/**
 *
 * @author MarekPC
 */
public class Linka {
    
    String nazov;
    List<Vozidlo> priradeneVozidla;
    List<Zastavka> zastavky;
    
    public Linka(String naz, List<Zastavka> zast) {
        nazov = naz;
        zastavky = zast;
        for (Zastavka zastavka : zastavky) {
            zastavka.setDobaKuStadionu(casDoStadiona(zastavka));
        }
        
    }
    
    public void priradVozidla(List<Vozidlo> vozidla) {
        priradeneVozidla = vozidla;
    }
    public List<Vozidlo> priradeneVozidla(){
        return priradeneVozidla;
    }
//    
    public Zastavka dalsiaZastavka(Zastavka zas) {
        int index = zastavky.indexOf(zas);
        if (index == zastavky.size() - 1) {
            return zastavky.get(0);
        }
        return zastavky.get(index + 1);
        
    }
    
    public List<Zastavka> listZastavky() {
        return zastavky;
    }
    
    public double casDoStadiona(Zastavka current) {
        boolean found = false;
        double casSum = 0;
        
        for (Zastavka zastavka : zastavky) {
            if (zastavka == current) {
                found = true;
            }
            
            if (found && !(zastavka instanceof Stadion)) {
                casSum += zastavka.dobaKdalsej;
            }
            
        }
//        if(current.nazov().compareTo("CA") == 0){
//            System.out.println("CA: "+casSum);
//        }
//        if(current.nazov().compareTo("AA") == 0){
//            System.out.println("AA: "+casSum);
//        }
//        if(current.nazov().compareTo("BA") == 0){
//            System.out.println("BA: "+casSum);
//        }
        return casSum;
    }
    
}
