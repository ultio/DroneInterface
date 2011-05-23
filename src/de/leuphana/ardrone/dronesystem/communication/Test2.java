package de.leuphana.ardrone.dronesystem.communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Test2 {
	private static InetAddress address;
	static DatagramSocket sendSocket;

	public static void main(String[] args) {
		try {
			address = InetAddress.getByName("192.168.1.1");
			sendSocket = new DatagramSocket();
			sendSocket.setSoTimeout(1000);
			sendTrash();
			DatagramPacket packet = receive();
			System.out.println(packet.getData());
			System.out.println(packet.getPort());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void sendTrash() throws IOException {
		byte[] buffer = { 0x1, 0x0, 0x0, 0x0 };

		DatagramPacket packet = new DatagramPacket(buffer, buffer.length,
				address, 5554);
		sendSocket.send(packet);
		System.out.println("trash send");
	}

	public static DatagramPacket receive() throws IOException {

		byte[] buffer = new byte[1024];
		// sendSocket = new DatagramSocket();
		// recSocket.setSoTimeout(10000);
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length,
				address, 5554);
		sendSocket.receive(packet);
		return packet;
	}

}
