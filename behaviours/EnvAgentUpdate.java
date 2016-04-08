package behaviours;

import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import model.Sudoku;
import model.SudokuData;

/** Update cell value basing their possibilities */
public class EnvAgentUpdate extends CyclicBehaviour {

	private Sudoku sudoku;
	
	private int count = 1;
	
	/** Use startTime to calculate the execution time*/
	private long startTime = System.currentTimeMillis();
	
	public EnvAgentUpdate() {
		this.sudoku = Sudoku.getInstance();
	}
	
	public void action() {
		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
		ACLMessage message = this.getAgent().receive(mt);
		if(message != null) {
			String code = message.getConversationId().split(" ")[0];
			int index = Integer.parseInt(message.getConversationId().split(" ")[1]);
			try {
				ObjectMapper mapper = new ObjectMapper();
				SudokuData[] sd = mapper.readValue(message.getContent(), SudokuData[].class);
//				System.out.println("code: " + code + " index: " + index);
				switch (code) {
					case "r": //row
						sudoku.setRowData(sd, index);
						break;
					case "c": //column
						sudoku.setColumnData(sd, index);
						break;
					case "s": //square
						sudoku.setSquareData(sd, index);
						break;
				}
				this.getAgent().addBehaviour(new EnvAgentSendSudoku());
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			System.out.println(count / 27 + "----------------");
			count++;
			sudoku.displaySudoku();
			System.out.println("-----------------");
			if(sudoku.sudokuResolved()){
				if(sudoku.verify()){
					this.getAgent().removeBehaviour(this);
					System.out.println("DONE !!");
					long stopTime = System.currentTimeMillis();
				    long elapsedTime = stopTime - startTime;
				    System.out.println("It took " + elapsedTime + "ms in total.");
				} else {
					System.out.println("Sudoku is not valid.");
				}
				
			}
		} else {
			block();
		}
	}
}