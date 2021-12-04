import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class Controller extends SenderReceiver {

	// stores the general routing table for the network
	//
	HashMap<String, ArrayList<String>>  networkList = new HashMap<String, ArrayList<String>>();

	public static void main(String[] args) {

		Controller controller = new Controller();

	}

	Controller () {
		try {
			socket= new DatagramSocket(CONTROLLER_PORT);
		}
		catch (SocketException e) {
			e.printStackTrace();
		}
		start();
		receive();
	}

	public synchronized void start() {
		System.out.println("Initialising routing map at router (" + CONTROLLER_PORT + ")...");
		this.initialiseRoutingMap();

		System.out.println("\nWaiting for contact at controller (" + CONTROLLER_PORT + ")...");
	}

	// 8 routers as in diagram 
	public void initialiseRoutingMap() {

		// for r1
		ArrayList <String> current = new ArrayList<String>();
		current.add("e2:r3");
		current.add("e2:r3");
		current.add("e4:r3");
		current.add("e1:e1");
		networkList.put("r1", current);

		// for r2 probably wont be used
		current = new ArrayList<String>();
		current.add("e1:e1");
		current.add("e2:r3");
		current.add("e3:r3");
		current.add("e4:r3");
		networkList.put("r2", current);

		// for r3 
		current = new ArrayList<String>();
		current.add("e1:e1");
		current.add("e2:r6");
		current.add("e3:r4");
		current.add("e4:r6");
		networkList.put("r3", current);

		// for r4
		current = new ArrayList<String>();
		current.add("e1:r3");
		current.add("e2:r6");
		current.add("e3:e3");
		current.add("e4:r6");
		networkList.put("r4", current);

		// for r5
		current = new ArrayList<String>();
		current.add("e1:r4");
		current.add("e2:r4");
		current.add("e3:e3");
		current.add("e4:r4");
		networkList.put("r5", current);

		// for r6
		current = new ArrayList<String>();
		current.add("e1:r3");
		current.add("e2:e2");
		current.add("e3:r4");
		current.add("e4:e4");
		networkList.put("r6", current);

		// for r7
		current = new ArrayList<String>();
		current.add("e1:r6");
		current.add("e2:e2");
		current.add("e3:r6");
		current.add("e4:e4");
		networkList.put("r7", current);

		// for r7
		current = new ArrayList<String>();
		current.add("e1:r6");
		current.add("e2:e2");
		current.add("e3:r6");
		current.add("e4:e4");
		networkList.put("r8", current);

	}
	
	// sends an initial part of the table
	public void sendOn (String add) {
		
		InetSocketAddress dst = new InetSocketAddress(add, 51510);
		ArrayList <String> list = networkList.get(add);
		String current = list.get(0);
		String [] str = current.split(":");
		ControllerPacket pack = new ControllerPacket(str[1], str[0]);
		DatagramPacket packet = pack.toDatagramPacket();
		packet.setSocketAddress(dst);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onReceipt(DatagramPacket packet) throws IOException, InterruptedException, Exception {

		byte [] buffer = packet.getData();

		switch (buffer[0]) {

		// in the case that it is an AckPacket
		case PACKET_TYPE_ACK:
			AckPacket ack = new AckPacket(packet);
			System.out.println("Received ack from: " + ack.toString());
			break;

		// in the case that it is an initial hello
		case PACKET_TYPE_HELLO:
			InitialPacket pack = new InitialPacket(packet);
			System.out.println("Received hello from: " + pack.toString());
			sendOn(pack.toString());
			break;

		// in the case it is an update packet
		case PACKET_TYPE_UPDATE:
			UpdateRequest update = new UpdateRequest(packet);
			sendUpdate(update.toString());
			break;

		// otherwise mistaken
		default:
			System.out.println("received mistaken packet");		
		}
		
		receive();
	}
	
	public void sendUpdate (String update) {
		String [] str = update.split(":");
		System.out.println(update);
		
		// get list for router
		ArrayList <String> list = networkList.get(str[1]);
		
		String [] current = new String[2];
		
		
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
			if (list.get(i).split(":")[0].equals(str[0])) {
				current = list.get(i).split(":");
			}
			
		}	
		System.out.println(current[0] + current[1]);
		
		ControllerPacket pack = new ControllerPacket(current[1], current[0]);
		DatagramPacket packet = pack.toDatagramPacket();
		InetSocketAddress dst = new InetSocketAddress(str[1], 51510);
		packet.setSocketAddress(dst);
		
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
