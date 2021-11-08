import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class StringContent implements PacketContent {
	
	int type;
	int length;
	String dstAddress;
	String message;
	byte hops;
	
	StringContent (DatagramPacket packet) throws IOException {
		ObjectInputStream ostream;
		ByteArrayInputStream bstream;
		byte[] buffer;
		// extract data from packet
		buffer= packet.getData();
		bstream= new ByteArrayInputStream(buffer);
		ostream= new ObjectInputStream(bstream);
		
		message = ostream.readUTF();
		char [] array = message.toCharArray();
		type = Character.getNumericValue(array[0]);
		length = Character.getNumericValue(array[1]);
		String dst = "";
		for (int i = 2; i < length+2; i++) {
			dst += array[i];
		}
		dstAddress = dst;
		String msg = "";
		for (int i = 2 + length; i < array.length; i++) {
		//	msg += buffer[i];
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
