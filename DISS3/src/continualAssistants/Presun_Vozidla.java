package continualAssistants;

import OSPABA.*;
import agents.*;
import objects.Linka;
import objects.Stadion;
import objects.Zastavka;
import objects.enums.Activity;
import simulation.*;

//meta! id="72"
public class Presun_Vozidla extends OSPABA.Process {

    public Presun_Vozidla(int id, Simulation mySim, CommonAgent myAgent) {
        super(id, mySim, myAgent);
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication
    }

    //meta! sender="Vozidla_agent", id="73", type="Start"
    public void processStart(MessageForm message) {

        //Vozidlo sa prida/odobere k zastavke v agentovi zastavok
        MyMessage msg = (MyMessage) message;
        msg.setCode(Mc.presun_assist);
        Linka linka = msg.linkaSpravy;
        Zastavka current = msg.zastavkaSpravy;
        Zastavka dalsia = linka.dalsiaZastavka(current);
        double casKdalsej = (current instanceof Stadion) ? msg.config.dajCasNaStart(linka) : current.casKdalsej();
        msg.zastavkaSpravy = dalsia;
        // NA zobrazovanie progressu
        msg.vozidloSpravy.setDalsia(dalsia, mySim().currentTime() + casKdalsej,mySim().currentTime());
        msg.vozidloSpravy.setAktivita(Activity.CESTUJE);
        //--
        hold(casKdalsej, message);

    }

    //meta! userInfo="Process messages defined in code", id="0"
    public void processDefault(MessageForm message) {
        switch (message.code()) {
            case Mc.presun_assist:
                
                assistantFinished(message);
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
    public Vozidla_agent myAgent() {
        return (Vozidla_agent) super.myAgent();
    }

}
