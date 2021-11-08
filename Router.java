import javax.xml.crypto.Data;

import java.util.Scanner;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map.Entry;

public class Router extends SenderReceiver{

	// RoutingMap has the routing table
	// <String> has the destination address
	// <RoutingKey> object for counting hops and next address
	HashMap<String, RoutingKey> routingTable;

	// costs between addresses
	HashMap<Integer, Integer> distanceMap;

	private int port = DEFAULT_PORT;
	static final int CONTROLLER_PORT = 50001;
	static final String CONTROLLER_NODE = "Controller";

	private String node;

	DatagramPacket messagePacket;

	InetSocketAddress controllerAddress;

	boolean addressReceived;

	Router (String name) {
		this.routingTable = new HashMap<String, RoutingKey>();
		this.distanceMap = new HashMap<Integer, Integer>();
		try {
			socket= new DatagramSocket(51510);
		}
		catch (SocketException e) {
			e.printStackTrace();
		}
		node = name;
		start();
		receive();
	}

	public synchronized void start() {
		System.out.println("Initialising routing map at router (" + this.port + ")...");
		this.initialiseRoutingMap();

		System.out.println("Printing maps at router (" + this.port + ")...\n");
		this. printRoutingMap();

		System.out.println("\nWaiting for contact at router(" + this.port + ")...");
	}

	// hard coded from e1 - r1 -r2 - e2
	// and from e2 - r2 - r1 - e1
	public void initialiseRoutingMap() {

		//Initialise the distanceMap table for the router
		switch(this.node) {
		
		//For router 1 do this
		case ROUTER_1:
			this.routingTable.put(END_USER_1, new RoutingKey(0,"e1"));
			this.routingTable.put(END_USER_2, new RoutingKey(0,"r2"));
			break;
			
		//For router 2 do this
		case ROUTER_2:
			this.routingTable.put(END_USER_1, new RoutingKey(0,"r1"));
			this.routingTable.put(END_USER_2, new RoutingKey(0,"e2"));
			break;
		}
	}

	public void onReceipt(DatagramPacket packet) {
		try {

			System.out.println("\nPacket recieved at router (" + this.port + ")...");
			

			this.messagePacket = packet;
			
			StringContent content = new StringContent(packet);
			System.out.println(node + " received: " + content.message);
			
			continueTransmission(packet);
		}
		catch(Exception e) {e.printStackTrace();}
	}

	public void continueTransmission(DatagramPacket packet) throws IOException, InterruptedException {
		
		
		StringContent content = new StringContent(packet);

		//If router has routing knowledge of how to get to destination, send packet to next router
		if(this.routingTable.get(content.dstAddress).nextDst != null){
			System.out.println("Router knows how to get to destination..." + this.routingTable.get(content.dstAddress).nextDst);
			content.incrementHopCount();
			RoutingKey key = routingTable.get(content.dstAddress);
			String nextHop = key.nextDst;

			//Set dst port of packet to that of the next router
			DatagramPacket updatedPacket = content.toDatagramPacket();
			InetSocketAddress nextAddr = new InetSocketAddress(nextHop, 51510);
			updatedPacket.setSocketAddress(nextAddr);
			socket.send(updatedPacket);

			if(nextHop == END_USER_1 || nextHop == END_USER_2)
				System.out.println("\nPacket sent to end user(" + nextHop + ")...");
			else
				System.out.println("\nPacket sent to next router(" + nextHop + ")...");
			System.out.println("\nWaiting for contact at router(" + this.port + ")...");
		}

	}

	public void printRoutingMap() {
		RoutingKey routingKey;
		String address;
		int hopCount;
		String nextHop;

		System.out.println("\n***Routing Map for Router(" + this.port + ")***\n");
		System.out.println("Address   |    HopCount    |   NextHop" );
		System.out.println("-------------------------------------------------------");

		for(Entry<String, RoutingKey> entry : this.routingTable.entrySet()) {

			address = entry.getKey();
			routingKey = entry.getValue();
			hopCount = routingKey.hops;
			nextHop = routingKey.nextDst;

			System.out.println("" + address + "       |       " + hopCount + "              |         " + nextHop);
		}
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the name of the router: ");
		String name = scanner.next();
		Router router = new Router(name);
		
		
	}

}
