import java.net.DatagramPacket;

public interface PacketContent {

	// packet types
	public static final byte PACKET_TYPE_ACK = 0;
	public static final byte PACKET_TYPE_HELLO = 1;
	public static final byte PACKET_TYPE_CONTROLLER = 2;
	public static final byte PACKET_TYPE_UPDATE = 3;
	public static final byte PACKET_TYPE_STRING = 4;

	public String toString();
	public DatagramPacket toDatagramPacket();
}
