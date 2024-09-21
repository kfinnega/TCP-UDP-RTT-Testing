import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {

    public static String xor(String inputString) {
        long xorKey = 344297195;
        String outputString = "";
        int len = inputString.length();

        for (int i = 0; i < len; i++) {
            outputString = outputString +
                    Character.toString((char) (inputString.charAt(i) ^ xorKey));
        }
        return outputString;
    }


    public static void main(String[] args) {
        int port = 4018;

        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            //Starting the server
            serverSocket = new ServerSocket(port);

            while (true) {
                // Accepts a client connection
                clientSocket = serverSocket.accept();

                // Creates a PrintWriter and BufferedReader for the connection
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                // loop for multiple messages from the client
                String msg;
                while ((msg = in.readLine()) != null) {
                    msg = xor(msg);
                    out.println(xor(msg)); // Sends the xor message back to the client
                }

                // closing everything opened
                out.close();
                in.close();
                clientSocket.close();
            }

        } catch (IOException e) {
            System.out.println("Unable to create socket");
        }
    }
}
