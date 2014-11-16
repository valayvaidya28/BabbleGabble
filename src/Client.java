import java.awt.EventQueue;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
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
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.lang.Thread;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;

@SuppressWarnings("serial")
public class Client extends JFrame implements Runnable {

	  private JPanel contentPane;
	  private static Socket clientSocket = null;
	  private static PrintStream outputStream = null;
	  private static DataInputStream inputStream = null;
	  private static BufferedReader inputLine = null;
	  private static boolean closed = false;
	  JFrame frame = new JFrame("BabbleGabble");
	  private static String host = "192.168.177.115";
	  private static String name = null;
	  private static List archive = new List();
	  private static TextArea textArea = new TextArea();
	 // static Server server = new Server();
	  private static String attachment = null;
		
	/**
	 * Launch the application.
	 */
	
	public Client() {
	//	host = JOptionPane.showInputDialog(frame, "Enter IP of the server:");
		name = JOptionPane.showInputDialog(frame, "Enter your name:");
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
		
		JButton sendVoice = new JButton("Send Voice");
		sendVoice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(frame, "Record your message!");
				JavaSoundRecorder.main(null);
				ByteArrayOutputStream out = new ByteArrayOutputStream();
                BufferedInputStream in = null;
				try {
					in = new BufferedInputStream(new FileInputStream("/home/valayism/recording.wav"));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				int read;
                byte[] buff = new byte[1024];
                try {
					while ((read = in.read(buff)) > 0)
					{
					    out.write(buff, 0, read);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                try {
					out.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                byte[] audioBytes = out.toByteArray();
                archive.add("Sending the audio file.");
                String temp = "AudioFile " + Arrays.toString(audioBytes);
                //System.out.print(temp);
                outputStream.println(temp);
			}
		});
		sendVoice.setBounds(321, 58, 117, 25);
		contentPane.add(sendVoice);
		
		JButton btnPlay = new JButton("Play");
		btnPlay.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				/*try {
					String temp = "vlc transmitted.wav";
					Runtime.getRuntime().exec("/bin/bash -c "+temp);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}*/
				String temp = "/usr/bin/vlc";
				ProcessBuilder pb = new ProcessBuilder(temp , "transmitted.wav");
				try {
					pb.start();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnPlay.setBounds(321, 10, 117, 25);
		contentPane.add(btnPlay);
			
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
	@SuppressWarnings({ "deprecation" })
	@Override
	public void run() {
		// TODO Auto-generated method stub
		String responseLine;
		  try {
		      while ((responseLine = inputStream.readLine()) != null) {
		    	if(responseLine.contains("Send the file: ")){
		    		archive.add(responseLine);
		    		String[] words = responseLine.split(" ");	    		
		    		String temp = FileTransfer.Send(words[3]);
		    		temp = "GetTheFile " + temp;	    		
		            outputStream.println(temp);
		    	}
		    	else if(responseLine.contains("GetTheFile ") ){
		    		File dirs = new File(".");
		    		String destPath = dirs.getCanonicalPath() + File.separator + "download.txt";
		    		String[] words = responseLine.split(" ",2);
		    		FileTransfer.Receive(destPath, words[1]);
		    		archive.add("Downloading complete.");
		    	}
		    	else if (responseLine.startsWith("AudioFile ")){
		    		String[] data = responseLine.split(" ",2);		    		
		    		String[] tempData = data[1].split(", ");
		    		System.out.println(tempData.length);
		    		byte[] dataBytes = new byte[tempData.length];
		    		for(int i=1; i< tempData[1].length(); i++){
		    			dataBytes[i]=(byte) Integer.parseInt(tempData[i]);
		    		}
		    		tempData[0]=tempData[0].replace("[", "");
		    		//int temp = tempData[1].length();
		    		/*System.out.println(tempData[1].length());
		    		System.out.println(tempData[1]);*/
		    		tempData[(tempData.length)-1]=tempData[tempData.length-1].replace("]","");
		    		dataBytes[0]=(byte) Integer.parseInt(tempData[0]);
		    		dataBytes[(tempData.length)-1]=(byte) Integer.parseInt(tempData[tempData.length-1]);
		    		ByteArrayInputStream bais = new ByteArrayInputStream(dataBytes);
		    		File fileOut = new File("transmitted.wav");
		    		AudioFormat audioFormat = getAudioFormat();
		    		DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
		    		SourceDataLine sourceDataLineTemp = (SourceDataLine)AudioSystem.getLine(dataLineInfo);                                 
		    	    sourceDataLineTemp.open(audioFormat);
		    	    sourceDataLineTemp.start();
		    		AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
					long length = (long)(dataBytes.length / audioFormat.getFrameSize());
					AudioInputStream audioInputStreamTemp = new AudioInputStream(bais, audioFormat, length);
		    		if (AudioSystem.isFileTypeSupported(fileType,audioInputStreamTemp)) {
		    	         System.out.println("Trying to write");
		    	         AudioSystem.write(audioInputStreamTemp, fileType, fileOut);
		    	         System.out.println("Written successfully");
		    	    }	    		
		    		archive.add("Audio file has arrived. Click Play to listen.");
		    		System.out.println("Audio file arrived.");
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
		    	} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	  	}
	private AudioFormat getAudioFormat() {
		// TODO Auto-generated method stub
	        float sampleRate = 16000;
	        int sampleSizeInBits = 8;
	        int channels = 2;
	        boolean signed = true;
	        boolean bigEndian = true;
	        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,
	                                             channels, signed, bigEndian);
	        return format;
	}
}
