package de.leuphana.ardrone.dronesystem.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import de.leuphana.ardrone.dronesystem.domain.util.Config;

/**
 * Socket that receives all navData.
 * 
 * @author Florian
 * 
 */
public class NavDataReceiver {
	private DatagramSocket navDataSocket;
	private InetAddress address;

	public NavDataReceiver() {
		try {
			this.address = InetAddress.getByName(Config.getIp());
			navDataSocket = new DatagramSocket(Config.getNavDataPort());
			navDataSocket.setSoTimeout(3000);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public byte[] receive() {
		byte[] buffer = new byte[1024];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length,
				address, 5554);
		try {
			navDataSocket.receive(packet);
			return packet.getData();
		} catch (IOException e) {
			e.printStackTrace();
			return new byte[] { 0 };
		}
	}

	public void sendTrash() throws IOException {
		byte[] buffer = { 0x1, 0x0, 0x0, 0x0 };
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length,
				address, 5554);
		try {
			navDataSocket.send(packet);

		} catch (IOException e) {
			System.out.println("init sequence failed!");
			// navDataSocket.close();
			throw e;
		}
	}
}
