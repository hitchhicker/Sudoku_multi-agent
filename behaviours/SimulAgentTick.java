package behaviours;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import agents.SimulAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import model.Sudoku;
import model.SudokuData;

/** Send regular request to analyze agent
 * 27 messages in total with 9 rows 9 columns 
 * 9 squares for each time */
public class SimulAgentTick extends TickerBehaviour {
	
	private Sudoku sudoku;
	private List<AID> arrayAgentsAid;

	private int agentAidIndex = 0;
	private int rowIndex = 0;
	private int colIndex = 0;
	private int squareIndex = 0;

	/**Send sudoku data on a period time*/
	final private static long period = 100;
	
	public SimulAgentTick(Agent simulAgent, Sudoku sudoku, List<AID> arrayAgentsAid) {
		super(simulAgent, period);
		this.sudoku = sudoku;
		this.arrayAgentsAid = arrayAgentsAid;
	}
	
	protected void onTick() {
		sudoku = ((SimulAgent) this.getAgent()).getSudoku();
		if(!sudoku.sudokuResolved()){
			try {
				for(int i = 0; i < 9; i++, agentAidIndex++) {
					SudokuData[] sd = sudoku.getRowData(rowIndex); 
					sendMessage(sd, agentAidIndex, 'r', rowIndex); 
					rowIndex++;
				}
				
				for(int i = 0; i < 9; i++, agentAidIndex++) {
					SudokuData[] sd = sudoku.getColumnData(colIndex);
					sendMessage(sd, agentAidIndex, 'c', colIndex);	
					colIndex++;
				}
				
				for(int i = 0; i < 9; i++, agentAidIndex++) {
					SudokuData[] sd = sudoku.getSquareData(squareIndex);
					sendMessage(sd, agentAidIndex, 's', squareIndex);
					squareIndex++;
				}

				rowIndex = 0;
				colIndex = 0;
				squareIndex = 0;
				agentAidIndex = 0;

			}catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Send message to analyze agent.
	 * @param sd: sudokuData array
	 * @param agentId: integer from 0 to 26
	 * @param direction: 'r'(row) or 'c'(column) or 's'(square)
	 * @param index: correspond index from 0 to 9
	 */
	public void sendMessage(SudokuData[] sd, int agentId, char direction, int index){
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		ObjectMapper mapper = new ObjectMapper();
		msg.setConversationId("" + direction + " " + index);
		StringWriter sw = new StringWriter();
		try {
			mapper.writeValue(sw, sd);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String s = sw.toString();
		msg.setContent(s);
		msg.addReceiver(arrayAgentsAid.get(agentId));
		this.getAgent().send(msg);
	}
}