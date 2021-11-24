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
			StringContent content = new StringContent(packet);
			System.out.println(node + " received: " + content.toString());

		}
		catch(Exception e) {e.printStackTrace();}
	}


	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the name of the end user: ");
		String name = scanner.next();
		User user = new User(name);

		System.out.println("Would you like to send or receive? ");
		name = scanner.next();
		System.out.println("Where would you like to send it? ");
		String location = scanner.next();
		
		InetSocketAddress dst = null;
		DatagramPacket packet = null;
		
		if (name.equals("send") && user.node.equals("e1")) {
			dst = new InetSocketAddress("r1", 51510);
			StringContent pack = new StringContent(location, "hello");
			packet = pack.toDatagramPacket();
			packet.setSocketAddress(dst);
		} else if (name.equals("send") && user.node.equals("e2")) {
			dst = new InetSocketAddress("r7", 51510);
			StringContent pack = new StringContent(location, "hello");
			packet = pack.toDatagramPacket();
			packet.setSocketAddress(dst);
		} else if (name.equals("send") && user.node.equals("e3")) {
			dst = new InetSocketAddress("r5", 51510);
			StringContent pack = new StringContent(location, "hello");
			packet = pack.toDatagramPacket();
			packet.setSocketAddress(dst);
		} else if (name.equals("send") && user.node.equals("e4")) {
			dst = new InetSocketAddress("r8", 51510);
			StringContent pack = new StringContent(location, "hello");
			packet = pack.toDatagramPacket();
			packet.setSocketAddress(dst);
		} else if (name.equals("receive"))
			user.receive();

	}

}
