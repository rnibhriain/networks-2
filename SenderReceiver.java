import javax.xml.crypto.Data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public abstract class SenderReceiver {

	public static final int DEFAULT_PORT= 51510;

	// types of headers - i.e. address types
	static final byte TYPE_ID = 1;			// "r1"
	static final byte TYPE_COMB = 2;		// "scss.tcd"
	
	static final String END_USER_1 = "e1";
	static final String END_USER_2 = "e2";
	static final String END_USER_3 = "e3";
	static final String END_USER_4 = "e4";
	
	public static final String ROUTER_1 = "r1";
	public static final String ROUTER_2= "r2";
	public static final String ROUTER_3 = "r3";
	public static final String ROUTER_4 = "r4";
	public static final String ROUTER_5= "r5";
	public static final String ROUTER_6 = "r6";
	public static final String ROUTER_7 = "r7";
	public static final String ROUTER_8= "r8";
	
	public static final String CONTROLLER = "controller";
	
	public static final int CONTROLLER_PORT = 50000;
	
	// packet types
	public static final byte PACKET_TYPE_ACK = 0;
	public static final byte PACKET_TYPE_HELLO = 1;
	public static final byte PACKET_TYPE_CONTROLLER = 2;
	public static final byte PACKET_TYPE_UPDATE = 3;
	public static final byte PACKET_TYPE_STRING = 4;
	
	DatagramSocket socket;
	
	final static int MTU = 1500;
	
	public abstract void onReceipt(DatagramPacket packet) throws IOException, InterruptedException, Exception;
	
	public void receive () {

		DatagramPacket packet;

		ObjectInputStream ostream;
		ByteArrayInputStream bstream;
		byte[] buffer;

		try {
			buffer= new byte[MTU];
			packet= new DatagramPacket(buffer, buffer.length);
			socket.receive(packet);

			onReceipt(packet);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

}
