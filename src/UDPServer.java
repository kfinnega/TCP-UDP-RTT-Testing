import java.io.*;
import java.net.*;

public class UDPServer {

    public static String xor(String inputString) {
        long xorKey = 344297195;
        String outputString = "";
        int len = inputString.length();

        for (int i = 0; i < len; i++) {
            outputString = outputString + Character.toString((char) (inputString.charAt(i) ^ xorKey));
        }
        return outputString;
    }

    public static void main(String[] args) {
        int port = 4018;

        try {
            // Starting the server
            DatagramSocket serverSocket = new DatagramSocket(port);

            while (true) {

                // waiting for the client message
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);

                // getting the message and address details from received message
                String msg = new String(receivePacket.getData(), 0, receivePacket.getLength());
                InetAddress IPAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();

                // xor the received message
                msg = xor(msg);
                byte[] sendData = msg.getBytes();

                // sending the message back
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, clientPort);
                serverSocket.send(sendPacket);
            }

        } catch (IOException e) {
            System.out.println("Unable to create socket");
        }
    }
}

