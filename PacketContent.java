import java.net.DatagramPacket;

public interface PacketContent {
	
	// Initial Packet Header
	public static final byte TYPE_LOCATION = 0;
	public static final byte LENGTH_LOCATION = 1;
	public static final byte DST_ADDRESS_LOCATION = 2;
	

	
	public String toString();
	public DatagramPacket toDatagramPacket();
}
