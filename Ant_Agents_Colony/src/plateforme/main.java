package plateforme;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.ControllerException;

public class main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Runtime runtime = Runtime.instance();
			Properties p = new Properties();
			p.setProperty(Profile.GUI, "true");
			ProfileImpl pi = new ProfileImpl(p);
			
			AgentContainer c = runtime.createMainContainer(pi);
			c.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
