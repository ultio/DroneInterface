package de.leuphana.ardrone.dronesystem.communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Test {
	private static InetAddress address;

	public static void main(String[] args) {
		try {
			address = InetAddress.getByName("192.168.1.1");
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
		byte[] buffer = { 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
		DatagramSocket sendSocket = new DatagramSocket();
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length,
				address, 5554);
		try {
			sendSocket.send(packet);
			System.out.println("trash send");

		} catch (IOException e) {
			System.out.println("init sequence failed!");
			throw e;
		} finally {
			sendSocket.close();
		}
	}

	public static DatagramPacket receive() throws IOException {

		byte[] buffer = new byte[2048];
		DatagramSocket recSocket = new DatagramSocket();
		recSocket.setSoTimeout(10000);
		recSocket.connect(address, 5554);
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length,
				address, 5554);
		recSocket.receive(packet);
		return packet;
	}

}
