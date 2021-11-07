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

	static final byte TYPE_UNKNOWN = 0;
	static final byte TYPE_ID = 1;
	static final byte TYPE_COMB = 9;
	
	static final String END_USER_1 = "e1";
	static final String END_USER_2 = "e2";
	
	public static final String ROUTER_1 = "r1";
	public static final String ROUTER_2= "r2";
	public static final String ROUTER_3 = "r3";
	
	public static final int ROUTE_ID_1 = 1; //ROUTE_ID_1 is when E1 sends packet to E2
	public static final int ROUTE_ID_2 = 2; //ROUTE_ID_2 is when E2 sends packet to E1
	
	public static final int CONTROLLER_PORT = 50000;
	
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
