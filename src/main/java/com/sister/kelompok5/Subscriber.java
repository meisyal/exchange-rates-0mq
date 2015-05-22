package com.sister.kelompok5;

import org.zeromq.ZMQ;
import org.joda.time.DateTime;

public class Subscriber {

	public static void main(String[] args) {
        ZMQ.Context context = ZMQ.context(1);

        // Connect our subscriber socket
        System.out.println("Collecting updates from exchange rates provider");
        ZMQ.Socket subscriber = context.socket(ZMQ.SUB);
        
        subscriber.connect("tcp://localhost:5565");
        subscriber.subscribe("".getBytes());
        
        // Get updates, expect random Ctrl-C death
        String update = "";
        
        while (!update.equalsIgnoreCase("END")) {
        	 update = new String(subscriber.recv(0));
        	 
        	 // Get local date and time using Joda-time
             DateTime dt = new DateTime();
             String localdt = dt.toString();
             
             System.out.println("Datetime (local): " + localdt);
             System.out.println(update);
        }
    }
	
}
