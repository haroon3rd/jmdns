package javax.jmdns.impl;
//import org.apache.log4j.Logger;
//import org.apache.log4j.PropertyConfigurator;
//import org.slf4j.simple.SimpleLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.simple.SimpleLogger;
import org.slf4j.LoggerFactory;

//simple main class from which jmdns/jmmdns can be run in a linux desktop machine.
public class Main {

	static Logger logger = LoggerFactory.getLogger(Main.class);
	//static Logger logger = Logger.getLogger(Main.class);
	static JmmdnsPlus jmdns;

	//main function
	public static void main(String argv[]) throws Exception {

		//PropertyConfigurator.configure("log4j.properties");
		System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "debug");

		//init jmmdns
		try {
			//start
			jmdns = new JmmdnsPlus();
			ExecutorService executorService = Executors.newFixedThreadPool(1);
			executorService.execute(jmdns);

			//add shutdown hook
			List<Terminable> terminableTasks = new ArrayList<Terminable>();
			terminableTasks.add(jmdns);
			Runtime.getRuntime().addShutdownHook(new ShutDownHook(terminableTasks));

			System.out.println("End of main function!");
//			TimeUnit.SECONDS.sleep(30);
//			System.out.println("##################################### Waited 30 Seconds ##########################");
//
//			jmdns.setText("amran-modified-text");
//
//			TimeUnit.SECONDS.sleep(30);
//			System.out.println("##################################### Waited 30 More Seconds ##########################");
//
//			jmdns.setText("amran-further-modified-text");

//			// List all registered services of a particular type
//			ServiceInfo[] services = jmdns.registry.list("_dnet._tcp.local.");
//			// Iterate over the list of services
//			for (ServiceInfo service : services) {
//				// Now you have references to your JmDNS instance (jmdns) and each ServiceInfo object (service)
//
//				// Perform operations with each service...
//
//				// Example: Print details of each service
//				System.out.println("Service Name: " + service.getName());
//				System.out.println("Service Type: " + service.getType());
//				System.out.println("Service Host: " + service.getHostAddress());
//
//				// Get TXT record (if any)
//				Map<String, String> txtRecord = service.getTextMap();
//				if (txtRecord != null && !txtRecord.isEmpty()) {
//					System.out.println("Service TXT Record:");
//					for (Map.Entry<String, String> entry : txtRecord.entrySet()) {
//						System.out.println(entry.getKey() + " = " + entry.getValue());
//					}
//				}
//			}



		}catch (Exception e){
			e.printStackTrace();
		}
		TimeUnit.SECONDS.sleep(30);
		System.out.println("##################################### Waited 30 Seconds ##########################");

		jmdns.setText("TEXT - CHANGE - 111111111");

		TimeUnit.SECONDS.sleep(30);
		System.out.println("##################################### Waited 30 More Seconds ##########################");

		jmdns.setText("999999999 - TEXT - CHANGE");
	}

}


