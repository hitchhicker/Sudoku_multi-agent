package agents;

import behaviours.EnvAgentSendSudoku;
import behaviours.EnvAgentUpdate;
import behaviours.SimulAgentListen;
import jade.core.Agent;
import model.Sudoku;
import model.SudokuData;

public class EnvAgent extends Agent {
	public Sudoku s;

	protected void setup() {
		System.out.println(getLocalName() + " Intialized");
		s = Sudoku.getInstance();
		addBehaviour(new EnvAgentSendSudoku());
		addBehaviour(new EnvAgentUpdate());
	}

	public Sudoku getSudoku() {
		return s;
	}
}
