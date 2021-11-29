import javax.xml.crypto.Data;

import java.util.Scanner;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
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
		System.out.println("Initialising routing map at router (" + this.node + ")...");
		initialiseRoutingMap();

		System.out.println("\nWaiting for contact at router(" + this.node + ")...");
	}

	public void send (String message) {
		InetSocketAddress dstAddress = new InetSocketAddress("controller", 51510);
		message += ":" + this.node;
		DatagramPacket packet = null;
		ObjectOutputStream ostream;
		ByteArrayOutputStream bstream;
		byte[] buffer = null;
		try {
			bstream= new ByteArrayOutputStream();
			ostream= new ObjectOutputStream(bstream);
			ostream.writeUTF(message);
			ostream.flush();
			buffer= bstream.toByteArray();
			packet = new DatagramPacket(buffer, buffer.length);
			packet.setSocketAddress(dstAddress);
			socket.send(packet);
		}
		catch(Exception e) {
			e.printStackTrace();
		} 

	}

	public void initialiseRoutingMap() {

		System.out.println("\nInitial hello to controller ... from " + this.node);

		InitialPacket hello = new InitialPacket(this.node);
		DatagramPacket packet = hello.toDatagramPacket();
		InetSocketAddress dst = new InetSocketAddress(CONTROLLER, CONTROLLER_PORT);
		packet.setSocketAddress(dst);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void onReceipt(DatagramPacket packet) {

		byte [] buffer = packet.getData();

		switch (buffer[0]) {

		// in the case that it is an string packet
		case PACKET_TYPE_STRING:
			try {

				System.out.println("\nPacket recieved at router (" + this.port + ")...");


				this.messagePacket = packet;

				StringContent content = new StringContent(packet);

				continueTransmission(packet);
			}
			catch(Exception e) {e.printStackTrace();}
			break;

			// in the case it is an update packet
		case PACKET_TYPE_CONTROLLER:

			ControllerPacket current = new ControllerPacket(packet);
			String [] table = current.toString().split(":");
			this.routingTable.put(table[0], new RoutingKey(0,table[1]));
			System.out.println("Printing maps at router (" + this.port + ")...\n");
			this.printRoutingMap();
			AckPacket hello = new AckPacket(this.node);
			packet = hello.toDatagramPacket();
			InetSocketAddress dst = new InetSocketAddress(CONTROLLER, CONTROLLER_PORT);
			packet.setSocketAddress(dst);
			try {
				socket.send(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

			// otherwise mistaken
		default:
			System.out.println("received mistaken packet");		
		}
		
		System.out.println("\nWaiting for contact at router(" + this.node + ")...");

		receive();

	}

	public void continueTransmission(DatagramPacket packet) throws IOException, InterruptedException {


		StringContent content = new StringContent(packet);

		//If router has routing knowledge of how to get to destination, send packet to next router
		if(this.routingTable.get(content.getAddress()).nextDst != null){
			System.out.println("Router knows how to get to destination..." + this.routingTable.get(content.getAddress()).nextDst);
			content.incrementHopCount();
			RoutingKey key = routingTable.get(content.getAddress());
			String nextHop = key.nextDst;

			//Set dst port of packet to that of the next router
			DatagramPacket updatedPacket = content.toDatagramPacket();
			InetSocketAddress nextAddr = new InetSocketAddress(nextHop, 51510);
			updatedPacket.setSocketAddress(nextAddr);
			socket.send(updatedPacket);

			if(nextHop == END_USER_1 || nextHop == END_USER_2 || nextHop == END_USER_3 || nextHop == END_USER_4)
				System.out.println("\nPacket sent to end user(" + nextHop + ")...");
			else
				System.out.println("\nPacket sent to next router(" + nextHop + ")...");
			System.out.println("\nWaiting for contact at router(" + this.port + ")...");
		} 

		// else send an update request
		else {
			InetSocketAddress dst = new InetSocketAddress(CONTROLLER, CONTROLLER_PORT);
			UpdateRequest request = new UpdateRequest(this.node, content.getAddress());
			DatagramPacket pack = request.toDatagramPacket();
			pack.setSocketAddress(dst);
			socket.send(packet);
			
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
