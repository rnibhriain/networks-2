import java.net.DatagramPacket;

public class ControllerPacket implements PacketContent{

	@Override
	public DatagramPacket toDatagramPacket() {
		byte [] buffer = new byte [dstLength+nextHopLength+5];
		DatagramPacket packet = null;
		buffer[0] = PACKET_TYPE_CONTROLLER;
		buffer[1] = dstType;
		buffer[2] = dstLength;

		System.arraycopy(dst, 0, buffer, 3, dstLength);

		buffer[dstLength+3] = nextHopType;
		buffer[dstLength+4] = nextHopLength;
		System.arraycopy(nextHop, 0, buffer, dstLength+5, nextHopLength);

		packet = new DatagramPacket(buffer, buffer.length);

		return packet;
	}

	// these controller packets will only have type one headers
	// i.e. they will go to tcd before scss

	// Packet Structure
	// [Packet Type] [dstType] [dstLength] [dst] [nextHopType] [nextHopLength] [nextHop] 

	// Type for tlv
	byte dstType;

	// Length for tlv
	byte dstLength;

	// final destination
	byte [] dst;

	// Type for tlv
	byte nextHopType;

	// Length for tlv
	byte nextHopLength;

	// next location on the route
	byte [] nextHop;

	// this should be of type controller packet
	byte packetType;

	public ControllerPacket (String nxt, String dst) {
		// all routers have address type "rx"
		dstType = 0;

		dstLength = (byte) dst.length();
		char [] array = dst.toCharArray();
		this.dst = new byte [array.length];
		for (int i = 0; i < dstLength; i++) {
			this.dst [i] = (byte)array[i];
		}

		// all routers have address type "rx"
		nextHopType = 0;

		nextHopLength = (byte) nxt.length();
		array = nxt.toCharArray();
		this.nextHop = new byte [array.length];
		for (int i = 0; i < nextHopLength; i++) {
			this.nextHop [i] = (byte)array[i];
		}
	}
	
	public String toString () {
		String string = "";
		
		for (int i = 0; i < dstLength; i++) {
			string += (char) dst[i];
		}
		
		string += ":";
		
		for (int i = 0; i < nextHopLength; i++) {
			string += (char) nextHop[i];
		}
		
		return string;
	}

	public ControllerPacket (DatagramPacket packet) {

		byte[] buffer= null;
		buffer= packet.getData();

		packetType = buffer[0];
		dstType = buffer[1];
		dstLength = buffer[2];
		dst = new byte[dstLength];
		System.arraycopy(buffer, 3, dst, 0, dstLength);


		nextHopType = buffer[dstLength+3];
		nextHopLength = buffer[dstLength+4];
		nextHop = new byte[nextHopLength];
		System.arraycopy(buffer, 5+dstLength, nextHop, 0, nextHopLength);


	}

}
