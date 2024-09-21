import java.io.*;
import java.net.Socket;

public class TCPClient {

    public static String xor(String inputString) {
        long xorKey = 344297195;
        String outputString = "";
        int len = inputString.length();

        for (int i = 0; i < len; i++) {
            outputString = outputString + Character.toString((char) (inputString.charAt(i) ^ xorKey));
        }
        return outputString;
    }

    public static long getRtt(String msg, PrintWriter out, BufferedReader in) throws IOException {
        String send = xor(msg);
        long start = System.nanoTime(); // start time
        out.println(send); // Send message to the server

        //Receiving a message from the server
        String received = in.readLine(); // reads msg from server and assigns it to a string
        long end = System.nanoTime(); // end time
        return end - start; // gets the total time for the rtt of the message
    }

    public static void measureThroughput(int msgCount, String msg, PrintWriter out, BufferedReader in) throws IOException {
        long start = System.nanoTime();

        for (int i = 0; i < msgCount; i++) {
            out.println(xor(msg));
            String response = in.readLine();
            String decodedResponse = xor(response);
        }

        long end = System.nanoTime();
        long totalTime = end - start;

        // Calculate throughput bits per second
        double throughput = (8.0 * msg.length() * msgCount) / (totalTime * 1e-9);

        System.out.println("Throughput for " + msgCount + " messages of " + msg.getBytes().length + " bytes each: " + throughput + "bps");
    }

    public static void main(String[] args) {
        Socket clientSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;

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
            //Connecting to the server
            clientSocket = new Socket(host, port);// creates a socket that is connected to the waiting server socket at the host and looks for the given port
            out = new PrintWriter(clientSocket.getOutputStream(), true); //Creates a writer in order to send messages to the server
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); //Creates a buffered reader in order to read messages from the server

            //Get the rtt of messages sized 8, 32, 512, and 1024 bytes
            long rttMsg1 = getRtt(msgs[0], out, in);
            long rttMsg2 = getRtt(msgs[1], out, in);
            long rttMsg3 = getRtt(msgs[2], out, in);
            long rttMsg4 = getRtt(msgs[3], out, in);

            System.out.println("Rtt for messages of  " + msgs[0].getBytes().length + " is " + rttMsg1 + " nanoseconds ");
            System.out.println("Rtt for messages of  " + msgs[1].getBytes().length + " is " + rttMsg2 + " nanoseconds ");
            System.out.println("Rtt for messages of  " + msgs[2].getBytes().length + " is " + rttMsg3 + " nanoseconds ");
            System.out.println("Rtt for messages of  " + msgs[3].getBytes().length + " bytes is " + rttMsg4 + " nanoseconds ");

            //get the throughput of 1024 1024Byte messages, 2048 512Byte messages and 8192 X 128Byte message
            String mgs1 = msgs[3];
            String mgs2 = msgs[2];
            String msg128 = msgs[3].substring(0,128);

            measureThroughput(1024,msgs[3],out,in);
            measureThroughput(2048,msgs[2],out,in);
            measureThroughput(8192,msg128,out,in);

            // closing everything opened
            clientSocket.close();
            out.close();
            in.close();

        } catch (IOException e) {
            System.out.println("Unable to create socket");
        }
    }
}