package com.sister.kelompok5;

import java.io.IOException;
import java.net.URL;

import org.zeromq.ZMQ;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

public class Publisher {
	
	public static void main(String[] args) throws InterruptedException, IOException {
		// Prepare our context and publisher
		ZMQ.Context context = ZMQ.context(1);
		
		ZMQ.Socket publisher = context.socket(ZMQ.PUB);
		publisher.bind("tcp://*:5565");
		publisher.bind("ipc://currency");
	    
	    // Broadcast the update with pause
	    while (!Thread.currentThread().isInterrupted()) {
		    // Take the data of exchange rates (API)
		    // from http://jsonrates.com/code/java/
	    	// Data update each 10 minutes
		    URL url = new URL("http://jsonrates.com/get/?" + "from=USD" + "&to=IDR" + "&apiKey=your API key");
		    String data = IOUtils.toString(url);
		    
	    	// Get the date and rate from JSON
	    	JSONObject json = new JSONObject(data);
	    	String date = json.getString("utctime");
		    Double rate = json.getDouble("rate");
		    
	    	String update = String.format("Datetime (UTC): %s\nCurrency 1 USD: %.2f IDR\n", date, rate);
	    	
	    	// Send message to all subscribers
	    	publisher.send(update, 0);
	    	Thread.sleep(10000);
	    }
	    
	    publisher.send("END".getBytes(), 0);
	    Thread.sleep(10000); // Give 0MQ to flush output
	}
	
}
