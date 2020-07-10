package continualAssistants;

import OSPABA.*;
import OSPRNG.TriangularRNG;
import OSPRNG.UniformContinuousRNG;
import agents.*;
import java.util.List;
import objects.Mikrobus;
import objects.Vozidlo;
import objects.Zastavka;
import objects.enums.Activity;
import objects.enums.Varianta_Nastupov;
import simulation.*;

//meta! id="74"
public class Nastup_Cestujucich extends OSPABA.Process {

    TriangularRNG nastupAutobusRNG;
    UniformContinuousRNG nastupMikrobusRNG;
    double cakanieNaDalsich_Varianta_B;
    double dlzkaCakania_Nastup_Mikrobus;

    public Nastup_Cestujucich(int id, Simulation mySim, CommonAgent myAgent) {
        super(id, mySim, myAgent);
        nastupAutobusRNG = new TriangularRNG(0.01, 0.07, 0.02, ((MySimulation) mySim()).seedGenerator);

        nastupMikrobusRNG = new UniformContinuousRNG(6.0 / 60.0, 10.0 / 60.0, ((MySimulation) mySim()).seedGenerator);

        cakanieNaDalsich_Varianta_B = 1.5;
        dlzkaCakania_Nastup_Mikrobus = 6.0;
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication

    }

    //meta! sender="Zastavky_Agent", id="75", type="Start"
    public void processStart(MessageForm message) {
        MyMessage msg = (MyMessage) message;

//        System.out.println("Na zastávke " + msg.zastavkaSpravy.nazov() + " je " + msg.zastavkaSpravy.pocetCestujucich());
       
        naplanujNastupy(msg);

    }

    private void processNastup_Autobus(MessageForm message) {

        MyMessage msg = (MyMessage) message;
        // vozidlo je uz v pooly vozidiel zastavky
        // skus najskor pre jedny dvere

        MyMessage cestujuciSpravy = msg.cestujuciSpravy;
        cestujuciSpravy.casNastupuDoVozidla = mySim().currentTime();
//        System.out.println("Nastupil Autobus " + mySim().currentTime());
//         System.out.println("NASTUPIL: "+cestujuciSpravy.casNastupuDoVozidla);
        Zastavka zastavkaSpravy = msg.zastavkaSpravy;
        Vozidlo vozidlo = msg.vozidloSpravy;
        int dvere = msg.dvereSpravy;
        vozidlo.uvolniDvere(dvere);

        // VARIANTA A , OKAMZITY ODCHOD, AK B DAJ HOLD 1.5 minuty
        if ((vozidlo.vsetkyDvereVolne() && zastavkaSpravy.pocetCestujucich() == 0)
                || (vozidlo.vsetkyDvereVolne() && vozidlo.plny())) {
            // VARIANTA B
            if (msg.config.variantaNastupov == Varianta_Nastupov.POCKAJ_90_SEKUND && !vozidlo.cakalUzVariantaB && !vozidlo.plny() &&!zastavkaSpravy.cakaUzNejakyVariantaB()) {
                msg.setCode(Mc.cakanie_varianta_B);
                vozidlo.setAktivitaVariantaBCakanie(Activity.CAKA_NASTUPY, mySim().currentTime(), mySim().currentTime() + cakanieNaDalsich_Varianta_B);
                vozidlo.cakalUzVariantaB = true;
                vozidlo.cakaVariantaB = true;
//                System.out.println("Vozidlo: " + vozidlo.getId() + " začína čakat(B) na zastavke" + zastavkaSpravy.nazov() + " : o " + mySim().currentTime());
                hold(cakanieNaDalsich_Varianta_B, msg);
                return;

            } else {
                vozidlo.cakalUzVariantaB = false;
                assistantFinished(message);
                return;
            }
            // Naplanujeme dalsi nastup pre dvere
        } else if (zastavkaSpravy.pocetCestujucich() > 0 && vozidlo.zmestiSaJeden()) {
            naplanujNastupy(msg);
            // Cakame este na nastupenie z ostatnych dveri
        } else if (!vozidlo.vsetkyDvereVolne() && !vozidlo.zmestiSaJeden()) {
            return;
        }

    }

    private void processCakanie_Varianta_B(MessageForm message) {
        // Dokoncil cakanie 1.5 minuty VARIANTA B
        MyMessage msg = (MyMessage) message;
        msg.vozidloSpravy.cakaVariantaB = false;
        msg.vozidloSpravy.cakalUzVariantaB = false;
//        System.out.println("Vozidlo: " + msg.vozidloSpravy.getId() + " končí čakanie čakat(B) na zastavke" + msg.zastavkaSpravy.nazov() + " : o " + mySim().currentTime());
        assistantFinished(message);
//        naplanujNastupy((MyMessage) message);
        //---

    }

