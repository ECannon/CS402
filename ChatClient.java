import java.io.*;
import java.net.*;
import java.util.*;

public class ChatClient {
	public static void main(String[] args) {
		
		DatagramSocket socket = null;
		DatagramPacket outPacket = null;
		byte[] outBuf;
		final int PORT = 8888;	
		
		try {
			
			Scanner sc = new Scanner(System.in);
			System.out.println("Enter your username:");
			String name = sc.nextLine();
			socket = new DatagramSocket();
			Thread r = new Thread(new Receiver());
			r.start();
			System.out.println("Start typing...");
			String msg;
			
			while(true){
				
				msg = name + ": " +sc.nextLine();
				outBuf = msg.getBytes();
				InetAddress address = InetAddress.getByName("224.2.2.3");
				outPacket = new DatagramPacket(outBuf, outBuf.length, address, PORT);
				socket.send(outPacket);
			}
		} catch (IOException ioe) {
			System.out.println(ioe);
		}	
	}
}

class Receiver implements Runnable {
	
	private int multicastport;
	DatagramPacket inPacket;
	byte[] inBuf;
	
	public Receiver() {
		multicastport = 8888;
		inPacket = null;
		inBuf = new byte[256];
	}

	@Override
	public void run(){
		inPacket = new DatagramPacket(inBuf, inBuf.length);
		try {
			
			InetAddress mcastaddr = InetAddress.getByName("224.2.2.3");
			int multicastport = 8888;
			NetworkInterface netif = NetworkInterface.getByInetAddress(mcastaddr);
			InetSocketAddress groupaddr = new InetSocketAddress(mcastaddr, multicastport);
			MulticastSocket socket = new MulticastSocket(multicastport);
			socket.joinGroup(groupaddr, netif); // Tell the router to associate the multicast socket 
			
			while(true) {
				
		        inPacket = new DatagramPacket(inBuf, inBuf.length);
		        socket.receive(inPacket);  // receive a multicast
		        String msg = new String(inBuf, 0, inPacket.getLength());
		        System.out.println();
		        System.out.println(msg);
			}
			
		} catch (IOException ioe) {
			System.out.println(ioe);
		}	
	}
}
