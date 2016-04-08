package behaviours;

import java.io.IOException;
import java.io.StringWriter;

import com.fasterxml.jackson.databind.ObjectMapper;

import agents.EnvAgent;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

/** Send confirm message to simulation agent with sudoku */
public class EnvAgentSendSudoku extends OneShotBehaviour {

	public void action() {
		ACLMessage msg = new ACLMessage(ACLMessage.CONFIRM);
		msg.addReceiver(new AID("SimulAgent", AID.ISLOCALNAME));
		ObjectMapper mapper = new ObjectMapper();
		StringWriter sw = new StringWriter();
		try {
			mapper.writeValue(sw, ((EnvAgent) this.getAgent()).getSudoku());
		} catch (IOException e) {
			e.printStackTrace();
		}
		String s = sw.toString();
		msg.setContent(s);
		this.getAgent().send(msg);
	}

}
