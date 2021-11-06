import java.net.DatagramSocket;
import java.net.SocketException;

public class Service extends SenderReceiver {
	Service(DatagramSocket socket) {
		super(socket);
	}


	private static int port = 51510;


	public static void main(String[] args) {
		try {
			socket= new DatagramSocket(port);
		} 
		catch (SocketException e) {
			e.printStackTrace();
		}


	}
}