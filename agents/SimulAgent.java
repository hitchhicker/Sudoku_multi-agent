package agents;

import model.Sudoku;
import model.SudokuData;
import behaviours.SimulAgentListen;
import behaviours.SimulAgentReceiveCalc;
import behaviours.SimulAgentReceiveSudoku;
import jade.core.Agent;

public class SimulAgent extends Agent {

	public Sudoku sudoku;
	
	protected void setup() {
		System.out.println(getLocalName() + " Intialized");
		addBehaviour(new SimulAgentReceiveSudoku());
		addBehaviour(new SimulAgentListen());
		addBehaviour(new SimulAgentReceiveCalc());
	}
	
	public void setSudoku(Sudoku s) {
		sudoku = s;
	}
	
	public Sudoku getSudoku() {
		return sudoku;
	}
}