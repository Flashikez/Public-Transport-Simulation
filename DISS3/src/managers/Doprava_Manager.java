package managers;

import OSPABA.*;
import agents.*;
import objects.Utilities;
import objects.Config;
import objects.Linka;
import objects.Stadion;
import objects.Vozidlo;
import objects.Zastavka;
import objects.enums.Activity;
import simulation.*;

//meta! id="22"
public class Doprava_Manager extends Manager {

    // Linky kvoli GUI updatu
    public Linka linkaA, linkaB, linkaC;
    private int pocetUkoncenychVozidiel;
    private int pocetVozidiel;
    // STAT
    public double casUkonceniaDopravy;

    public int countPocetLudiNaZastavkach() {
        return Utilities.StateCalc.pocetNaZastavkach(linkaA, linkaB, linkaC);
    }

    public int countPocetLudiVoVozidlach() {
        return Utilities.StateCalc.pocetVoVozidlach(linkaA, linkaB, linkaC);
    }

    public Doprava_Manager(int id, Simulation mySim, Agent myAgent) {
        super(id, mySim, myAgent);
        init();
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication
        pocetVozidiel = 0;
        pocetUkoncenychVozidiel = 0;
        casUkonceniaDopravy = 0;
        if (petriNet() != null) {
            petriNet().clear();
        }
    }

    //meta! sender="Model_Agent", id="62", type="Notice"
    public void processINITModel_Agent(MessageForm message) {
        MyMessage msg = (MyMessage) message;
        // referencie kvoli GUI
        linkaA = Config.Linka_A.linkaA;
        linkaB = Config.Linka_B.linkaB;
        linkaC = Config.Linka_C.linkaC;
        //

        /*
        INIT REQUESTY , RESPONSE PRIDE KED JE VOZIDLO NA STARTOVACEJ ZASTAVKE
         */
        MyMessage copied = (MyMessage) message.createCopy();
        copied.setAddressee(Id.zastavky_Agent);
        notice(copied);
        // request pre kazde vozidlo
        Config cfg = msg.config;

        //Linka A
        for (Vozidlo vozidlo : cfg.vozidlaLinkaA) {
            MyMessage copy = (MyMessage) msg.createCopy();
            copy.linkaSpravy = linkaA;

            copy.zastavkaSpravy = vozidlo.startZastavka;
            copy.vozidloSpravy = vozidlo;
            copy.setAddressee(Id.vozidla_agent);
            pocetVozidiel++;
//            copy.setCode(Mc.iNIT);
            request(copy);
        }
        // Linka B
        for (Vozidlo vozidlo : cfg.vozidlaLinkaB) {
            MyMessage copy = (MyMessage) msg.createCopy();
            copy.linkaSpravy = linkaB;

            copy.zastavkaSpravy = vozidlo.startZastavka;
            copy.vozidloSpravy = vozidlo;
            copy.setAddressee(Id.vozidla_agent);
            pocetVozidiel++;
//            copy.setCode(Mc.iNIT);
            request(copy);
        }
        //Linka C
        for (Vozidlo vozidlo : cfg.vozidlaLinkaC) {
            MyMessage copy = (MyMessage) msg.createCopy();
            copy.linkaSpravy = linkaC;
            copy.zastavkaSpravy = vozidlo.startZastavka;
            
            copy.vozidloSpravy = vozidlo;
            copy.setAddressee(Id.vozidla_agent);
            pocetVozidiel++;
//            copy.setCode(Mc.iNIT);
            request(copy);
        }

    }

    //meta! sender="Vozidla_agent", id="70", type="Response"
    public void processINITVozidla_agent(MessageForm message) {
        // Vozidlo je na starte
        MyMessage msg = (MyMessage) message;
//        System.out.println(((MySimulation) mySim()).currentTime() + "\t" + msg.vozidloSpravy + " je v bode zaciatku: " + msg.zastavkaSpravy);
        // Sprav nástup

        //ak je startovacia zastavka Stadion, nerob nástup ani vystup
        if (msg.zastavkaSpravy instanceof Stadion) {
            message.setCode(Mc.presun_Vozidlo);
            message.setAddressee(Id.vozidla_agent);
            request(message);
        } else {
            // Sprav nástup 
            message.setAddressee(Id.zastavky_Agent);
            message.setCode(Mc.vykonaj_Nastup);
            request(message);

//            presunVozidlo(message);
        }

    }

