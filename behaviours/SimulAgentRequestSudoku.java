package behaviours;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class SimulAgentRequestSudoku extends OneShotBehaviour{

	@Override
	public void action() {
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(new AID("EnvAgent", AID.ISLOCALNAME));
		this.getAgent().send(msg);
		
	}

}
