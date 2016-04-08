package behaviours;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/** Once receive a inform message send it to Environment Agent */
public class SimulAgentReceiveCalc extends CyclicBehaviour {

	public void action() {
		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
		ACLMessage message = this.getAgent().receive(mt);
		if(message != null) {
			ACLMessage m = new ACLMessage(ACLMessage.REQUEST);
			m.setConversationId(message.getConversationId());
			m.setContent(message.getContent());
			m.addReceiver(new AID("EnvAgent", AID.ISLOCALNAME));
			this.getAgent().send(m);
		} else {
			block();
		}
	}
}