    public void naplanujNastupy(MyMessage msg) {

        // vozidlo je uz v pooly vozidiel zastavky
        Zastavka zastavkaSpravy = msg.zastavkaSpravy;
        //vozidlo ktore prislo na zstavku teraz
        Vozidlo vozidloSpravy = msg.vozidloSpravy;
        int volneMiesta = vozidloSpravy.volneMiesta();
        //ak je plne alebo na zastavke nikto neni

        if (volneMiesta == 0 || zastavkaSpravy.pocetCestujucich() == 0) {
            vozidloSpravy.cakalUzVariantaB = false;
            vozidloSpravy.cakaVariantaB = false;
            assistantFinished(msg);
            return;
        }

        // Tu su vzdy su aspon 1 dvere volne
        List<Integer> volneDvere = vozidloSpravy.indexyVolnychDveri();
        int pocetVolnychDveri = volneDvere.size();
        if (volneMiesta > pocetVolnychDveri) {
            for (Integer i : volneDvere) {
                int dvere = i;
                // ostatny cestujuci urcite cakaju menej nez prvy, kedze je to Queue
                if (vozidloSpravy instanceof Mikrobus) {
                    // Ak je mikrobus 
                    // Prvy urcite cakal najdlhsie, kedze je to Queue
                    MyMessage prvyCestujuci = zastavkaSpravy.peekPrvyCestujuci();
                    if (prvyCestujuci != null) {
                        // Ak neckala viac ako 6 minut tak nenastupuje do mikrobusu
                        if (mySim().currentTime() - prvyCestujuci.casPrichoduNaZastavku < dlzkaCakania_Nastup_Mikrobus) {
//                            System.out.println("NENASTUPUJU MIRKOBUS " + mySim().currentTime());
                            assistantFinished(msg);
                            return;
                        }
                    }

                }
                MyMessage cestujuci = zastavkaSpravy.odoberCestujuceho();
                MyMessage copy = (MyMessage) msg.createCopy();

                if (cestujuci != null) {
                    // Na štatistiku, akym vozidlom isiel cestujuci
                    cestujuci.casZacatiaNastupu = mySim().currentTime();
                    cestujuci.vozidloSpravy = vozidloSpravy;

                    vozidloSpravy.pridajCestujuceho(cestujuci);

                    copy.cestujuciSpravy = cestujuci;
                    vozidloSpravy.obsadDvere(dvere);
                    copy.dvereSpravy = dvere;
                    double holdTime = nastupAutobusRNG.sample();
                    int code = Mc.nastup_autobus;
                    if (vozidloSpravy instanceof Mikrobus) {
//                        System.out.println("NASTUPUJE MIRKOBUS " + mySim().currentTime());
                        holdTime = nastupMikrobusRNG.sample();
                        code = Mc.nastup_mikrobus;
                    }
                    copy.setCode(code);

                    hold(holdTime, copy);
                }

            }
        } else {
            for (int i = 0; i < volneMiesta; i++) {
                int dvere = volneDvere.get(i);

                if (vozidloSpravy instanceof Mikrobus) {
                    // Ak je mikrobus 
                    // Prvy urcite cakal najdlhsie, kedze je to Queue
                    MyMessage prvyCestujuci = zastavkaSpravy.peekPrvyCestujuci();
                    if (prvyCestujuci != null) {
                        // Ak neckala viac ako 6 minut tak nenastupuje do mikrobusu
                        if (mySim().currentTime() - prvyCestujuci.casPrichoduNaZastavku < dlzkaCakania_Nastup_Mikrobus) {
//                            System.out.println("NENASTUPUJU MIRKOBUS " + mySim().currentTime());
                            assistantFinished(msg);
                            return;
                        }
                    }

                }
                MyMessage cestujuci = zastavkaSpravy.odoberCestujuceho();
                MyMessage copy = (MyMessage) msg.createCopy();

                if (cestujuci != null) {
                    // Na štatistiku, akym vozidlom isiel cestujuci
                    cestujuci.vozidloSpravy = vozidloSpravy;
                    vozidloSpravy.pridajCestujuceho(cestujuci);
                    cestujuci.casZacatiaNastupu = mySim().currentTime();
                    copy.cestujuciSpravy = cestujuci;
                    vozidloSpravy.obsadDvere(dvere);
                    copy.dvereSpravy = dvere;
                    double holdTime = nastupAutobusRNG.sample();
                    int code = Mc.nastup_autobus;
                    if (vozidloSpravy instanceof Mikrobus) {

                        holdTime = nastupMikrobusRNG.sample();
                        code = Mc.nastup_mikrobus;
                    }
                    copy.setCode(code);
                    hold(holdTime, copy);
                }

            }
        }

    }

    private void processNastup_Mikrobus(MessageForm message) {
        MyMessage msg = (MyMessage) message;
        MyMessage cestujuciSpravy = msg.cestujuciSpravy;
        cestujuciSpravy.casNastupuDoVozidla = mySim().currentTime();
//        System.out.println("nastupil MIRKOBUS " + mySim().currentTime());
        Vozidlo vozidlo = msg.vozidloSpravy;
        int dvere = msg.dvereSpravy;
        vozidlo.uvolniDvere(dvere);
        naplanujNastupy(msg);
    }

    //meta! userInfo="Process messages defined in code", id="0"
    public void processDefault(MessageForm message) {
        switch (message.code()) {

            case Mc.nastup_autobus:
                processNastup_Autobus(message);
                break;

            case Mc.nastup_mikrobus:
                processNastup_Mikrobus(message);
                break;
            case Mc.cakanie_varianta_B:
                processCakanie_Varianta_B(message);
                break;
        }
    }

    //meta! userInfo="Generated code: do not modify", tag="begin"
    @Override
    public void processMessage(MessageForm message) {
        switch (message.code()) {
            case Mc.start:
                processStart(message);
                break;

            default:
                processDefault(message);
                break;
        }
    }
    //meta! tag="end"

    @Override
    public Zastavky_Agent myAgent() {
        return (Zastavky_Agent) super.myAgent();
    }

}
