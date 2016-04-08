package behaviours;

import java.io.IOException;
import java.io.StringWriter;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import model.SudokuData;

/** Reply to simulation agent with 9 cells and their lists of possible values*/
public class AnalyseAgentResponse extends OneShotBehaviour {
	
	private SudokuData[] sd;
	ACLMessage msgReceived;
	
	public AnalyseAgentResponse(SudokuData[] sd, ACLMessage msgReceived) {
		this.sd = sd;
		this.msgReceived = msgReceived;
	}
	
	public void action() {
		ACLMessage reply = msgReceived.createReply();
		ObjectMapper mapper = new ObjectMapper();
		StringWriter sw = new StringWriter();
		try {
			mapper.writeValue(sw, sd);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String s = sw.toString();
		reply.setContent(s);
		reply.setPerformative(ACLMessage.INFORM);
		this.getAgent().send(reply);
	}
}