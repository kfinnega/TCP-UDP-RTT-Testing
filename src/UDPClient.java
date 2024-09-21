import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {

    public static String xor(String inputString) {
        long xorKey = 344297195;
        String outputString = "";
        int len = inputString.length();
        for (int i = 0; i < len; i++) {
            outputString = outputString + Character.toString((char) (inputString.charAt(i) ^ xorKey));
        }
        return outputString;
    }

    public static long getRtt(String msg, DatagramSocket socket, InetAddress address, int port) throws IOException {
        String send = xor(msg);
        byte[] sendData = send.getBytes();
        byte[] receiveData = new byte[1024];

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, port);
        long start = System.nanoTime();
        socket.send(sendPacket);//send message
    
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(receivePacket);//get message back
        long end = System.nanoTime();
        return end - start;
    }

    public static void sendMessages(String msg, int msgCount, DatagramSocket socket, InetAddress address, int port) throws IOException {
        long totalTime = 0;
        for (int i = 0; i < msgCount; i++) {
            totalTime += getRtt(msg, socket, address, port);
        }
        double throughput = (8.0 * msg.length() * msgCount) / (totalTime * 1e-9); // bits per second
        System.out.println("Throughput for " + msgCount + " messages of size " + msg.length() + " bytes is: " + throughput + " bps");
    }

    public static void main(String[] args) {
        // size 8, 32, 512, and 1024 bytes
        String[] msgs = new String[4];
        msgs[0] = "eightbts";
        msgs[1] = "to be -32- bytesto be -32- bytes";
        msgs[2] = "This message is going to be -512- bytes. So I am just going to write about nothing and just babble on because I have decided it this way. " +
                " There is definitely a better way to do this, this is probably the wrong way and I won't be able to do the demo. But that's okay because I have" +
                " already ruined my chances to pass but why not. Still need to get roughly 150 characters in order to get the 512 bytes. The new Mandalorian season " +
                " is not that very good. I need to find out how to throw hard because 89 is very slow";
        msgs[3] = "This message is going to be -1024- bytes. So I am just going to write about nothing and just babble on because I have decided it this way." +
                " There is definitely a better way to do this, this is probably the wrong way and I won't be able to do the demo. But that's okay because I have " +
                " already ruined my chances to pass but why not. Still need to get roughly 150 characters in order to get the -1024- bytes. The new Mandalorian " +
                " season is not that very good. I need to find out how to throw hard because 89mph is very slow. I am getting recruited by Rutgers and va tech for my " +
                " last year but I need to figure out how to get in. Isn't it great I spent my entire life trying to get to one of those places and I might not be able " +
                " to because I decided to challenge myself academically 3 years ago and choose computer sciencewhy? What a genius I am. I like to make things hard on myself, " +
                " don't I? Just a touch of self-sabotage in everything hahaha. I am going to figure it out and get it done. I don't have a choice in the matter.";


        String host = "localhost";
        int port = 4018;

        try {
            DatagramSocket clientSocket = new DatagramSocket();//Creating connection
            InetAddress IPAddress = InetAddress.getByName(host);

            //Get the rtt of messages sized 8, 32, 512, and 1024 bytes
            long rttMsg1 = getRtt(msgs[0], clientSocket, IPAddress, port);
            long rttMsg2 = getRtt(msgs[1], clientSocket, IPAddress, port);
            long rttMsg3 = getRtt(msgs[2], clientSocket, IPAddress, port);
            long rttMsg4 = getRtt(msgs[3], clientSocket, IPAddress, port);

            System.out.println("Rtt for messages of  " + msgs[0].getBytes().length + " is " + rttMsg1 + " nanoseconds ");
            System.out.println("Rtt for messages of  " + msgs[1].getBytes().length + " is " + rttMsg2 + " nanoseconds ");
            System.out.println("Rtt for messages of  " + msgs[2].getBytes().length + " is " + rttMsg3 + " nanoseconds ");
            System.out.println("Rtt for messages of  " + msgs[3].getBytes().length + " bytes is " + rttMsg4 + " nanoseconds ");

            //get the throughput of 1024 1024Byte messages, 2048 512Byte messages and 8192 X 128Byte message
            String msg1 = msgs[3];
            String msg2 = msgs[2];
            String msg128 = msgs[3].substring(0, 128);

            sendMessages(msg1, 1024, clientSocket, IPAddress, port);
            sendMessages(msg2, 2048, clientSocket, IPAddress, port);
            sendMessages(msg128, 8192, clientSocket, IPAddress, port);


            clientSocket.close(); // closing the socket

        } catch (IOException e) {
            System.out.println("Unable to create socket");
        }
    }
}
