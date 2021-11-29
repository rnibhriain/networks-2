import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class StringContent implements PacketContent {
	
	byte type;
	byte length;
	byte hops;
	byte [] address;
	byte addressLength;
	byte [] message;

	public StringContent(DatagramPacket packet) {
		byte [] buffer = packet.getData();
		type = buffer[1];
		addressLength = buffer[2];
		address = new byte [addressLength];
		message = new byte[buffer.length-(addressLength+3)];
		System.arraycopy(buffer, 3, address, 0, addressLength);
		System.arraycopy(buffer, 3+addressLength, message, 0, buffer.length-(addressLength+3));
	}
	
	public StringContent (String address, String message) {
		// all users have address type "ex"
		type = 0;
		hops = 0;
		addressLength = (byte) address.length();
		char [] array = address.toCharArray();
		this.address = new byte [array.length];
		for (int i = 0; i < addressLength; i++) {
			this.address [i] = (byte)array[i];
		}
		array = message.toCharArray();
		this.message = new byte [array.length];
		for (int i = 0; i < this.message.length; i++) {
			this.message [i] = (byte)array[i];
		}
	}
	
	public String getAddress () {
		String string = "";
		
		for (int i = 0; i < addressLength; i++) {
			string += (char) address[i];
		}
		
		return string;
	}
	
	public String toString () {
		String string = "";
		
		for (int i = 0; i < addressLength; i++) {
			string += (char) address[i];
		}
		string += ":";
		for (int i = 0; i < message.length; i++) {
			string += (char) message[i];
		}
		return string;
	}

	@Override
	public DatagramPacket toDatagramPacket() {

		byte [] buffer = new byte [address.length+3+message.length];
		DatagramPacket packet = null;
		buffer[0] = PACKET_TYPE_STRING;
		buffer[1] = type;
		buffer[2] = addressLength;
		
		// copies in the address
		System.arraycopy(address, 0, buffer, 3, addressLength);
		
		// copies in the message
		System.arraycopy(message, 0, buffer, 3+addressLength, message.length);
		
		packet = new DatagramPacket(buffer, buffer.length);
		
		return packet;
	}

	public void incrementHopCount() {
		hops++;
	}
	
}
