package behaviours;

import java.util.ArrayList;
import java.util.List;

import agents.SimulAgent;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/** Listen the subscribe message */
public class SimulAgentListen extends Behaviour {

	private List<AID> subscribeAgents;
	private boolean isAllSubscribed ;
	
	public SimulAgentListen() {
		this.subscribeAgents = new ArrayList<AID>();
		isAllSubscribed = false;
	}

	public void action() {
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.SUBSCRIBE);
			ACLMessage message = this.getAgent().receive(mt);
			if(message != null) {
				subscribeAgents.add(message.getSender());
			} else {
				block();
			}
			
			if(subscribeAgents.size() >= 27 && ((SimulAgent) this.getAgent()).getSudoku() != null) {
				this.getAgent().addBehaviour(
						new SimulAgentTick(
								(SimulAgent) this.getAgent(), 
								((SimulAgent) this.getAgent()).getSudoku(), 
								subscribeAgents));
				// if so, stop this behavior
				isAllSubscribed = true;
			}
	}

	public boolean done() {
		return isAllSubscribed;
	}
	
}
