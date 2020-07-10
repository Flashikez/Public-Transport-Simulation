package managers;

import OSPABA.*;
import agents.*;
import simulation.*;

//meta! id="52"
public class Vozidla_Manager extends Manager {

    public Vozidla_Manager(int id, Simulation mySim, Agent myAgent) {
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

    //meta! sender="Doprava_Agent", id="70", type="Request"
    public void processINIT(MessageForm message) {
        message.setAddressee(Id.presun_Vozidla_Na_Start);
        startContinualAssistant(message);

    }

    //meta! sender="Doprava_Agent", id="58", type="Request"
    public void processPresun_Vozidlo(MessageForm message) {
        message.setAddressee(Id.presun_Vozidla);
        startContinualAssistant(message);

    }

    //meta! sender="Presun_Vozidla", id="73", type="Finish"
    public void processFinish(MessageForm message) {
        switch (message.sender().id()) {
            case Id.presun_Vozidla_Na_Start:
                message.setCode(Mc.iNIT);
                response(message);
                break;

            case Id.presun_Vozidla:
                message.setCode(Mc.presun_Vozidlo);
//                System.out.println("CURRENT TIME: " + mySim().currentTime());
                response(message);
                break;

        }
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
            case Mc.iNIT:
                processINIT(message);
                break;

            case Mc.finish:
                processFinish(message);
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
    public Vozidla_agent myAgent() {
        return (Vozidla_agent) super.myAgent();
    }

}
