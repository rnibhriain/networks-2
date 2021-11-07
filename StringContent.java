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
	String dstAddress;
	String message;
	byte hops;
	
	StringContent (DatagramPacket packet) {
		byte [] buffer= packet.getData();
		type = buffer[0];
		length = buffer[1];
		String dst = "";
		for (int i = 0; i < length; i++) {
			dst += buffer[i+2];
		}
		dstAddress = dst;
		String msg = "";
		for (int i = 2 + length; i < buffer.length; i++) {
			msg += buffer[i];
		}
		message = msg;
	}


	@Override
	public DatagramPacket toDatagramPacket() {
		DatagramPacket packet = null;
		byte [] data = null;
		ObjectOutputStream ostream;
		ByteArrayOutputStream bstream;
		byte[] buffer = null;
		String finalData = "" + type + length + dstAddress + hops + message;
		
		try {
			bstream= new ByteArrayOutputStream();
			ostream= new ObjectOutputStream(bstream);
			ostream.writeUTF(finalData);
			ostream.flush();
			buffer= bstream.toByteArray();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		packet = new DatagramPacket(buffer, buffer.length);
		
		return packet;
	}
	
	public void incrementHopCount () {
		hops++;
	}

}
