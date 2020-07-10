package continualAssistants;

import OSPABA.*;
import agents.*;
import objects.enums.Activity;
import simulation.*;

//meta! id="72"
public class Presun_Vozidla_Na_Start extends OSPABA.Process{
    
    public Presun_Vozidla_Na_Start(int id, Simulation mySim, CommonAgent myAgent) {
        super(id, mySim, myAgent);
    }
    
    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication
    }

    //meta! sender="Vozidla_agent", id="73", type="Start"
    public void processStart(MessageForm message) {
        //zastavkaSpravyUzJeNastavena na startovaciu
        MyMessage msg = (MyMessage) message;
        
        msg.setCode(Mc.presun_start_assist);
        double holdTime = msg.vozidloSpravy.startCas;
        msg.vozidloSpravy.setAktivita(Activity.CAKA);
        msg.vozidloSpravy.setDalsia(msg.zastavkaSpravy, mySim().currentTime() + holdTime,mySim().currentTime());
        
        hold(holdTime, msg);
        
    }

    //meta! userInfo="Process messages defined in code", id="0"
    public void processDefault(MessageForm message) {
        switch (message.code()) {
            case Mc.presun_start_assist:
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
