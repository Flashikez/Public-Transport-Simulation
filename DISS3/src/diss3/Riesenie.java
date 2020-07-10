/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diss3;

import OSPStat.Stat;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import objects.Config;
import objects.Utilities;
import objects.Vozidlo;

/**
 *
 * @author MarekPC
 */
public class Riesenie {

    public Config config;
    public double pocetCelkovo, pocetStadion;
    public double percentoNeskoro, dobaCakania;
    public double[] percentoNeskoroIS, dobaCakaniaIS;
    public double percentoA, cakanieA, percentoB, cakanieB, percentoC, cakanieC;
    public double[] percentoAIS, cakanieAIS, percentoBIS, cakanieBIS, percentoCIS, cakanieCIS;
    
    public double ziskA,ziskB,ziskC;

    private final double acceptableThreshHold_percento = 0;
    private final double acceptableThreshHold_cakanie = 0;
    public Map<Vozidlo, Stat> statistikyVozidiel;

    public Riesenie(Config config, double percentoNeskoro, double dobaCakania, double percentoA, double cakanieA, double percentoB, double cakanieB, double percentoC, double cakanieC,
            double[] cakanieIS, double[] percentoIS, double[] cakAIS, double[] percAIS, double[] cakBIS, double[] percBIS, double[] cakCIS, double[] percCIS, double pC, double pS,
            Map<Vozidlo, Stat> voz,double mA,double mB,double mC) {
        this.config = config;
        this.percentoNeskoro = percentoNeskoro;
        this.dobaCakania = dobaCakania;
        this.percentoA = percentoA;
        this.cakanieA = cakanieA;
        this.percentoB = percentoB;
        this.cakanieB = cakanieB;
        this.percentoC = percentoC;
        this.cakanieC = cakanieC;
        this.dobaCakaniaIS = cakanieIS;
        this.percentoNeskoroIS = percentoIS;
        this.percentoAIS = percAIS;
        this.cakanieAIS = cakAIS;
        this.percentoBIS = percBIS;
        this.cakanieBIS = cakBIS;
        this.percentoCIS = percCIS;
        this.cakanieCIS = cakCIS;
        this.pocetCelkovo = pC;
        this.pocetStadion = pS;
        statistikyVozidiel = voz;
        ziskA = mA;
        ziskB = mB;
        ziskC = mC;
    }

    public boolean vyhovujeNaLinkeA() {
//                System.out.println(percentoA+"     "+cakanieA);
        return (percentoA <= 7.0 + acceptableThreshHold_percento && cakanieA <= 10.0 + acceptableThreshHold_cakanie);
    }

    public boolean vyhovujeNaLinkeB() {
        return (percentoB <= 7.0 + acceptableThreshHold_percento && cakanieB <= 10.0 + acceptableThreshHold_cakanie);
    }

    public boolean vyhovujeNaLinkeC() {
        System.out.println(percentoC + "     " + cakanieC);
        return (percentoC <= 7.0 + acceptableThreshHold_percento && cakanieC <= 10.0 + acceptableThreshHold_cakanie);
    }

    public boolean vyhovujeCelkovo() {
        System.out.println(percentoNeskoro + "     " + dobaCakania);
        return (percentoNeskoro <= 7.0 && dobaCakania <= 10.0);
    }

    public Riesenie porovnajAdajLepsiuCenu(Riesenie other) {
        return (config.cenaVozidiel() <= other.config.cenaVozidiel()) ? this : other;

    }

    public boolean jeCasovoLepsieAko(Riesenie other) {
        if (percentoNeskoro < other.percentoNeskoro && dobaCakania <= other.dobaCakania) {
            return true;
        }
        if (dobaCakania < other.dobaCakania && percentoNeskoro <= other.percentoNeskoro) {
            return true;
        }
        return false;
    }

