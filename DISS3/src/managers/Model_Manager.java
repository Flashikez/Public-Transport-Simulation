package managers;

import OSPABA.*;
import agents.*;
import simulation.*;

//meta! id="1"
public class Model_Manager extends Manager {

    public Model_Manager(int id, Simulation mySim, Agent myAgent) {
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

    //meta! sender="Okolie_Agent", id="5", type="Notice"
    public void processNovy_Cestujuci(MessageForm message) {
        message.setCode(Mc.doprava_Cestujuceho);
        message.setAddressee(Id.doprava_Agent);
        request(message);
    }

    //meta! sender="Doprava_Agent", id="61", type="Response"
    public void processDoprava_Cestujuceho(MessageForm message) {
        message.setCode(Mc.cestujuci_Na_Stadione);
        message.setAddressee(Id.okolie_Agent);
        notice(message);
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

            case Mc.doprava_Cestujuceho:
                processDoprava_Cestujuceho(message);
                break;

            default:
                processDefault(message);
                break;
        }
    }
    //meta! tag="end"

    @Override
    public Model_Agent myAgent() {
        return (Model_Agent) super.myAgent();
    }

}
