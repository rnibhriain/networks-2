import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;

public class AckPacket implements PacketContent {
	
	byte type;
	byte [] address;
	byte addressLength;

	public AckPacket(DatagramPacket packet) {
		byte [] buffer = packet.getData();
		type = buffer[1];
		addressLength = buffer[2];
		address = new byte [addressLength];
		System.arraycopy(buffer, 3, address, 0, addressLength);
	}
	
	public AckPacket (String address) {
		// all routers have address type "rx"
		type = 0;
		
		addressLength = (byte) address.length();
		char [] array = address.toCharArray();
		this.address = new byte [array.length];
		for (int i = 0; i < addressLength; i++) {
			this.address [i] = (byte)array[i];
		}
		
	}
	
	public String toString () {
		String string = "";
		
		for (int i = 0; i < addressLength; i++) {
			string += (char) address[i];
		}
		return string;
	}

	@Override
	public DatagramPacket toDatagramPacket() {
		byte [] buffer = new byte [address.length+3];
		DatagramPacket packet = null;
		buffer[0] = PACKET_TYPE_ACK;
		buffer[1] = type;
		buffer[2] = addressLength;
		
		// copies in the address
		System.arraycopy(address, 0, buffer, 3, addressLength);
		packet = new DatagramPacket(buffer, buffer.length);
		
		return packet;
	}

}