    public void zapisCSV(String fileName) {
        // START ZAPASU JE PRI HLADANI 13:00
        /* Tvar zaznamu
        **;**;**;**;**; ZAZNAM ;**;**;**;**;**;**
        Varianta Nastupov
        Vozidla na linke A (pocet): ;  Vozidla na linke B (pocet): ; Vozidla na linke C (pocet):
        Varianta vozidla Start_Zastavka casStart; Varianta vozidla Start_Zastavka casStart; Varianta vozidla Start_Zastavka casStart;
        .
        .
        Linka;Čakanie cestujuceho na linke;IS;Percento neskorych prichodov na linke;IS
        A; čakanie;cakanieInterval;percento;percentoInterval
        B; čakanie;cakanieInterval;percento;percentoInterval
        C; čakanie;cakanieInterval;percento;percentoInterval
        
        Celkové priemerné čakanie cestujúceho;IS;Celkové priemerné percento neskoro;IS
        čak;čakIS;perc;percIS
        Počet celkových cestujúcich;Počet cestujúcich na štadióne
        poč,poč
        **;**;**;**;**; END ZAZNAM ;**;**;**;**;**;**
         */
        FileWriter fw = null;
        try {
            fw = new FileWriter(fileName, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);
            List<Vozidlo> vozA = config.vozidlaLinkaA;
            List<Vozidlo> vozB = config.vozidlaLinkaB;
            List<Vozidlo> vozC = config.vozidlaLinkaC;
            out.println("*********ZAZNAM***********");
            out.println(config.variantaNastupov.toString());
            out.println("Vozidla na linke A  " + vozA.size() + ";  Vozidla na linke B  " + vozB.size() + "; Vozidla na linke C  " + vozC.size());
            int maxIndex = Math.max(Math.max(vozA.size(), vozB.size()), vozC.size());
            for (int i = 0; i < maxIndex; i++) {

                Vozidlo a = vozA.size() > i ? vozA.get(i) : null;
                Vozidlo b = vozB.size() > i ? vozB.get(i) : null;
                Vozidlo c = vozC.size() > i ? vozC.get(i) : null;
                String aString = a != null ? "" + a.getVarianta().toString() + " " + a.startZastavka.nazov() + " " + Utilities.toTimeString(a.startCas) : " ";
                String bString = b != null ? "" + b.getVarianta().toString() + " " + b.startZastavka.nazov() + " " + Utilities.toTimeString(b.startCas) : " ";
                String cString = c != null ? "" + c.getVarianta().toString() + " " + c.startZastavka.nazov() + " " + Utilities.toTimeString(c.startCas) : " ";
//                String aString = a != null ? "" + a.getId() + " " + a.getVarianta().toString() + " " + String.format("%.2f", statistikyVozidiel.get(a).mean()) : " ";
//                String bString = b != null ? "" + b.getId() + " " + b.getVarianta().toString() + " " + String.format("%.2f", statistikyVozidiel.get(b).mean()) : " ";
//                String cString = c != null ? "" + c.getId() + " " + c.getVarianta().toString() + " " + String.format("%.2f", statistikyVozidiel.get(c).mean()) : " ";

                out.println(aString + ";" + bString + ";" + cString);

            }
            out.println("Cena vozidiel: " + config.cenaVozidiel());
            out.println("Linka;Cakanie cestujuceho na linke IS;Percento neskorych prichodov na linke IS");
            String cakanieA = String.format("%.4f", Utilities.roundDouble(this.cakanieA, 4));
            String percentoA = String.format("%.4f", Utilities.roundDouble(this.percentoA, 4));

            String cakanieB = String.format("%.4f", Utilities.roundDouble(this.cakanieB, 4));
            String percentoB = String.format("%.4f", Utilities.roundDouble(this.percentoB, 4));
            String cakanieC = String.format("%.4f", Utilities.roundDouble(this.cakanieC, 4));
            String percentoC = String.format("%.4f", Utilities.roundDouble(this.percentoC, 4));

            out.println("A;" + cakanieA + " " + makeISstring(cakanieAIS, false) + ";" + percentoA + " " + makeISstring(percentoAIS, true));
            out.println("B;" + cakanieB + " " + makeISstring(cakanieBIS, false) + ";" + percentoB + " " + makeISstring(percentoBIS, true));
            out.println("C;" + cakanieC + " " + makeISstring(cakanieCIS, false) + ";" + percentoC + " " + makeISstring(percentoCIS, true));
            out.println("Linka;Zisk mikrobusov");
            out.println("A;"+ziskA);
            out.println("B;"+ziskB);
            out.println("C;"+ziskC);
            out.println("Celkove priemerne cakanie cestujuceho;Celkove priemerne percento neskoro");
            String cak = String.format("%.4f", Utilities.roundDouble(this.dobaCakania, 4));
            String perc = String.format("%.4f", Utilities.roundDouble(this.percentoNeskoro, 4));
            out.println(cak + " " + makeISstring(dobaCakaniaIS, false) + ";" + perc + " " + makeISstring(percentoNeskoroIS, true));
            out.println("Pocet celkovych cestujucich;Pocet cestujucich na stadione");
            out.println(String.format("%.3f", this.pocetCelkovo) + ";" + String.format("%.3f", this.pocetStadion));
            out.println("*********END ZAZNAM***********");
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(HladacRieseni.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    private String makeISstring(double[] IS, boolean percento) {
        double A = Utilities.roundDouble(IS[0], 3);
        double B = Utilities.roundDouble(IS[1], 3);
        if (percento) {
            A = Utilities.roundDouble(IS[0] * 100, 3);
            B = Utilities.roundDouble(IS[1] * 100, 3);
        }

        String As = String.format("%.3f", A);
        String Bs = String.format("%.3f", B);

        return "<" + As + " , " + Bs + ">";

    }

}
