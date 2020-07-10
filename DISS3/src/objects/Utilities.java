/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import javafx.util.Pair;
import objects.Linka;
import objects.Vozidlo;
import objects.Zastavka;

/**
 *
 * @author MarekPC
 */
public class Utilities {

    public static String toTimeString(double mins) {

        int totalSecs = (int) (mins * 60);

        int hours = totalSecs / 3600;
        int minutes = (totalSecs % 3600) / 60;
        int seconds = totalSecs % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static double stringToDoubleMins(String str) {
        String splitString[] = str.split(":");
        int hours = Integer.parseInt(splitString[0]);
        int minuties = Integer.parseInt(splitString[1]);
        int seconds = Integer.parseInt(splitString[2]);

        return hours * 60.0 + minuties + (double) seconds / 60.0;

    }

    public static double roundDouble(double value, int decimalPoints) {
        double mocnina = Math.pow(10, decimalPoints);
        return Math.round(value * mocnina) / mocnina;
    }

    public static <T, V> Pair<T, V> pair(T t, V v) {
        return new Pair<>(t, v);

    }

    public static class StateCalc {

        public static int pocetNaZastavkach(Linka... linky) {
            int ret = 0;

            for (Linka linka1 : linky) {
                for (Zastavka zastavka : linka1.listZastavky()) {
                    ret += zastavka.pocetCestujucich();

                }

            }
            return ret;
        }

        public static int pocetVoVozidlach(Linka... linky) {
            int ret = 0;

            for (Linka linka1 : linky) {
                for (Vozidlo vozidlo : linka1.priradeneVozidla()) {
                    ret += vozidlo.pocetLudi();

                }

            }
            return ret;
        }
    }
    
    public class SolutionFinder{
        
        // Sprav config, sprav replikacie, zisti percento co prisli neskoro..
        // Ak je percento <= 7, && priemrny cas cakania na bus <= 10min
        
        //zapis config do csv a zapis aj statistiky.
        String fileNameVariantaA = "vysledkyBezMikrobusovVariantaA.csv";
        String fileNameVariantaB = "vysledkyBezMikrobusovVariantaB.csv";
        //----
        
        
        
        
    
    
    }
    
}
