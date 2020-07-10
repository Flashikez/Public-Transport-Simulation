package agents;

import OSPABA.*;
import continualAssistants.*;
import managers.*;
import simulation.*;


//meta! id="3"
public class Okolie_Agent extends Agent
{
	public Okolie_Agent(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new Okolie_Manager(Id.okolie_Manager, mySim(), this);
		new Planovac_Prichodov(Id.planovac_Prichodov, mySim(), this);
		addOwnMessage(Mc.iNIT);
		addOwnMessage(Mc.novy_Cestujuci);
		addOwnMessage(Mc.cestujuci_Na_Stadione);
	}
	//meta! tag="end"
}