    //meta! sender="Vozidla_agent", id="58", type="Response"
    public void processPresun_Vozidlo(MessageForm message) {
        // Vozidlo bolo presunute na dalsiu zastavku
        MyMessage msg = (MyMessage) message;
//        System.out.println(((MySimulation) mySim()).currentTime() + "\t" + msg.vozidloSpravy + " je na zastávke: " + msg.zastavkaSpravy + " volne miesta: "+msg.vozidloSpravy.volneMiesta());
        if (msg.zastavkaSpravy instanceof Stadion) {
            //Ak vozidlo prislo na štadión prázdne a po začatí zápasu.. ukonmči dopravu tomuto vozidlo
            if (msg.config.casStartZapasu() < mySim().currentTime() && msg.vozidloSpravy.pocetLudi() == 0) {
                pocetUkoncenychVozidiel++;
                msg.vozidloSpravy.setAktivita(Activity.UKONCIL);
                if (pocetUkoncenychVozidiel == pocetVozidiel) {
                    casUkonceniaDopravy = mySim().currentTime();
                    mySim().stopReplication();
                }

                return;
            }
            // sprav výstup
            message.setAddressee(Id.vystup_Cestujucich);
            startContinualAssistant(message);
        } else {
            // sprav nástup
            message.setAddressee(Id.zastavky_Agent);
            message.setCode(Mc.vykonaj_Nastup);
            request(message);
        }

    }

    //meta! sender="Zastavky_Agent", id="67", type="Response"
    public void processVykonaj_Nastup(MessageForm message) {
        MyMessage msg = (MyMessage) message;
        // Prebehol nástup
        message.setAddressee(Id.vozidla_agent);
        message.setCode(Mc.presun_Vozidlo);
        request(message);
    }

    //meta! sender="Model_Agent", id="61", type="Request"
    public void processDoprava_Cestujuceho(MessageForm message) {
        // Prisiel  novy cestujuci
        message.setCode(Mc.novy_Cestujuci);
        message.setAddressee(Id.zastavky_Agent);
        notice(message);
    }

    //meta! userInfo="Process messages defined in code", id="0"
    public void processDefault(MessageForm message) {
        switch (message.code()) {
        }
    }

    //Vykonaný výstup cestujúcich
    private void processFinish(MessageForm message) {
        presunVozidlo(message);
    }

    // Cestujúci je na štadióne
    private void processVystupil(MessageForm message) {
        MyMessage msg = (MyMessage) message;
        msg.cestujuciSpravy.setCode(Mc.doprava_Cestujuceho);
        response(msg.cestujuciSpravy);
    }

    //meta! userInfo="Generated code: do not modify", tag="begin"
    public void init() {
    }

    @Override
    public void processMessage(MessageForm message) {
        switch (message.code()) {
            case Mc.iNIT:
                switch (message.sender().id()) {
                    case Id.model_Agent:
                        processINITModel_Agent(message);
                        break;

                    case Id.vozidla_agent:
                        processINITVozidla_agent(message);
                        break;
                }
                break;

            case Mc.vykonaj_Nastup:
                processVykonaj_Nastup(message);
                break;
            case Mc.vystupil:
                processVystupil(message);
                break;
            case Mc.finish:
                processFinish(message);
                break;

            case Mc.doprava_Cestujuceho:
                processDoprava_Cestujuceho(message);
                break;

            case Mc.presun_Vozidlo:
                processPresun_Vozidlo(message);
                break;

            default:
                processDefault(message);
                break;
        }
    }
    //meta! tag="end"

    @Override
    public Doprava_Agent myAgent() {
        return (Doprava_Agent) super.myAgent();
    }

    public void presunVozidlo(MessageForm message) {
        MyMessage msg = (MyMessage) message;
        Zastavka zastavka = msg.zastavkaSpravy;
        msg.vozidloSpravy.setAktivita(Activity.CESTUJE);

//        System.out.println(((MySimulation) mySim()).getTimeString() + "\t" + msg.vozidloSpravy + " je v: " + msg.zastavkaSpravy);
        //Kontrola ci este maju jazdit
        message.setAddressee(Id.vozidla_agent);
        message.setCode(Mc.presun_Vozidlo);
        request(message);
    }

}
