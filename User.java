import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Scanner;

public class User extends SenderReceiver{

	String node;

	User(String name) {
		try {
			socket= new DatagramSocket(51510);
		}
		catch (SocketException e) {
			e.printStackTrace();
		}
		node = name;
	}

	public void onReceipt(DatagramPacket packet) {
		try {

			System.out.println("\nPacket recieved at user (" + node + ")...");


		}
		catch(Exception e) {e.printStackTrace();}
	}

	public void send (String message, InetSocketAddress dstAddress) {
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


	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the name of the end user: ");
		String name = scanner.next();
		User user = new User(name);

		System.out.println("Would you like to send or receive? ");
		name = scanner.next();
		if (name.equals("send") && user.node.equals("e1"))
			user.send("12e20hello", new InetSocketAddress("r1", 51510));
		else if (name.equals("send") && user.node.equals("e2"))
			user.send("12e10hello", new InetSocketAddress("r2", 51510));
		else if (name.equals("receive"))
			user.receive();

	}

}
