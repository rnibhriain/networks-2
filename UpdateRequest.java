import java.net.DatagramPacket;

public class UpdateRequest implements PacketContent {

	@Override
	public DatagramPacket toDatagramPacket() {
		byte [] buffer = new byte [dstLength+routerLength+5];
		DatagramPacket packet = null;
		buffer[0] = PACKET_TYPE_UPDATE;
		buffer[1] = dstType;
		buffer[2] = dstLength;

		System.arraycopy(dst, 0, buffer, 3, dstLength);

		buffer[dstLength+3] = routerType;
		buffer[dstLength+4] = routerLength;
		System.arraycopy(router, 0, buffer, dstLength+5, routerLength);

		packet = new DatagramPacket(buffer, buffer.length);

		return packet;
	}

	// Packet Structure
	// [Packet Type] [dstType] [dstLength] [dst] [routerType] [routerLength] [router] 

	// Type for tlv
	byte dstType;

	// Length for tlv
	byte dstLength;

	// final destination
	byte [] dst;

	// Type for tlv
	byte routerType;

	// Length for tlv
	byte routerLength;

	// current router address
	byte [] router;

	// this should be of type update
	byte packetType;

	public UpdateRequest (String nxt, String dst) {
		// all routers have address type "rx"
		dstType = 0;

		dstLength = (byte) dst.length();
		char [] array = dst.toCharArray();
		this.dst = new byte [array.length];
		for (int i = 0; i < dstLength; i++) {
			this.dst [i] = (byte)array[i];
		}

		// all routers have address type "rx"
		routerType = 0;

		routerLength = (byte) nxt.length();
		array = nxt.toCharArray();
		this.router = new byte [array.length];
		for (int i = 0; i < routerLength; i++) {
			this.router [i] = (byte)array[i];
		}
	}
	
	public String toString () {
		String string = "";
		
		for (int i = 0; i < dstLength; i++) {
			string += (char) dst[i];
		}
		
		string += ":";
		
		for (int i = 0; i < routerLength; i++) {
			string += (char) router[i];
		}
		
		return string;
	}

	public UpdateRequest (DatagramPacket packet) {

		byte[] buffer= null;
		buffer= packet.getData();

		packetType = buffer[0];
		dstType = buffer[1];
		dstLength = buffer[2];
		dst = new byte[dstLength];
		System.arraycopy(buffer, 3, dst, 0, dstLength);


		routerType = buffer[dstLength+3];
		routerLength = buffer[dstLength+4];
		router = new byte[routerLength];
		System.arraycopy(buffer, 5+dstLength, router, 0, routerLength);


	}
	
}
