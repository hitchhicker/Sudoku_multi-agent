package behaviours;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

/** Send subscribe message to simulation agent */
public class AnalyseAgentSubscribe extends OneShotBehaviour{
	public void action() {
		ACLMessage message = new ACLMessage(ACLMessage.SUBSCRIBE);
		message.addReceiver(new AID("SimulAgent", AID.ISLOCALNAME));
		this.getAgent().send(message);
	}
}
