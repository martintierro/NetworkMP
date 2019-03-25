package Network;

import Game.GameState;

import java.io.*;
import java.net.*;

public class UDPClient implements Serializable{
    private DatagramSocket clientSocket;
    private InetAddress serverIPAddress;
    private int serverPort;

    public UDPClient(InetAddress serverIPAddress) {
        this.serverIPAddress = serverIPAddress;
        try {
            clientSocket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        serverPort  = 1234;
    }

    public void sendPacket(Object o) throws Exception{
        //clientSocket.setSoTimeout(30000);
        byte[] sendData = new byte[1024];
        if (o instanceof String)
            sendData = ((String) o).getBytes();
        else
            sendData = Blob.toStream(o);
        DatagramPacket datagramPacket = new DatagramPacket(sendData, sendData.length, serverIPAddress, serverPort );
        clientSocket.send(datagramPacket);
        System.out.println("Sent Packet");
    }

    public Object receivePacket() throws Exception{
        System.out.println("in receive packet");

        byte[] receiveData = new byte[1048576];
        DatagramPacket receivePacket = new DatagramPacket (receiveData, receiveData.length);
        clientSocket.receive(receivePacket);
        return Blob.toObject(receivePacket.getData());
    }

    public String receiveString() throws Exception{
        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket (receiveData, receiveData.length);
        clientSocket.receive(receivePacket);
        serverPort =  receivePacket.getPort();
        return new String(receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength());
    }

    public static void main(String args[]) throws Exception {
        BufferedReader inFromUser =
                new BufferedReader(new InputStreamReader(System.in));

        DatagramSocket clientSocket = new DatagramSocket();

        //InetAddress IPAddress = InetAddress.getByName("localhost");
        InetAddress IPAddress = InetAddress.getByName("localhost");
        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];

        String sentence = inFromUser.readLine();
        sendData = sentence.getBytes();

        DatagramPacket sendPacket =
                new DatagramPacket(sendData, sendData.length, IPAddress, 1234);

        clientSocket.send(sendPacket);

        DatagramPacket receivePacket =
                new DatagramPacket (receiveData, receiveData.length);

        clientSocket.receive(receivePacket);

        String modifiedSentence = new String (receivePacket.getData());

        System.out.println("FROM SERVER: " + modifiedSentence);
        clientSocket.close();
    }
}
