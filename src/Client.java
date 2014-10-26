import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements Runnable {

  // The client socket
  private static Socket clientSocket = null;
  // The output stream
  private static PrintStream outputStream = null;
  // The input stream
  private static DataInputStream inputStream = null;

  private static BufferedReader inputLine = null;
  private static boolean closed = false;
  
  
  public static void main(String[] args) throws FileNotFoundException {

    // The default port.
    int portNumber = 3000;
    // The default houtputStreamt.
    String host = args[0];
    //byte[] myFile = new byte[1024];
    //Server srvr = new Server();
    if (args.length < 2) {
      System.out
          .println("Usage: java MultiThreadChatClient <host> <portNumber>\n"
              + "Now using host=" + host + ", portNumber=" + portNumber);
     /* if(Server.threads != null){
    	  for(int i=0; i<Server.threads.length; i++){
    		  if(Server.threads[i].clientName != null)
    			  System.out.println(Server.threads[i].clientName);
    		  else break;
      	  }
      }*/
    } else {
      host = args[0];
      portNumber = Integer.valueOf(args[1]).intValue();
    }

    /*
     * Open a socket on a given houtputStreamt and port. Open input and output streams.
     */
    try {
      clientSocket = new Socket(host, portNumber);
      inputLine = new BufferedReader(new InputStreamReader(System.in));
      outputStream = new PrintStream(clientSocket.getOutputStream());
      inputStream = new DataInputStream(clientSocket.getInputStream());
      /*FileOutputStream fileSocket = new FileOutputStream("s.pdf");
      BufferedOutputStream fileWrite = new BufferedOutputStream(fileSocket);
      int bytesRead = inputStream.read(myFile, 0, myFile.length);
      fileWrite.write(myFile, 0, bytesRead);*/
    } catch (UnknownHostException e) {
      System.err.println("Don't know about houtputStreamt " + host);
    } catch (IOException e) {
      System.err.println("Couldn't get I/O for the connection to the houtputStreamt "
          + host);
    }

    /*
     * If everything has been initialized then we want to write some data to the
     * socket we have opened a connection to on the port portNumber.
     */
    if (clientSocket != null && outputStream != null && inputStream != null) {
      try {

        /* Create a thread to read from the server. */
        new Thread(new Client()).start();
        while (!closed) {
          outputStream.println(inputLine.readLine().trim());
        }
        /*
         * close the output stream, close the input stream, close the socket.
         */
        outputStream.close();
        inputStream.close();
        clientSocket.close();
      } catch (IOException e) {
        System.err.println("IOException:  " + e);
      }
    }
  }

  /*
   * Create a thread to read from the server. (non-Javadoc)
   * 
   * @see java.lang.Runnable#run()
   */
  @SuppressWarnings("deprecation")
public void run() {
    /*
     * Keep on reading from the socket till we receive "Bye" from the
     * server. Once we received that then we want to break.
     */
    String responseLine;
    try {
      while ((responseLine = inputStream.readLine()) != null) {
        System.out.println(responseLine);
        if (responseLine.indexOf("*** Bye") != -1)
          break;
      }
      closed = true;
    } catch (IOException e) {
      System.err.println("IOException:  " + e);
    }
  }
}