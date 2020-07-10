package agents;

import OSPABA.*;
import continualAssistants.*;
import managers.*;
import simulation.*;


//meta! id="52"
public class Vozidla_agent extends Agent
{
	public Vozidla_agent(int id, Simulation mySim, Agent parent)
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
		new Vozidla_Manager(Id.vozidla_agentManager, mySim(), this);
		new Presun_Vozidla(Id.presun_Vozidla, mySim(), this);
                new Presun_Vozidla_Na_Start(Id.presun_Vozidla_Na_Start,mySim(),this);
		addOwnMessage(Mc.iNIT);
                addOwnMessage(Mc.presun_start_assist);
                addOwnMessage(Mc.presun_assist);
		addOwnMessage(Mc.presun_Vozidlo);
	}
	//meta! tag="end"
}
