package agents;

import behaviours.AnalyseAgentSubscribe;
import behaviours.AnalyseAgentResolve;

import jade.core.Agent;

public class AnalyseAgent extends Agent {
	protected void setup() {
		System.out.println(getLocalName() + " Intialized");
		addBehaviour(new AnalyseAgentSubscribe());
		addBehaviour(new AnalyseAgentResolve());
	}
}