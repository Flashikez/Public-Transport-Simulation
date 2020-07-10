/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import objects.enums.Varianta_Nastupov;
import objects.enums.Varianta_Vozidla;

/**
 *
 * @author MarekPC
 */
public class Config {

    private double startZapasu;

    // Volane po replikacii na resetovanie statistik o vozidle a linke
    public void resetState() {
        for (Vozidlo vozidlo : vozidlaLinkaA) {
            vozidlo.resetState();

        }
        for (Vozidlo vozidlo : vozidlaLinkaB) {
            vozidlo.resetState();

        }
        for (Vozidlo vozidlo : vozidlaLinkaC) {
            vozidlo.resetState();
        }

        for (Zastavka zastavka : Linka_A.listA) {
            zastavka.resetState();
        }
        for (Zastavka zastavka : Linka_B.listB) {
            zastavka.resetState();
        }
        for (Zastavka zastavka : Linka_C.listC) {
            zastavka.resetState();
        }

    }

    public double casStartZapasu() {
        return startZapasu;
    }

    public List<Vozidlo> vozidlaLinkaA, vozidlaLinkaB, vozidlaLinkaC;
    public Varianta_Nastupov variantaNastupov;
    final HashMap<Linka, Double> casyNaStart = new HashMap<>();

    public double dajCasNaStart(Linka link) {
        return casyNaStart.get(link);
    }

    public double cenaVozidiel() {
        double cena = 0;
        for (Vozidlo vozidlo : vozidlaLinkaA) {

            cena += vozidlo.varianta().cena();
        }
        for (Vozidlo vozidlo : vozidlaLinkaB) {

            cena += vozidlo.varianta().cena();
        }
        for (Vozidlo vozidlo : vozidlaLinkaC) {

            cena += vozidlo.varianta().cena();
        }
        return cena;
    }

    public Config(double startZapasu, List<Vozidlo> linkaA, List<Vozidlo> linkaB, List<Vozidlo> linkaC, Varianta_Nastupov varianta) {

        this.variantaNastupov = varianta;
        this.startZapasu = startZapasu;
        this.vozidlaLinkaA = linkaA;
        this.vozidlaLinkaB = linkaB;
        this.vozidlaLinkaC = linkaC;

        casyNaStart.put(Linka_A.linkaA, 25.0);
        casyNaStart.put(Linka_B.linkaB, 10.0);
        casyNaStart.put(Linka_C.linkaC, 30.0);

    }

    // Pre účely pridávania vozidiel v nastavení konfigurácie
    public int maxVozidloID() {
        int max = Integer.MIN_VALUE;

        for (Vozidlo vozidlo : vozidlaLinkaA) {
            if (max < vozidlo.getId()) {
                max = vozidlo.getId();
            }

        }
        for (Vozidlo vozidlo : vozidlaLinkaB) {
            if (max < vozidlo.getId()) {
                max = vozidlo.getId();
            }

        }
        for (Vozidlo vozidlo : vozidlaLinkaC) {
            if (max < vozidlo.getId()) {
                max = vozidlo.getId();
            }

        }
        return max < 0 ? 0 : max;

    }

