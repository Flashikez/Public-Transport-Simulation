package agents;

import OSPABA.*;
import continualAssistants.*;
import managers.*;
import simulation.*;


//meta! id="22"
public class Doprava_Agent extends Agent
{
	public Doprava_Agent(int id, Simulation mySim, Agent parent)
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
		new Doprava_Manager(Id.doprava_Manager, mySim(), this);
                new Vystup_Cestujucich(Id.vystup_Cestujucich, mySim(), this);
		addOwnMessage(Mc.iNIT);
		addOwnMessage(Mc.presun_Vozidlo);
		addOwnMessage(Mc.vykonaj_Nastup);
		addOwnMessage(Mc.doprava_Cestujuceho);
                addOwnMessage(Mc.vystupil);
	}
	//meta! tag="end"
}
