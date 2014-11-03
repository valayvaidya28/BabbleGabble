import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JOptionPane;
import java.awt.TextArea;
import javax.swing.Box;
import java.awt.List;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.lang.Thread;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;

@SuppressWarnings("serial")
public class Client extends JFrame implements Runnable {

	private JPanel contentPane;
	private static Socket clientSocket = null;
	  // The output stream
	  private static PrintStream outputStream = null;
	  // The input stream
	  private static DataInputStream inputStream = null;
	 // private ObjectOutputStream fileStream = null;
	  private static BufferedReader inputLine = null;
	  private static boolean closed = false;
	 // private ObjectInputStream inputFileStream = null;
	 // private FileEvent fileEvent = null;
	 // private File dstFile = null;
	  //private FileOutputStream fileOutputStream = null;
	 // private String sourceFilePath = null;
	  //private String destinationPath = "/home/valayism/Downloads/";
	  JFrame frame = new JFrame("BabbleGabble");
	  private static String host = "localhost";
	  private static String name = null;
	  private static List archive = new List();
	  private static List list = new List();
	  private static TextArea textArea = new TextArea();
	 // static Server server = new Server();
	  private static String attachment = null;
		
	/**
	 * Launch the application.
	 */
	
	public Client() {
		//host = JOptionPane.showInputDialog(frame, "Server IP:", "Welcome to BabbleGabble!", JOptionPane.QUESTION_MESSAGE);
		name = JOptionPane.showInputDialog(frame, "Enter your name:", "Welcome to BabbleGabble!", JOptionPane.QUESTION_MESSAGE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		
		textArea.setBounds(26, 197, 286, 63);
		contentPane.add(textArea);
		textArea.setBackground(Color.WHITE);
		
		JButton sendButton = new JButton("send");
		sendButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String message;
				message = textArea.getText();
				outputStream.println(message.trim());
				textArea.setText("");
			}
		});
		
		sendButton.setBounds(318, 197, 120, 25);
		
		contentPane.setLayout(null);
		contentPane.add(sendButton);
		
		Box horizontalBox = Box.createHorizontalBox();
		horizontalBox.setBounds(357, 107, 1, 1);
		contentPane.add(horizontalBox);

		Box verticalBox = Box.createVerticalBox();
		verticalBox.setBounds(384, 73, 1, 1);
		contentPane.add(verticalBox);
		archive.setForeground(Color.BLACK);
		archive.setBackground(Color.WHITE);
		
		archive.setBounds(26, 10, 286, 160);
		contentPane.add(archive);
		
		list.setBounds(318, 41, 120, 129);
		list.setForeground(Color.BLACK);
		list.setBackground(Color.WHITE);
		contentPane.add(list);
		
		JButton attachButton = new JButton("Attachment");
		attachButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				attachment = JOptionPane.showInputDialog(frame, "Enter the file path:", "Send attachment", JOptionPane.QUESTION_MESSAGE);
				textArea.setText("Send the file: " + attachment);
				//attachment = "/file " + attachment;
			}
		});
		attachButton.setBounds(318, 235, 117, 25);
		contentPane.add(attachButton);
		//final Server server = new Server();
		JButton refresh = new JButton("Refresh");
		
		refresh.setBounds(318, 10, 117, 25);
		contentPane.add(refresh);
			
		addWindowListener(new WindowListener(){
			@Override
			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				// TODO Auto-generated method stub
				outputStream.println("/quit");
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	/**
	 * Create the frame.
	 */
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client frame = new Client();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		int portNumber = 4000;
	    if (args.length < 2) {
	      System.out
	          .println("Usage: Multithread chat application!\n"
	              + "Now using host=" + host + ", portNumber=" + portNumber);
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
	      System.err.println("Don't know about host " + host);
	    } catch (IOException e) {
	      System.err.println("Couldn't get I/O for the connection to the host "
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
	        int flag = 0;
	        while (!closed) {
	        	if(flag == 0)
	        		outputStream.println(name);
	        	else
	        		outputStream.println(inputLine.readLine().trim());
	        	flag++;
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
	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		// TODO Auto-generated method stub
		String responseLine;
		  try {
		      while ((responseLine = inputStream.readLine()) != null) {
		    	if(responseLine.contains("Send the file: ")){
		    		archive.add(responseLine);
		    		String[] words = responseLine.split(" ");
		    		//archive.add(words[3]);
		    		
		    		String temp = FileTransfer.Send(words[3]);
		    		archive.add(temp);
		    		//temp = temp.trim();
		    		temp = "GetTheFile " + temp;
		    		//archive.add(temp);
		            outputStream.println(temp);
		    	}
		    	else if(responseLine.contains("GetTheFile ") ){
		    		File dirs = new File(".");
		    	//	String destPath = JOptionPane.showInputDialog(frame, "Enter the file path:", "Receive attachment", JOptionPane.QUESTION_MESSAGE);
		    		String destPath = dirs.getCanonicalPath() + File.separator + "download.txt";
		    		//destPath = destPath + File.separator + "test20.txt" ;
		    		
		    		//archive.add(destPath);
		    		String[] words = responseLine.split(" ",3);
		    		archive.add(words[0]);
		    		archive.add(words[1]);
		    		//archive.add("File to download:" + words[2]);
		    		FileTransfer.Receive(destPath, words[2]);
		    		archive.add("Downloading complete.");
		    		//outputStream.println("Downloading Done!");
		    	}
		    	else{
		    			archive.add(responseLine);
		    	}
		        if (responseLine.indexOf("*** Bye") != -1)
		          break;
		      }
		      closed = true;
		    } catch (IOException e) {
		    		System.err.println("IOException:  " + e);
		    	}
	  	}
}
