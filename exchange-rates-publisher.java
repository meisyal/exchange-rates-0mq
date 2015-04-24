package com.sister.kelompok5;

import java.io.IOException;
import java.net.URL;

import org.zeromq.ZMQ;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

public class DurablePub {

	public static void main(String[] args) throws InterruptedException, IOException {
		// Prepare our context and publisher
		ZMQ.Context context = ZMQ.context(1);

		ZMQ.Socket publisher = context.socket(ZMQ.PUB);
		publisher.bind("tcp://*:5565");
		publisher.bind("ipc://currency");
		//ZMQ.Socket sync = context.socket(ZMQ.PULL);

	    //sync.bind("tcp://*:5564");

	    // We send updates via this socket
	    //publisher.bind("tcp://*:5565");

	    // Wait for synchronization request
	    //sync.recv(0);

	    // Broadcast the update with pause
	    while (!Thread.currentThread().isInterrupted()) {
		    // Take the data of exchange rates (API)
		    // from http://jsonrates.com/code/java/
	    	// Data update each 10 minutes
		    URL url = new URL("http://jsonrates.com/get/?" + "from=USD" + "&to=IDR" + "&apiKey=jr-8ec48e5075aaacc170082a93814b769a");
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
	    //publisher.close();
	    //context.term();

	    // Now broadcast exactly 10 updates with pause
	    // for (int i = 0; i < 10; i++) {
	      //String msg = String.valueOf(rate);
	      //publisher.send(msg.getBytes(), 0);
	      //Thread.sleep(1000);
	    //}

	    //publisher.send("END".getBytes(), 0);
	    //Thread.sleep(1000); // Give 0MQ/2.0.x to flush output
	}

}
