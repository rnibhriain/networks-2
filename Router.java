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

public class Router extends SenderReceiver{
	
	Router (DatagramSocket socket) {
		super(socket);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
