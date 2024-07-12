package javax.jmdns.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jmdns.JmmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

// Simple wrapper class that starts JmmDNS with additional support for LTE.
// Please look at the bottom of the file to see how to use this class.
public class JmmdnsPlus extends Thread implements Terminable {

     static Logger  logger  = LoggerFactory.getLogger(JmmdnsPlus.class.getName());


    //service variables
    String serviceType = "_http._tcp.local.";
    String serviceName = "HOME-PC";
    String serviceText = "AMRAN - 000000000 - ORIGINAL";
    int servicePort = 0;
    JmmDNS registry;
    ServiceInfo serviceInfo;
    SampleListener sampleListener;

    //constructor
    public JmmdnsPlus(){}

    @Override
    public void terminate() {
        try {
            if (this.registry != null) {
                this.registry.unregisterAllServices();
                this.registry.close();
                //this.registry.disableLTEsupport();
                System.out.println("terminated jmdns stuff successfully");
            }
        } catch (Exception ex) {
            System.out.println("error in terminating jmdns stuff");
            ex.printStackTrace();
        }
    }

    @Override
    public void run(){
        try{
            //get new jmmdns instance
            registry = JmmDNS.Factory.getInstance();

            //sampleListener
            sampleListener = new SampleListener();

            //serviceinfo
            serviceInfo = ServiceInfo.create(serviceType, serviceName, servicePort, 1, 1, false,  encodeServiceText(serviceText));

            //register service
            registry.registerService(serviceInfo);

            //add service listener
            registry.addServiceListener(serviceType, sampleListener);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //ServiceListener callback functions
    private static class SampleListener implements ServiceListener {

        public SampleListener(){}

        @Override
        public void serviceAdded(ServiceEvent event) {
            final String name = event.getInfo().getName();
            final String text = decodeServiceText(new String(event.getInfo().getTextBytes()));
            final String[] ips = event.getInfo().getHostAddresses();
            String ip="";
            for(String aipee:ips){ip+=aipee + " ";}

            logger.debug("_NSD_ Service Added:" + " Name: " + name + ", Text: " + text + " ips: " + ip);
        }

        @Override
        public void serviceRemoved(ServiceEvent event) {
            final String name = event.getInfo().getName();
            final String text = decodeServiceText(new String(event.getInfo().getTextBytes()));
            final String[] ips = event.getInfo().getHostAddresses();
            String ip="";
            for(String aipee:ips){ip+=aipee + " ";}

            logger.debug("_NSD_ Service Removed:" + " Name: " + name + ", Text: " + text + " ips: " + ip);

        }

        @Override
        public void serviceResolved(ServiceEvent event) {
            final String name = event.getInfo().getName();
            final String text = decodeServiceText(new String(event.getInfo().getTextBytes()));
            final String[] ips = event.getInfo().getHostAddresses();
            String ip="";
            for(String aipee:ips){ip+=aipee + " ";}

            logger.debug("_NSD_ Service Resolved:" + " Name: " + name + ", Text: " + text + " ips: " + ip);

        }

    }

    //only used for android
    public void enableNeighborDiscoveryOnLTE(){
        if (registry!=null){
            //enable lte support
            ServiceInfo serviceInfoLTE = ServiceInfo.create("LTE_LTE_LTE", serviceName, servicePort, 1, 1, true,  encodeServiceText("LTE_LTE_LTE"));
            registry.enableLTEsupport(serviceInfoLTE, sampleListener);
        }
    }

    //only used for android
    public void disableNeighborDiscoveryOnLTE(){
        if (registry!=null){
            registry.disableLTEsupport();
        }
    }

    //adds encoding with a service text before use.
    public static String encodeServiceText(String serviceText){
        //Add '!@# before and '$%^' after serviceText
        return "!@#" + serviceText + "$%^";
    }

    //decode a service text before
    public static String decodeServiceText(String serviceText){
        //check if the string has '!@# before and '$%^' after
        if(serviceText.contains("!@#") && serviceText.contains("$%^")){
            return serviceText.substring(serviceText.indexOf("!@#") + "!@#".length(), serviceText.indexOf("$%^"));
        }

        return "<empty>";
    }

    //change service text on the fly - MODDED by AMRAN
    public void setText(String serviceText) throws IOException {
        //register service
        logger.debug("Accessing SetText $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");

        /*
        //Retrieve the ServiceInfo Object:
        ServiceInfo[] services = registry.list("_dnet._tcp.local.");
        if (services.length > 0) {
            logger.debug("Service Length = " + services.length);
            ServiceInfo serviceInfo = services[0]; // Assuming you want to modify the first service found
            // Now you have the ServiceInfo object (serviceInfo) that you want to modify
        }

        // Get the current TXT record as byte[]
        byte[] txtRecord = serviceInfo.getTextBytes();

        // Modify or add entries to the TXT record
        txtRecord = serviceText.getBytes(); // Update the TXT record

        // Set the updated TXT record back to ServiceInfo
        serviceInfo.setText(txtRecord);


        //serviceInfo.setText(encodeServiceText(serviceText).getBytes());

        */
        //remove existing service
        String serviceKey = "AMRAN-SRV";
        Map<String, byte[]> properties = new HashMap<String, byte[]>();
        byte[] byteText = encodeServiceText(serviceText).getBytes();
        serviceInfo.setMyText(byteText);
        properties.put(serviceKey, byteText);
        serviceInfo.setText(properties);

        logger.debug("TEXT---UPDATE $$$$$$$$$ Text = " + Arrays.toString(byteText));
//
//        registry.unregisterService(serviceInfo);
//        logger.debug("Service Un - Registered -------------------------------------- $$$$$$$$$");
//        //register or update service
//        serviceInfo = ServiceInfo.create(serviceType, serviceName, servicePort, 1, 1, false,  encodeServiceText(serviceText));
//        //logger.debug("ServiceInfo Created $$$$$$$$$$          $$$$$$$$$$         $$$$$$$$$$         $$$$$$$$$$      $$$$$$$$$");
//        registry.registerService(serviceInfo);
//        logger.debug("Service Registered +++++++++++++++++++++++++++++++++++++      $$$$$$$$$");
    }

}


/*
//start
jmdns = new JmmdnsPlus();
ExecutorService executorService = Executors.newFixedThreadPool(1);
executorService.execute(jmdns);
//add shutdown hook
List<Terminable> terminableTasks = new ArrayList<Terminable>();
terminableTasks.add(jmdns);
Runtime.getRuntime().addShutdownHook(new ShutDownHook(terminableTasks));
*/
