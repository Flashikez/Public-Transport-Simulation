/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import OSPDataStruct.SimQueue;
import OSPStat.Stat;
import java.util.ArrayList;
import java.util.List;
import objects.enums.Activity;
import objects.enums.Varianta_Vozidla;
import simulation.MyMessage;

/**
 *
 * @author MarekPC
 */
public class Vozidlo {

    SimQueue<MyMessage> ludiaVoVozidle;
    Activity aktivita;
    Varianta_Vozidla variantaVozidla;
    int pocetVolnychDveri;
    boolean stavDveri[];
    public int celkovyPocetCoIsloVozidlom;
    public boolean cakalUzVariantaB;
    public boolean cakaVariantaB;
    public Stat cakanieLudiCoNastupiliDoVozidla;
    public boolean dopravaZastavena;

    public Zastavka startZastavka;
    public double startCas;

    //Na progress
    //LEN NA UCELY VYPISU
    Zastavka dalsiaZastavka;
    double casPrichoduKdalsej, rozsahZmeny;
    //

    int idVozidla;
    public double ziskMikrobusu;

    public void resetState() {
        aktivita = Activity.CAKA;
        ludiaVoVozidle.clear();
        pocetVolnychDveri = variantaVozidla.pocetDveri();
        stavDveri = new boolean[pocetVolnychDveri];
        casPrichoduKdalsej = 0;
        rozsahZmeny = 0;
        dalsiaZastavka = null;
        celkovyPocetCoIsloVozidlom = 0;
        cakalUzVariantaB = false;
        cakaVariantaB = false;
        cakanieLudiCoNastupiliDoVozidla.clear();
        dopravaZastavena = false;
        ziskMikrobusu = 0;
    }

    public Vozidlo(int id, Varianta_Vozidla varianta, Zastavka startZ, double startT) {
        aktivita = Activity.CAKA;
        ludiaVoVozidle = new SimQueue<>();
        this.variantaVozidla = varianta;
        pocetVolnychDveri = variantaVozidla.pocetDveri();
        idVozidla = id;
        stavDveri = new boolean[pocetVolnychDveri];
        cakanieLudiCoNastupiliDoVozidla = new Stat();
        this.startZastavka = startZ;
        this.startCas = startT;
    }

    public void pridajCestujuceho(MyMessage msg) {
        ludiaVoVozidle.enqueue(msg);
        celkovyPocetCoIsloVozidlom++;
    }

    public MyMessage dajCestujuceho() {
        return ludiaVoVozidle.peek();
    }

    public MyMessage odoberCestujuceho() {
        if (pocetLudi() == 0) {
            return null;
        }
        return ludiaVoVozidle.dequeue();
    }

    public void setAktivita(Activity act) {
        aktivita = act;
    }

    public void setAktivitaVariantaBCakanie(Activity act,double startTime, double endTime){
        aktivita = act;
        casPrichoduKdalsej = endTime;
        rozsahZmeny = casPrichoduKdalsej - startTime;
    }
    
    public Activity aktivita() {
        return aktivita;
    }

    public void setDalsia(Zastavka dalsia, double prichod, double casZmeny) {
        dalsiaZastavka = dalsia;
        casPrichoduKdalsej = prichod;
        rozsahZmeny = casPrichoduKdalsej - casZmeny;
    }

    private String stateString() {
        String retString = "";
        switch (aktivita) {
            case CAKA:

                retString = "čaká na štart , bude štartovať na " + dalsiaZastavka.nazov + " o " + objects.Utilities.toTimeString(casPrichoduKdalsej);
                break;
            case CESTUJE:
                retString = "je na ceste k zastavke " + dalsiaZastavka.nazov;
                break;
            default:
                retString = aktivita.toString();
                break;
        }
        return retString;
    }

    public String progressPercentoString(double currentTime) {
        double currentRozsah = casPrichoduKdalsej - currentTime;
        return (int) (100 - objects.Utilities.roundDouble((currentRozsah / rozsahZmeny), 2) * 100) + "%";
    }

    public void obsadDvere(int indexDveri) {
        // FALSE = volne
        stavDveri[indexDveri] = true;
        pocetVolnychDveri--;

    }

    public void uvolniDvere(int indexDveri) {
        stavDveri[indexDveri] = false;
        pocetVolnychDveri++;
    }

    public boolean volneDvere() {
        return pocetVolnychDveri > 0;
    }

    public boolean vsetkyDvereVolne() {
        for (int i = 0; i < stavDveri.length; i++) {
            if (stavDveri[i]) {
                return false;
            }

        }
        return true;
    }

    public int pocetLudi() {
        return ludiaVoVozidle.size();
    }

    public boolean plny() {
        return pocetLudi() >= this.variantaVozidla.kapacita();
    }

    public int volneMiesta() {
        return variantaVozidla.kapacita() - pocetLudi();
    }

    public List<Integer> indexyVolnychDveri() {
        List<Integer> arr = new ArrayList<>();
        for (int i = 0; i < stavDveri.length; i++) {
            if (!stavDveri[i]) {
                arr.add(i);
            }
        }
        return arr;

    }

    public boolean zmestiSaJeden() {
        return volneMiesta() > 0;
    }

    public int pocetVolnychDveri() {
        return indexyVolnychDveri().size();
    }

    public int pocetDveri() {
        return this.variantaVozidla.pocetDveri();
    }

    @Override
    public String toString() {
        return "Vozidlo";
    }

    // tablesProperties---------------
    public int getId() {
        return idVozidla;
    }

    public String getTyp() {
        return this.toString();
    }

    public Varianta_Vozidla varianta() {
        return this.variantaVozidla;
    }

    public String getVarianta() {
        return this.variantaVozidla.toString();
    }

    public String getAktivita() {
        return stateString();
    }

    public double getProgress(double currentTime) {
        if (aktivita == Activity.CAKA || aktivita == Activity.CESTUJE || aktivita == Activity.CAKA_NASTUPY) {
            double currentRozsah = casPrichoduKdalsej - currentTime;
            return (1 - objects.Utilities.roundDouble((currentRozsah / rozsahZmeny), 2));
        } else {
            return 0;
        }

    }

    public int stavDveri(int indexDveri) {
        //-1 nema 0 zatvorene 1 otvorene
        if (indexDveri >= stavDveri.length) {
            return -1;
        }
        if (!stavDveri[indexDveri]) {
            return 1;
        } else {
            return 0;
        }

    }

    public String getObsadenost() {
        return pocetLudi() + "/" + this.variantaVozidla.kapacita();
    }

    //-----------------------------
}
