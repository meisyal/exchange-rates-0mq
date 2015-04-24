package com.sister.kelompok5;

import org.zeromq.ZMQ;
import org.joda.time.DateTime;

public class DurableSub {

	//private static Scanner scanner;

	public static void main(String[] args) {
        ZMQ.Context context = ZMQ.context(1);

        // Connect our subscriber socket
        System.out.println("Collecting updates from exchange rates provider");
        ZMQ.Socket subscriber = context.socket(ZMQ.SUB);
        //subscriber.setIdentity("hello".getBytes());

        // Synchronize with the publisher
        //ZMQ.Socket sync = context.socket(ZMQ.PUSH);

        subscriber.connect("tcp://localhost:5565");
        subscriber.subscribe("".getBytes());
        //sync.connect("tcp://localhost:5564");
        //sync.send("".getBytes(), 0);

        //scanner = new Scanner(System.in);

        // Get updates, expect random Ctrl-C death
        String update = "";
        while (!update.equalsIgnoreCase("END")) {
        //for (int update_init = 0; update_init < 10; update_init++) {
        	 update = new String(subscriber.recv(0));
             //double currency = Double.parseDouble(msg);

             //System.out.println("Masukkan mata uang dalam USD:");

             //double money = scanner.nextDouble();

             //double result = currency * money;
        	 // Get local date and time using Joda-time
             DateTime dt = new DateTime();
             String localdt = dt.toString();

             System.out.println("Datetime (local): " + localdt);
             System.out.println(update);
        }
        //}
        //subscriber.close();
        //context.term();
    }

}
