/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diss3;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import objects.Config;
import objects.Linka;
import objects.Mikrobus;
import objects.Vozidlo;
import objects.Zastavka;
import objects.enums.Varianta_Nastupov;
import objects.enums.Varianta_Vozidla;
import simulation.MySimulation;

/**
 * V tejto triede som len skúšal hladat riesenia
 * @author MarekPC
 */
public class HladacRieseni {

    int idCounter = 0;

    public void pridajMikrobusyNaLinkyTest() throws IOException {
        //Najlpsi config bez mikrobusov
        Config cfg = Config.loadSavedConfigs("prvotnyConfig.txt").get(9);
        pridajMikrobusyNaLinkuA(cfg, 5,  5);
        pridajMikrobusyNaLinkuB(cfg, 3,  5);
        pridajMikrobusyNaLinkuC(cfg, 5,  5);
        Config.saveConfig("mikrobusyConfigs.txt", cfg);
        System.out.println("DONE");
    }
    
    
    public void testMikrobusyConfig(int confIndex) throws IOException{
        Config cfg = Config.loadSavedConfigs("mikrobusyConfigs.txt").get(confIndex);
        Riesenie ries = spravRiesenie(cfg, 100);
        ries.zapisCSV("mikrobusyStats.csv");
        System.out.println("DONE");
    }
    
    public void spojenePrvotneRiesenia() throws IOException {
//        for (int i = 0; i < 100; i++) {
        Config cfg = Config.loadSavedConfigs("prvotnyConfig.txt").get(9);
        Riesenie ries = spravRiesenie(cfg, 300);
//            if (ries.vyhovujeCelkovo()) {
        ries.zapisCSV("upravaRieseniaB.csv");
        System.out.println("DONE");
//            }
//           C System.out.println(i);
//        }

    }

    public void najdiPrvotneRieseniaPreLinky() throws IOException {
        for (int i = 0; i < 4; i++) {
            System.out.println("I: " + i);
            Riesenie ries = dajRieseniePreLinkuA();
            ries.zapisCSV("PrvotneRieseniaLinkaA_B.csv");
            Config.saveConfig("prvotneConfigyLinkaA_B.txt", ries.config);
        }

        for (int i = 0; i < 4; i++) {
            System.out.println("I: " + i);
            Riesenie ries = dajRieseniePreLinkuB();
            ries.zapisCSV("PrvotneRieseniaLinkaB_B.csv");
            Config.saveConfig("prvotneConfigyLinkaB_B.txt", ries.config);
        }
        for (int i = 0; i < 4; i++) {
            System.out.println("I: " + i);
            Riesenie ries = dajRieseniePreLinkuC();
            ries.zapisCSV("PrvotneRieseniaLinkaC_B.csv");
            Config.saveConfig("prvotneConfigyLinkaC_B.txt", ries.config);
        }

    }

    private Riesenie dajRieseniePreLinkuA() {
        int maxPocetVozidiel = 25;
        int minPocetVozidiel = 12;

        for (int i = minPocetVozidiel; i <= maxPocetVozidiel; i++) {
            System.out.println("I: " + i);
            for (int swap = 0; swap < 15; swap++) {
                idCounter = 0;
                List<Vozidlo> vozidlaA = vytvorRandomVozidla(i, 13 * 60, Config.Linka_A.linkaA, 667);
                Config conf = new Config(13 * 60, vozidlaA, new ArrayList<Vozidlo>(), new ArrayList<Vozidlo>(), Varianta_Nastupov.POCKAJ_90_SEKUND);
                Riesenie riesenie = spravRiesenie(conf, 20);
                if (riesenie.vyhovujeNaLinkeA()) {
                    System.out.println("MAM LINKU A " + i);
                    return riesenie;
                }
            }

        }
        return null;
    }

    private Riesenie dajRieseniePreLinkuB() {
        int maxPocetVozidiel = 25;
        int minPocetVozidiel = 4;

        for (int i = minPocetVozidiel; i <= maxPocetVozidiel; i++) {
            System.out.println("I: " + i);
            for (int swap = 0; swap < 15; swap++) {
                idCounter = 0;
                List<Vozidlo> vozidlaB = vytvorRandomVozidla(i, 13 * 60, Config.Linka_B.linkaB, 672.3);
                Config conf = new Config(13 * 60, new ArrayList<Vozidlo>(), vozidlaB, new ArrayList<Vozidlo>(), Varianta_Nastupov.POCKAJ_90_SEKUND);
                Riesenie riesenie = spravRiesenie(conf, 20);
                if (riesenie.vyhovujeNaLinkeB()) {
                    System.out.println("MAM LINKU B " + i);
                    return riesenie;
                }
            }

        }
        return null;
    }

