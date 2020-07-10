package agents;

import OSPABA.*;
import managers.*;
import objects.Config;
import objects.Linka;
import simulation.*;

//meta! id="1"
public class Model_Agent extends Agent {

    public Model_Agent(int id, Simulation mySim, Agent parent) {
        super(id, mySim, parent);
        init();
    }

    @Override
    public void prepareReplication() {
        super.prepareReplication();
        // Setup component for the next replication
    }

    //meta! userInfo="Generated code: do not modify", tag="begin"
    private void init() {
        new Model_Manager(Id.model_Manager, mySim(), this);
        addOwnMessage(Mc.novy_Cestujuci);
        addOwnMessage(Mc.doprava_Cestujuceho);

    }
    //meta! tag="end"

    public void initSimulation(Config cfg) {
        MyMessage startMsg = new MyMessage(_mySim, cfg);
        priradVozidlaLinkam(cfg);
        startMsg.setCode(Mc.iNIT);
        startMsg.setAddressee(Id.okolie_Agent);
        manager().notice(startMsg);
        MyMessage start2Msg = (MyMessage) startMsg.createCopy();
        start2Msg.setAddressee(Id.doprava_Agent);
        manager().notice(start2Msg);
    }

    private void priradVozidlaLinkam(Config cfg) {
        Linka linkaA = Config.Linka_A.linkaA;
        linkaA.priradVozidla(cfg.vozidlaLinkaA);
        Linka linkaB = Config.Linka_B.linkaB;
        linkaB.priradVozidla(cfg.vozidlaLinkaB);
        Linka linkaC = Config.Linka_C.linkaC;
        linkaC.priradVozidla(cfg.vozidlaLinkaC);

    }
}
