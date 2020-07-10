package continualAssistants;

import OSPABA.*;
import OSPRNG.ExponentialRNG;
import agents.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import objects.Config;
import objects.Linka;
import objects.Stadion;
import objects.Zastavka;
import simulation.*;

//meta! id="7"
public class Planovac_Prichodov extends Scheduler {

    private Map<Linka, HashMap<Zastavka, ExponentialRNG>> generatory;
    double casZapasu;

    public Planovac_Prichodov(int id, Simulation mySim, CommonAgent myAgent) {
        super(id, mySim, myAgent);
        generatory = new HashMap<>();

        // GENERATORS CREATE
        double prichodyStartOffset = Config.startPrichoduOffset;
        double prichodyEndOffset = Config.koniecPrichoduOffset;
        casZapasu = ((MySimulation) mySim()).config.casStartZapasu();
        List<Linka> linky = Arrays.asList(Config.Linka_A.linkaA, Config.Linka_B.linkaB, Config.Linka_C.linkaC);

        for (Linka linka : linky) {
            HashMap<Zastavka, ExponentialRNG> linkRNG = new HashMap<>();
            for (Zastavka zast : linka.listZastavky()) {
//                if (!zast.generovanieSpustene()) {
                if (!(zast instanceof Stadion)) {
//                        if (zast.nazov().compareTo("K1") != 0 && zast.nazov().compareTo("K2") != 0 && zast.nazov().compareTo("K3") != 0) {
                    double casDoStadiona = zast.dobaKuStadionu();
                    double casStart = casZapasu - (prichodyStartOffset + casDoStadiona);
                    double casEnd = casZapasu - (prichodyEndOffset + casDoStadiona);
                    int pocetCestujucich = zast.pocetOcakavanych();
                    // Krizovatka sa rozdeli na polovicu (Polovica ludi pride z jednej linky, druha z druhej)
                    if (zast.krizuje()) {
                        pocetCestujucich /= 2;
                    }
                    double rozsah = casEnd - casStart;
//                        System.out.println(rozsah);
                    ExponentialRNG rng = new ExponentialRNG((rozsah / pocetCestujucich), ((MySimulation) mySim()).seedGenerator);
//                            DeterministicRNG rng = new DeterministicRNG(15.0/60.0);
                    linkRNG.put(zast, rng);
//                    zast.spustilGenerovanie();

                }
                generatory.put(linka, linkRNG);
//                }
//                }
            }

        }
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication
    }

    //meta! sender="Okolie_Agent", id="8", type="Start"
    public void processStart(MessageForm message) {

        message.setCode(Mc.novy_Cestujuci);

        for (Linka linka : generatory.keySet()) {
            for (Zastavka zastavka : generatory.get(linka).keySet()) {
                MyMessage spravaStart = (MyMessage) message.createCopy();
                spravaStart.linkaSpravy = linka;
                spravaStart.zastavkaSpravy = zastavka;
                hold(casZapasu - (Config.startPrichoduOffset + zastavka.dobaKuStadionu()) + generatory.get(linka).get(zastavka).sample(), spravaStart);

            }

        }

    }

    //meta! userInfo="Process messages defined in code", id="0"
    public void processDefault(MessageForm message) {
        switch (message.code()) {

            case Mc.novy_Cestujuci:

                MyMessage mess = (MyMessage) message;
                MyMessage copied = (MyMessage) mess.createCopy();
//                System.out.println("Novy cestujúci " + mySim().currentTime() + "  " + mess.zastavkaSpravy.nazov());
                notice(copied);

                if (mySim().currentTime() < casZapasu - (mess.zastavkaSpravy.dobaKuStadionu() + Config.koniecPrichoduOffset) && !mess.zastavkaSpravy.vygenerovanychDost()) {
                    HashMap<Zastavka, ExponentialRNG> gens = generatory.get(mess.linkaSpravy);
                    hold(gens.get(mess.zastavkaSpravy).sample(), message);
                    break;
                } else {
//                    System.out.println("BRAKING "+mess.zastavkaSpravy.nazov());
                }

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
    public Okolie_Agent myAgent() {
        return (Okolie_Agent) super.myAgent();
    }

}
