package behaviours;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import agents.SimulAgent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import model.Sudoku;

/** Once receive a confirm message about sudoku data, stock it in simulation agent */
public class SimulAgentReceiveSudoku extends CyclicBehaviour {

	public void action() {
		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CONFIRM);
		ACLMessage message = this.getAgent().receive(mt);
		if(message != null) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				Sudoku sudoku = mapper.readValue(message.getContent(), Sudoku.class);
				((SimulAgent) this.getAgent()).setSudoku(sudoku);
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			block();
		}
	}
}
