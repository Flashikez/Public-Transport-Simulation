package agents;

import OSPABA.*;
import continualAssistants.*;
import managers.*;
import simulation.*;

//meta! id="50"
public class Zastavky_Agent extends Agent {

    public Zastavky_Agent(int id, Simulation mySim, Agent parent) {
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
        new Zastavky_Manager(Id.zastavky_Manager, mySim(), this);
        new Nastup_Cestujucich(Id.nastup_Cestujucich, mySim(), this);
        new Nastup_v_cakani_varianta_B(Id.nastup_Cestujuceho_Varianta_B, _mySim, this);
        addOwnMessage(Mc.iNIT);
        addOwnMessage(Mc.vykonaj_Nastup);
        addOwnMessage(Mc.novy_Cestujuci);
        addOwnMessage(Mc.nastup_autobus);
        addOwnMessage(Mc.nastup_mikrobus);
        addOwnMessage(Mc.cakanie_varianta_B);
    }
    //meta! tag="end"
}