    public static List<Config> loadSavedConfigs(String fileName) throws IOException {
        File file = new File("./"+fileName);

        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        List<Config> configs = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            String casZapasu = line;
            String variantaNastupov = br.readLine();
            int pocetVozidielLinkaA = Integer.parseInt(br.readLine().split(";")[1]);
            List<String> linkaA = new ArrayList<>();
            for (int i = 0; i < pocetVozidielLinkaA; i++) {
                linkaA.add(br.readLine());
            }
            int pocetVozidielLinkaB = Integer.parseInt(br.readLine().split(";")[1]);
            List<String> linkaB = new ArrayList<>();
            for (int i = 0; i < pocetVozidielLinkaB; i++) {
                linkaB.add(br.readLine());
            }
            int pocetVozidielLinkaC = Integer.parseInt(br.readLine().split(";")[1]);
            List<String> linkaC = new ArrayList<>();
            for (int i = 0; i < pocetVozidielLinkaC; i++) {
                linkaC.add(br.readLine());
            }
            configs.add(loadConfig(casZapasu, variantaNastupov, linkaA, linkaB, linkaC));

        }
        return configs;

    }

    public static void saveConfig(String fileName, Config conf) throws IOException {
        FileWriter fw = new FileWriter("./"+fileName, true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw);
        out.println(conf.startZapasu);
        //more code
        out.println(conf.variantaNastupov == Varianta_Nastupov.ODCHOD_HNED ? "A" : "B");
        out.println("LINKA A;" + conf.vozidlaLinkaA.size());
        for (Vozidlo vozidlo : conf.vozidlaLinkaA) {
            String variantaString = "A";
            if (vozidlo.variantaVozidla == Varianta_Vozidla.AUTOBUS_B) {
                variantaString = "B";
            } else if (vozidlo.variantaVozidla == Varianta_Vozidla.MIKROBUS) {
                variantaString = "M";
            }

            out.println(vozidlo.getId() + ";" + variantaString + ";" + vozidlo.startZastavka + ";" + vozidlo.startCas);
        }
        out.println("LINKA B;" + conf.vozidlaLinkaB.size());
        for (Vozidlo vozidlo : conf.vozidlaLinkaB) {
            String variantaString = "A";
            if (vozidlo.variantaVozidla == Varianta_Vozidla.AUTOBUS_B) {
                variantaString = "B";
            } else if (vozidlo.variantaVozidla == Varianta_Vozidla.MIKROBUS) {
                variantaString = "M";
            }

            out.println(vozidlo.getId() + ";" + variantaString + ";" + vozidlo.startZastavka + ";" + vozidlo.startCas);
        }
        out.println("LINKA C;" + conf.vozidlaLinkaC.size());
        for (Vozidlo vozidlo : conf.vozidlaLinkaC) {
            String variantaString = "A";
            if (vozidlo.variantaVozidla == Varianta_Vozidla.AUTOBUS_B) {
                variantaString = "B";
            } else if (vozidlo.variantaVozidla == Varianta_Vozidla.MIKROBUS) {
                variantaString = "M";
            }

            out.println(vozidlo.getId() + ";" + variantaString + ";" + vozidlo.startZastavka + ";" + vozidlo.startCas);
        }
        out.close();

        //more code
    }

    public static Config loadConfig(String casZapasu, String variantaNastupov, List<String> linkaA, List<String> linkaB, List<String> linkaC) {
        /*
        Tvar konfigu:
        casZapasu
        A/B (varianta)
        LINKA A;početVozidiel
        IDvozidla;A/B/M;nazovZastavky;čas
        IDvozidla;A/B/M;nazovZastavky;čas
        LINKA B;početVozidiel
        IDvozidla;A/B/M;nazovZastavky;čas
        IDvozidla;A/B/M;nazovZastavky;čas
         LINKA C;početVozidiel
        IDvozidla;A/B/M;nazovZastavky;čas
        IDvozidla;A/B/M;nazovZastavky;čas       
         */
        double cas = Double.parseDouble(casZapasu);
        Varianta_Nastupov var = Varianta_Nastupov.ODCHOD_HNED;
        if (variantaNastupov.compareTo("B") == 0) {
            var = Varianta_Nastupov.POCKAJ_90_SEKUND;
        }
        List<Vozidlo> linkaAvoz = new ArrayList<>();

        for (String strLinka : linkaA) {
            String[] split = strLinka.split(";");
            int ID = Integer.parseInt(split[0]);
            Varianta_Vozidla varVozidla = Varianta_Vozidla.AUTOBUS_A;
            String varStr = split[1];
            if (varStr.compareTo("B") == 0) {
                varVozidla = Varianta_Vozidla.AUTOBUS_B;
            } else if (varStr.compareTo("M") == 0) {
                varVozidla = Varianta_Vozidla.MIKROBUS;

            }
            Zastavka zastavka = Config.findZastavkaPodlaNazvuLinkaA(split[2]);
            double startTime = Double.parseDouble(split[3]);
            Vozidlo voz;
            if (varVozidla == Varianta_Vozidla.AUTOBUS_A || varVozidla == Varianta_Vozidla.AUTOBUS_B) {
                voz = new Autobus(ID, varVozidla, zastavka, startTime);
            } else {
                voz = new Mikrobus(ID, varVozidla, zastavka, startTime);
            }
            linkaAvoz.add(voz);

        }
        List<Vozidlo> linkaBvoz = new ArrayList<>();

        for (String strLinka : linkaB) {
            String[] split = strLinka.split(";");
            int ID = Integer.parseInt(split[0]);
            Varianta_Vozidla varVozidla = Varianta_Vozidla.AUTOBUS_A;
            String varStr = split[1];
            if (varStr.compareTo("B") == 0) {
                varVozidla = Varianta_Vozidla.AUTOBUS_B;
            } else if (varStr.compareTo("M") == 0) {
                varVozidla = Varianta_Vozidla.MIKROBUS;

            }
            Zastavka zastavka = Config.findZastavkaPodlaNazvuLinkaB(split[2]);
            double startTime = Double.parseDouble(split[3]);
            Vozidlo voz;
            if (varVozidla == Varianta_Vozidla.AUTOBUS_A || varVozidla == Varianta_Vozidla.AUTOBUS_B) {
                voz = new Autobus(ID, varVozidla, zastavka, startTime);
            } else {
                voz = new Mikrobus(ID, varVozidla, zastavka, startTime);
            }

            linkaBvoz.add(voz);

        }
        List<Vozidlo> linkaCvoz = new ArrayList<>();

        for (String strLinka : linkaC) {
            String[] split = strLinka.split(";");
            int ID = Integer.parseInt(split[0]);
            Varianta_Vozidla varVozidla = Varianta_Vozidla.AUTOBUS_A;
            String varStr = split[1];
            if (varStr.compareTo("B") == 0) {
                varVozidla = Varianta_Vozidla.AUTOBUS_B;
            } else if (varStr.compareTo("M") == 0) {
                varVozidla = Varianta_Vozidla.MIKROBUS;

            }
            Zastavka zastavka = Config.findZastavkaPodlaNazvuLinkaC(split[2]);
            double startTime = Double.parseDouble(split[3]);
            Vozidlo voz;
            if (varVozidla == Varianta_Vozidla.AUTOBUS_A || varVozidla == Varianta_Vozidla.AUTOBUS_B) {
                voz = new Autobus(ID, varVozidla, zastavka, startTime);
            } else {
                voz = new Mikrobus(ID, varVozidla, zastavka, startTime);
            }
            linkaCvoz.add(voz);

        }
        return new Config(cas, linkaAvoz, linkaBvoz, linkaCvoz, var);

    }

    public String toString() {
        return "Konfig " + vozidlaLinkaA.size() + "  " + vozidlaLinkaB.size() + "  " + vozidlaLinkaC.size();
    }

    public static Config makeDefault() {
        double startZapasu = 780.0;
        Autobus a1 = new Autobus(1, Varianta_Vozidla.AUTOBUS_A, Config.stadion, startZapasu - 50);
        Autobus a2 = new Autobus(2, Varianta_Vozidla.AUTOBUS_A, Linka_A.AF, startZapasu - 25);
        Autobus a3 = new Autobus(3, Varianta_Vozidla.AUTOBUS_B, Linka_A.K1A, startZapasu - 70);

        Autobus b1 = new Autobus(4, Varianta_Vozidla.AUTOBUS_B, stadion, startZapasu - 75);
        Mikrobus c1 = new Mikrobus(5, Varianta_Vozidla.MIKROBUS, Linka_C.CA, startZapasu - 200);

        return new Config(startZapasu, Arrays.asList(a1, a2, a3), Arrays.asList(b1), Arrays.asList(c1), Varianta_Nastupov.ODCHOD_HNED);
    }
    public final static double startPrichoduOffset = 75;
    public final static double koniecPrichoduOffset = 10;
    public final static String saveFileName = "savedConfigs.txt";

    public final static Stadion stadion;
    public final static Pool_Ludia_Zastavka ludiaStadion = new Pool_Ludia_Zastavka(-1);
    public final static Pool_Vozidla_Zastavka vozidlaStadion = new Pool_Vozidla_Zastavka();
    public final static Pool_Ludia_Zastavka ludiaK1 = new Pool_Ludia_Zastavka(260);
    public final static Pool_Ludia_Zastavka ludiaK2 = new Pool_Ludia_Zastavka(210);
    public final static Pool_Ludia_Zastavka ludiaK3 = new Pool_Ludia_Zastavka(220);

    public final static Pool_Vozidla_Zastavka vozidlaK1 = new Pool_Vozidla_Zastavka();
    public final static Pool_Vozidla_Zastavka vozidlaK2 = new Pool_Vozidla_Zastavka();
    public final static Pool_Vozidla_Zastavka vozidlaK3 = new Pool_Vozidla_Zastavka();

    static {
        stadion = new Stadion("Stadion", -1, ludiaStadion, vozidlaStadion);
    }

    // kvoli čítaní konfigu
    public static Zastavka findZastavkaPodlaNazvuLinkaA(String nazov) {
        for (Zastavka zastavka : Linka_A.listA) {
            if (zastavka.nazov().compareTo(nazov) == 0) {
                return zastavka;
            }
        }

        return null;

    }

    public static Zastavka findZastavkaPodlaNazvuLinkaB(String nazov) {

        for (Zastavka zastavka : Linka_B.listB) {
            if (zastavka.nazov().compareTo(nazov) == 0) {
                return zastavka;
            }
        }
        return null;

    }

    public static Zastavka findZastavkaPodlaNazvuLinkaC(String nazov) {

        for (Zastavka zastavka : Linka_C.listC) {
            if (zastavka.nazov().compareTo(nazov) == 0) {
                return zastavka;
            }
        }
        return null;

    }

    public static class Linka_A {

        public final static Zastavka AA = new Zastavka("AA", 3.2, 123);
        public final static Zastavka AB = new Zastavka("AB", 2.3, 92);
        public final static Zastavka AC = new Zastavka("AC", 2.1, 241);
        public final static Zastavka AD = new Zastavka("AD", 1.2, 123);
        public final static Zastavka K1A = new Zastavka("K1", 5.4, ludiaK1, vozidlaK1);
        public final static Zastavka AE = new Zastavka("AE", 2.9, 215);
        public final static Zastavka AF = new Zastavka("AF", 3.4, 245);
        public final static Zastavka AG = new Zastavka("AG", 1.8, 137);
        public final static Zastavka K3A = new Zastavka("K3", 4.0, ludiaK3, vozidlaK3);
        public final static Zastavka AH = new Zastavka("AH", 1.6, 132);
        public final static Zastavka AI = new Zastavka("AI", 4.6, 164);
        public final static Zastavka AJ = new Zastavka("AJ", 3.4, 124);
        public final static Zastavka AK = new Zastavka("AK", 1.2, 213);
        public final static Zastavka AL = new Zastavka("AL", 0.9, 185);
        public final static List<Zastavka> listA = Arrays.asList(AA, AB, AC, AD, K1A, AE, AF, AG, K3A, AH, AI, AJ, AK, AL, stadion);
        public static Linka linkaA = new Linka("Linka A", listA);

    }

    public static class Linka_B {

        public final static Zastavka BA = new Zastavka("BA", 1.2, 79);
        public final static Zastavka BB = new Zastavka("BB", 2.3, 69);
        public final static Zastavka BC = new Zastavka("BC", 3.2, 43);
        public final static Zastavka BD = new Zastavka("BD", 4.3, 127);
        public final static Zastavka K2B = new Zastavka("K2", 1.2, ludiaK2, vozidlaK2);
        public final static Zastavka BE = new Zastavka("BE", 2.7, 30);
        public final static Zastavka BF = new Zastavka("BF", 3.0, 69);
        public final static Zastavka K3B = new Zastavka("K3", 6.0, ludiaK3, vozidlaK3);
        public final static Zastavka BG = new Zastavka("BG", 4.3, 162);
        public final static Zastavka BH = new Zastavka("BH", 0.5, 90);
        public final static Zastavka BI = new Zastavka("BI", 2.7, 148);
        public final static Zastavka BJ = new Zastavka("BJ", 1.3, 171);

        public final static List<Zastavka> listB = Arrays.asList(BA, BB, BC, BD, K2B, BE, BF, K3B, BG, BH, BI, BJ, stadion);
        public static Linka linkaB = new Linka("Linka B", listB);

    }

    public static class Linka_C {

        public final static Zastavka CA = new Zastavka("CA", 0.6, 240);
        public final static Zastavka CB = new Zastavka("CB", 2.3, 310);
        public final static Zastavka K1C = new Zastavka("K1", 4.1, ludiaK1, vozidlaK1);
        public final static Zastavka K2C = new Zastavka("K2", 6.0, ludiaK2, vozidlaK2);
        public final static Zastavka CC = new Zastavka("CC", 2.3, 131);
        public final static Zastavka CD = new Zastavka("CD", 7.1, 190);
        public final static Zastavka CE = new Zastavka("CE", 4.8, 132);
        public final static Zastavka CF = new Zastavka("CF", 3.7, 128);
        public final static Zastavka CG = new Zastavka("CG", 7.2, 70);

        public final static List<Zastavka> listC = Arrays.asList(CA, CB, K1C, K2C, CC, CD, CE, CF, CG, stadion);
        public static Linka linkaC = new Linka("Linka C", listC);

    }

}
