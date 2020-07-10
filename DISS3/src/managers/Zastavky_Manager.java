package managers;

import OSPABA.*;
import agents.*;
import java.util.List;
import objects.Autobus;
import objects.Config;
import objects.Linka;
import objects.Vozidlo;
import objects.Zastavka;
import objects.enums.Activity;
import objects.enums.Varianta_Nastupov;
import simulation.*;

//meta! id="50"
public class Zastavky_Manager extends Manager {

    // Na účely GUI
    Linka linkaA, linkaB, linkaC;

    public List<Zastavka> zastavkyA() {
        return linkaA.listZastavky();
    }

    public List<Zastavka> zastavkyB() {
        return linkaB.listZastavky();
    }

    public List<Zastavka> zastavkyC() {
        return linkaC.listZastavky();
    }

    //
    public Zastavky_Manager(int id, Simulation mySim, Agent myAgent) {
        super(id, mySim, myAgent);
        init();
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication

        if (petriNet() != null) {
            petriNet().clear();
        }
    }

    //meta! sender="Doprava_Agent", id="68", type="Notice"
    public void processINIT(MessageForm message) {
        MyMessage msg = (MyMessage) message;
        linkaA = Config.Linka_A.linkaA;
        linkaB = Config.Linka_B.linkaB;
        linkaC = Config.Linka_C.linkaC;
    }

    //meta! sender="Doprava_Agent", id="67", type="Request"
    public void processVykonaj_Nastup(MessageForm message) {
        MyMessage msg = (MyMessage) message;
        Zastavka zast = msg.zastavkaSpravy;
        zast.pridajVozidlo(msg.vozidloSpravy);
        msg.vozidloSpravy.setAktivita(Activity.NASTUP);

        message.setAddressee(Id.nastup_Cestujucich);
        startContinualAssistant(message);

    }

    //meta! sender="Doprava_Agent", id="65", type="Notice"
    public void processNovy_Cestujuci(MessageForm message) {
        MyMessage msg = (MyMessage) message;
        msg.casPrichoduNaZastavku = mySim().currentTime();
        msg.zastavkaSpravy.pridajCestujuceho(msg);
        // Varianta B
        if (msg.config.variantaNastupov == Varianta_Nastupov.POCKAJ_90_SEKUND) {
            List<Vozidlo> vozidlaNaZastavke = msg.zastavkaSpravy.dajVozidlaNaZastavke();
            for (Vozidlo vozidlo : vozidlaNaZastavke) {
                // ak je to autobus a caka na dalsich 1.5 minuty
                if (vozidlo instanceof Autobus && vozidlo.cakaVariantaB && vozidlo.zmestiSaJeden() && vozidlo.volneDvere()) {
                    List<Integer> dvere = vozidlo.indexyVolnychDveri();
                    msg.dvereSpravy = dvere.get(0);
                    msg.vozidloSpravy = vozidlo;
                    msg.cestujuciSpravy = msg.zastavkaSpravy.odoberCestujuceho();
                    msg.cestujuciSpravy.nastupilVcakaniVariantaB = true;
                    msg.setAddressee(Id.nastup_Cestujuceho_Varianta_B);
                    startContinualAssistant(message);

                    return;
                }

            }

        }

    }

    //meta! sender="Nastup_Cestujucich", id="75", type="Finish"
    public void processFinishNastup_Cestujucich(MessageForm message) {
        // Nástup do vozidla vykonany
        MyMessage msg = (MyMessage) message;
        Zastavka zast = msg.zastavkaSpravy;
        zast.odchodVozidla(msg.vozidloSpravy);
        msg.setCode(Mc.vykonaj_Nastup);
        response(message);
    }

    //meta! userInfo="Process messages defined in code", id="0"
    public void processDefault(MessageForm message) {
        switch (message.code()) {
            case Mc.finish:
                if(message.addressee().id() == Id.nastup_Cestujuceho_Varianta_B){
                    
                    break;
                }
        }
    }

    //meta! userInfo="Generated code: do not modify", tag="begin"
    public void init() {
    }

    @Override
    public void processMessage(MessageForm message) {
        switch (message.code()) {
            case Mc.finish:
                switch (message.sender().id()) {
                    case Id.nastup_Cestujucich:
                        processFinishNastup_Cestujucich(message);
                        break;
                }
                break;

            case Mc.novy_Cestujuci:
                processNovy_Cestujuci(message);
                break;

            case Mc.vykonaj_Nastup:
                processVykonaj_Nastup(message);
                break;

            case Mc.iNIT:
                processINIT(message);
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
