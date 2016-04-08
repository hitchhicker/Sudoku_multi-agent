package container;

import agents.AnalyseAgent;
import agents.EnvAgent;
import agents.SimulAgent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class MainContainer {
	public static void main (String[] args) {
		final String MAIN_PROPERTIES_FILE = "config/config";
		Runtime rt = Runtime.instance();
		Profile p = null;
		try{
			p = new ProfileImpl(MAIN_PROPERTIES_FILE);
			AgentContainer mc = rt.createMainContainer(p);
			AgentController simulAgentContoller = mc.createNewAgent("SimulAgent", SimulAgent.class.getName(), null);
			simulAgentContoller.start();
			AgentController envAgentContoller = mc.createNewAgent("EnvAgent", EnvAgent.class.getName(), null);
			envAgentContoller.start();
			createAnalyseAgents(mc);
			
		} catch(Exception ex) {
			System.out.println(ex);
		}
	}
	
	private static void createAnalyseAgents(AgentContainer container) {
		for (int i = 1; i <= 27; i++) {
			AgentController analyseAgent;
			try {
				analyseAgent = container.createNewAgent("AnalyseAgent" + i, AnalyseAgent.class.getName(), null);
				analyseAgent.start();
			} catch (StaleProxyException e) {
				e.printStackTrace();
			}
		}
	}
}
