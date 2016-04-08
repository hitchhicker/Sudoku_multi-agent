package behaviours;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import model.SudokuData;

/** Calculate lists of possible values for 9 cells each time */
public class AnalyseAgentResolve extends CyclicBehaviour {
	
	public void action() {
		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
		ACLMessage message = this.getAgent().receive(mt);
		if(message != null) {
			SudokuData[] sd = null;
			
			try {
				// Receive SudokuData
				ObjectMapper mapper = new ObjectMapper();
				sd = mapper.readValue(message.getContent(), SudokuData[].class);

				int [] unwanted  = getUnwantedNumbers(sd);
				int [] wanted = getPossibilitiesNumbers(unwanted);
				generateNewPossibilities(sd, wanted);
//				 for debug
//				System.out.println("INPUT: ");
//				for(int i = 0; i < sd.length; i++){
//					System.out.println(sd[i].getPossibleValues());
//				}
				 this.getAgent().addBehaviour(new AnalyseAgentResponse(sd, message));
			}catch(Exception ex) {
				ex.printStackTrace();
			}
		}else{
			block();
		}
	}
	
	public int[] getUnwantedNumbers(SudokuData[] sd) {
		int unwanted[] = new int[10];
		for (int i = 0; i < sd.length; i++) {
			unwanted[sd[i].getVal()] = sd[i].getVal();
		}
		return unwanted;
	}
	
	public int[] getPossibilitiesNumbers(int[] unwanted) {
		int [] possibilities = new int[10];
		int count = 0;
		for (int i = 1; i <= 9; i++) {
			if(unwanted[i] == 0) {
				possibilities[count++] = i;
			}
		}
		return possibilities;
	}

	public void generateNewPossibilities(SudokuData[] sd, int [] possibilities) {
		ArrayList<Integer> possibilitiesArray = new ArrayList<Integer>();
		for (int i = 0; i < possibilities.length; i++) {
			if(possibilities[i] != 0) {
				possibilitiesArray.add(possibilities[i]);
			}
		}
		for (int i = 0; i < sd.length; i++) {
			if(sd[i].getVal() == 0 ) {
				if(sd[i].getPossibleValues().isEmpty()){
					sd[i].setPossibleValues(possibilitiesArray);
				} else {
					sd[i].getPossibleValues().retainAll(possibilitiesArray);
				}
			}
		}
	}

}
