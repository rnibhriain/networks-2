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

public class SenderReceiver {

	static DatagramSocket socket;

	public static final int DEFAULT_PORT= 51510;

	static final byte TYPE_UNKNOWN = 0;
	static final byte TYPE_ID = 1;
	static final byte TYPE_COMB = 9;
	
	final static int MTU = 1500;
	

	SenderReceiver (DatagramSocket socket) { 
		this.socket = socket;
	}
	
	public static void send (int type, String message, InetSocketAddress dstAddress) {
		byte [] array = packPacket(type, message);
		DatagramPacket packet = new DatagramPacket(array, array.length);
		packet.setSocketAddress(dstAddress);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void receive () {

		DatagramPacket packet;

		ObjectInputStream ostream;
		ByteArrayInputStream bstream;
		byte[] buffer;

		try {
			buffer= new byte[MTU];
			packet= new DatagramPacket(buffer, buffer.length);
			socket.receive(packet);

			buffer= packet.getData();
			bstream= new ByteArrayInputStream(buffer);
			ostream= new ObjectInputStream(bstream);

			String data =  ostream.readUTF();
			
			System.out.println("System received: " + data);
		}
		catch(Exception e) {
			e.printStackTrace();
		}

	}


	public static byte [] packPacket (int type, String message) {
		byte [] data = null;
		DatagramPacket packet = null;
		ObjectOutputStream ostream;
		ByteArrayOutputStream bstream;
		byte[] buffer = null;
		String finalData = type + ":" + message.length() + ":" + message;
		try {
			bstream= new ByteArrayOutputStream();
			ostream= new ObjectOutputStream(bstream);
			ostream.writeUTF(finalData);
			ostream.flush();
			buffer= bstream.toByteArray();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return buffer;
	}

}
