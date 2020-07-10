/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import java.util.ArrayList;
import java.util.List;
import objects.enums.Activity;
import simulation.MyMessage;

/**
 *
 * @author MarekPC
 */
public class Zastavka {

    String nazov;
    double dobaKdalsej, dobaKuStadionu;
    boolean jeKrizovatka;
    Pool_Ludia_Zastavka poolLudi;
    Pool_Vozidla_Zastavka poolVozidla;

    public List<Vozidlo> dajVozidlaNaZastavke() {
        return poolVozidla.dajList();
    }

    public void resetState() {
        poolLudi.reset();
        poolVozidla.reset();

    }

    public Zastavka(String nazov, double dobaK, Pool_Ludia_Zastavka poolL, Pool_Vozidla_Zastavka poolV) {
        this.nazov = nazov;
        jeKrizovatka = true;
        poolLudi = poolL;
        poolVozidla = poolV;
        dobaKdalsej = dobaK;

    }

    public Zastavka(String nazov, double dobaK, int pocetOcakavanych) {
        this.nazov = nazov;
        jeKrizovatka = false;
        poolLudi = new Pool_Ludia_Zastavka(pocetOcakavanych);
        poolVozidla = new Pool_Vozidla_Zastavka();
        dobaKdalsej = dobaK;

    }

    public void pridajCestujuceho(MyMessage msg) {
        poolLudi.pridajCestujuceho(msg);

    }

    public void spustilGenerovanie() {
        poolLudi.generovanieSpustene = true;
    }

    public boolean generovanieSpustene() {
        return poolLudi.generovanieSpustene;
    }

    //kvoli zadaniu, ze viac ludi nemoze prist, menej moze
    public boolean vygenerovanychDost() {
        return poolLudi.vygenerovanychDost();
    }
    //

    public void pridajVozidlo(Vozidlo voz) {
        poolVozidla.pridajVozidlo(voz);
    }

    public void odchodVozidla(Vozidlo voz) {
        poolVozidla.odchodVozidla(voz);

    }

    public MyMessage peekPrvyCestujuci() {
        return poolLudi.peekPrveho();
    }

    public boolean zastavkaBezVozidiel() {
        return poolVozidla.zastavkaPrazdna();
    }

    public MyMessage odoberCestujuceho() {
        return poolLudi.dajCestujuceho();
    }

    public int pocetCestujucich() {
        return poolLudi.pocetLudi();
    }

    public int pocetOcakavanych() {
        return poolLudi.pocetOcakavanych;
    }

    public double dobaKuStadionu() {
        return dobaKuStadionu;
    }

    public void setDobaKuStadionu(double dobaKuStadionu) {
        this.dobaKuStadionu = dobaKuStadionu;
    }

    public String nazov() {
        return this.nazov;
    }

    public boolean krizuje() {
        return jeKrizovatka;
    }

    public double casKdalsej() {
        return dobaKdalsej;
    }

    public String toString() {
//        return "Zastavka " + nazov + " pocetLudi: " + pocetCestujucich();
        return nazov;
    }
    // Na účely tabulkového prístupi z GUI

    public String getVozidlaNaZastavkeString() {
        String ret = "";
        List<Vozidlo> vozidla = new ArrayList<>(poolVozidla.dajList());

        for (Vozidlo vozidlo : vozidla) {
            ret += "ID: " + vozidlo.getId() + "  obsadenosť: " + vozidlo.getObsadenost() + "\n";

        }
        return ret;
    }

    public int getPocetNaZastavke() {
        return pocetCestujucich();
    }

    public int getPocetCelkovo() {
        return poolLudi.pocetCelkovo;
    }

    public String getID() {
        return nazov;
    }

    public boolean cakaUzNejakyVariantaB() {
        for (Vozidlo vozidlo : poolVozidla.dajList()) {
            if (vozidlo.aktivita() == Activity.CAKA_NASTUPY) {
                return true;
            }

        }
        return false;

    }

}