    private Riesenie dajRieseniePreLinkuC() {
        int maxPocetVozidiel = 30;
        int minPocetVozidiel = 15;

        for (int i = minPocetVozidiel; i <= maxPocetVozidiel; i++) {
            System.out.println("I: " + i);
            for (int swap = 0; swap < 25; swap++) {
                idCounter = 0;
                List<Vozidlo> vozidlaC = vytvorRandomVozidla(i, 13 * 60, Config.Linka_C.linkaC, 666.9);
                Config conf = new Config(13 * 60, new ArrayList<Vozidlo>(), new ArrayList<Vozidlo>(), vozidlaC, Varianta_Nastupov.POCKAJ_90_SEKUND);
                Riesenie riesenie = spravRiesenie(conf, 20);
                if (riesenie.vyhovujeNaLinkeC()) {
                    System.out.println("MAM LINKU C " + i);
                    return riesenie;
                }
            }

        }
        return null;
    }

    private Riesenie spravRiesenie(Config config, int replications) {
        MySimulation sim = new MySimulation(config, -1);
        sim.simulate(replications);
        MySimulation.Sim_Replications_Stats results = sim.statistics;
        return new Riesenie(config, results.percentoCoPrisloNeskoro.mean() * 100, results.dobaCakaniaNaVozidlo.mean(),
                results.percentoNeskoroA.mean() * 100, results.cakanieA.mean(),
                results.percentoNeskoroB.mean() * 100, results.cakanieB.mean(),
                results.percentoNeskoroC.mean() * 100, results.cakanieC.mean(), results.dobaCakaniaNaVozidlo.confidenceInterval_90(), results.percentoCoPrisloNeskoro.confidenceInterval_90(),
                results.cakanieA.confidenceInterval_90(), results.percentoNeskoroA.confidenceInterval_90(),
                results.cakanieB.confidenceInterval_90(), results.percentoNeskoroB.confidenceInterval_90(),
                results.cakanieC.confidenceInterval_90(), results.percentoNeskoroC.confidenceInterval_90(), results.pocetVsetkychCestujucich.mean(), results.pocetNaStadione.mean(), results.poctyCestujucichVozidiel,
                results.ziskyMikrobusovNaLinkach.get(Config.Linka_A.linkaA).mean(),
                results.ziskyMikrobusovNaLinkach.get(Config.Linka_B.linkaB).mean(),
                results.ziskyMikrobusovNaLinkach.get(Config.Linka_C.linkaC).mean());

    }

    private List<Vozidlo> vytvorRandomVozidla(int i, double startZapasu, Linka linka, double startTime) {

        List<Vozidlo> vozidla = new ArrayList<>();
        for (int j = 1; j < i + 1; j++) {
//            int index = j% linka.listZastavky().size()-1 ;
            Zastavka zast = nahodnaZastavka(linka);
            double casDoStadiona = zast.dobaKuStadionu();
            vozidla.add(new Vozidlo(idCounter++, Varianta_Vozidla.AUTOBUS_A, zast, startTime + casDoStadiona));
        }
        return vozidla;
    }

    private Zastavka nahodnaZastavka(Linka linka) {

        List<Zastavka> zastavky = linka.listZastavky();
        Random rand = new Random();
        // bez štadiona
        return zastavky.get(rand.nextInt(zastavky.size() - 1));

    }

    private void pridajMikrobusyNaLinkuA(Config cfg, int pocet, double rozostup) {
        List<Vozidlo> vozidla = cfg.vozidlaLinkaA;
        double startTime = 667;
        for (int i = 0; i < pocet; i++) {
            vozidla.add(new Mikrobus(1000 + i, Varianta_Vozidla.MIKROBUS, (Config.Linka_A.AA), startTime));
            startTime += rozostup;
        }
    }

    private void pridajMikrobusyNaLinkuB(Config cfg, int pocet, double rozostup) {
        List<Vozidlo> vozidla = cfg.vozidlaLinkaB;
        double startTime = 672.3;
        for (int i = 0; i < pocet; i++) {
            vozidla.add(new Mikrobus(2000 + i, Varianta_Vozidla.MIKROBUS, (Config.Linka_B.BA), startTime));
            startTime += rozostup;
        }
    }

    private void pridajMikrobusyNaLinkuC(Config cfg, int pocet, double rozostup) {
        List<Vozidlo> vozidla = cfg.vozidlaLinkaC;
        double startTime = 666.9;
        for (int i = 0; i < pocet; i++) {
            vozidla.add(new Mikrobus(3000 + i, Varianta_Vozidla.MIKROBUS, (Config.Linka_C.CA), startTime));
            startTime += rozostup;
        }
    }
}
