package managers;

import OSPABA.*;
import OSPStat.Stat;
import agents.*;
import java.util.HashMap;
import java.util.Map;
import objects.Config;
import objects.Linka;
import objects.Mikrobus;
import simulation.*;

//meta! id="3"
public class Okolie_Manager extends Manager {

    public int pocetCestujucich;
    //--Celkovo
    public int pocetNaStadione;
    public int pocetCoPrisliNeskoro;
    // Celková
    public Stat dobaCakaniaNaVozidlo;
    //---
    public Stat casPrichoduNaStadion;
    public int pocetMikrobusom;
    public int pocetAutobusom;
    // Staty pre jednotlive Linky
    public Map<Linka, Stat> dobaCakaniaPreLinky;
    public Map<Linka, Stat> poctyNaStadioneLinky;
    public Map<Linka, Stat> poctyNeskoroLinky;
    


    public Okolie_Manager(int id, Simulation mySim, Agent myAgent) {
        super(id, mySim, myAgent);
        dobaCakaniaNaVozidlo = new Stat();
        casPrichoduNaStadion = new Stat();

        dobaCakaniaPreLinky = new HashMap<>();
        poctyNaStadioneLinky = new HashMap<>();
        poctyNeskoroLinky = new HashMap<>();
        Linka A = Config.Linka_A.linkaA;
        dobaCakaniaPreLinky.put(A, new Stat());
        poctyNaStadioneLinky.put(A, new Stat());
        poctyNeskoroLinky.put(A, new Stat());
        Linka B = Config.Linka_B.linkaB;
        dobaCakaniaPreLinky.put(B, new Stat());
        poctyNaStadioneLinky.put(B, new Stat());
        poctyNeskoroLinky.put(B, new Stat());
        Linka C = Config.Linka_C.linkaC;
        dobaCakaniaPreLinky.put(C, new Stat());
        poctyNaStadioneLinky.put(C, new Stat());
        poctyNeskoroLinky.put(C, new Stat());

        init();

    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication

        if (petriNet() != null) {
            petriNet().clear();
        }

//        dobaCakaniaPreLinky = new HashMap<>();
//        poctyNaStadioneLinky = new HashMap<>();
//        poctyNeskoroLinky = new HashMap<>();
        for (Stat value : dobaCakaniaPreLinky.values()) {
            value.clear();

        }
        for (Stat value : poctyNaStadioneLinky.values()) {
            value.clear();

        }
        for (Stat value : poctyNeskoroLinky.values()) {
            value.clear();

        }

        pocetCestujucich = 0;
        pocetCoPrisliNeskoro = 0;
        pocetNaStadione = 0;
        dobaCakaniaNaVozidlo.clear();
        casPrichoduNaStadion.clear();
        pocetMikrobusom = 0;
    }

    //meta! sender="Model_Agent", id="63", type="Notice"
    public void processINIT(MessageForm message) {
        message.setAddressee(Id.planovac_Prichodov);
        startContinualAssistant(message);

    }

    //meta! sender="Planovac_Prichodov", id="9", type="Notice"
    public void processNovy_Cestujuci(MessageForm message) {
        pocetCestujucich++;
        message.setCode(Mc.novy_Cestujuci);
        message.setAddressee(Id.model_Agent);
        notice(message);

    }

    //meta! sender="Model_Agent", id="18", type="Notice"
    public void processCestujuci_Na_Stadione(MessageForm message) {
        MyMessage msg = (MyMessage) message;
        msg.casPrichoduNaStadion = mySim().currentTime();
        if (msg.casPrichoduNaStadion > ((MySimulation) mySim()).config.casStartZapasu()) {
            pocetCoPrisliNeskoro++;
            poctyNeskoroLinky.get(msg.linkaSpravy).addSample(1);

        }
        if (msg.vozidloSpravy instanceof Mikrobus) {
            pocetMikrobusom++;
        } else {
            pocetAutobusom++;
        }
//         System.out.println("NASTUPIL : "+msg.casNastupuDoVozidla+"Prisiel: "+msg.casPrichoduNaZastavku+"  -> cakal:"+(msg.casNastupuDoVozidla - msg.casPrichoduNaZastavku));
        dobaCakaniaNaVozidlo.addSample(msg.casZacatiaNastupu - msg.casPrichoduNaZastavku);
        dobaCakaniaPreLinky.get(msg.linkaSpravy).addSample(msg.casZacatiaNastupu - msg.casPrichoduNaZastavku);
//        if (msg.linkaSpravy == Config.Linka_B.linkaB) {
//            System.out.println(msg.zastavkaSpravy.nazov());
//        }
        casPrichoduNaStadion.addSample(msg.casPrichoduNaStadion);
        
        poctyNaStadioneLinky.get(msg.linkaSpravy).addSample(1);

        pocetNaStadione++;
    }

    //meta! sender="Planovac_Prichodov", id="8", type="Finish"
    public void processFinish(MessageForm message) {

    }

    //meta! userInfo="Process messages defined in code", id="0"
    public void processDefault(MessageForm message) {
        switch (message.code()) {
        }
    }

    //meta! userInfo="Generated code: do not modify", tag="begin"
    public void init() {
    }

    @Override
    public void processMessage(MessageForm message) {
        switch (message.code()) {
            case Mc.novy_Cestujuci:
                processNovy_Cestujuci(message);
                break;

            case Mc.iNIT:
                processINIT(message);
                break;

            case Mc.finish:
                processFinish(message);
                break;

            case Mc.cestujuci_Na_Stadione:
                processCestujuci_Na_Stadione(message);
                break;

            default:
                processDefault(message);
                break;
        }
    }
    //meta! tag="end"

    @Override
    public Okolie_Agent myAgent() {
        return (Okolie_Agent) super.myAgent();
    }

}
