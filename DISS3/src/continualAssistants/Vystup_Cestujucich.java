package continualAssistants;

import OSPABA.*;
import OSPRNG.TriangularRNG;
import agents.*;
import objects.Mikrobus;
import objects.Vozidlo;
import objects.enums.Activity;
import simulation.*;

//meta! id="76"
public class Vystup_Cestujucich extends OSPABA.Process {

    private final TriangularRNG vystupCestujucichRNG;

    private double vystupMikrobus;

    public Vystup_Cestujucich(int id, Simulation mySim, CommonAgent myAgent) {
        super(id, mySim, myAgent);
        vystupCestujucichRNG = new TriangularRNG(0.01, 0.07, 0.02, ((MySimulation) mySim()).seedGenerator);
        vystupMikrobus = 4 / 60d;
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication
    }

    //meta! sender="Zastavky_Agent", id="77", type="Start"
    public void processStart(MessageForm message) {
        // Zacni vystup cestujucich
        MyMessage msg = (MyMessage) message;
        Vozidlo vozidlo = msg.vozidloSpravy;
        vozidlo.setAktivita(Activity.VYSTUP);
        boolean budeVystup = false;
//        System.out.println("Prebieha výstup: "+vozidlo.pocetLudi()+" ľudí");
        for (int i = 0; i < vozidlo.pocetDveri(); i++) {
            MyMessage cestujuci = vozidlo.odoberCestujuceho();
            MyMessage copy = (MyMessage) msg.createCopy();
            copy.cestujuciSpravy = cestujuci;
            if (cestujuci != null) {
                copy.setCode(Mc.vystupil);
                copy.dvereSpravy = i;
                vozidlo.obsadDvere(i);
                double holdTime = vystupCestujucichRNG.sample();
                if (vozidlo instanceof Mikrobus) {
                    holdTime = vystupMikrobus;
                }
                budeVystup = true;
                hold(holdTime, copy);
            }

        }
        if (!budeVystup) {
            assistantFinished(message);
        }
    }

    private void processVystupil(MessageForm message) {
        MyMessage msg = (MyMessage) message;

        MyMessage copied = (MyMessage) msg.createCopy();
        msg.setAddressee(Id.doprava_Agent);
        notice(msg);
        Vozidlo vozidlo = copied.vozidloSpravy;
//       vozidlo.odoberCestujuceho();
//        System.out.println("Vystupil: "+mySim().currentTime());
        int indexDveri = copied.dvereSpravy;
        vozidlo.uvolniDvere(indexDveri);
        //Ak su vsetky dvere otvorene a pocetLudi je 0 konci
        if (vozidlo.vsetkyDvereVolne() && vozidlo.pocetLudi() == 0) {
            MyMessage msgC = (MyMessage) copied.createCopy();
            assistantFinished(msgC);
            // este vystupuju z nejakych dveri
        } else if (!vozidlo.vsetkyDvereVolne() && vozidlo.pocetLudi() == 0) {
            return;
        } else if (vozidlo.pocetLudi() > 0) {
            MyMessage cestujuci = vozidlo.odoberCestujuceho();
            copied.cestujuciSpravy = cestujuci;
            vozidlo.obsadDvere(indexDveri);
            double holdTime = vystupCestujucichRNG.sample();
            if (vozidlo instanceof Mikrobus) {
                holdTime = vystupMikrobus;
            }
            hold(holdTime, copied);

        }

    }

    //meta! userInfo="Process messages defined in code", id="0"
    public void processDefault(MessageForm message) {
        switch (message.code()) {
        }
    }

    //meta! userInfo="Generated code: do not modify", tag="begin"
    @Override
    public void processMessage(MessageForm message) {
        switch (message.code()) {
            case Mc.start:
                processStart(message);
                break;
            case Mc.vystupil:
                processVystupil(message);
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

}
