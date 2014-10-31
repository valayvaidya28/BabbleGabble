import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("unused")
public class FileTransfer {

	private static int k = 0;
	public static String Send(String destPath) throws IOException{
		StringBuilder fileData = new StringBuilder(1000);//Constructs a string buffer with no characters in it and the specified initial capacity
		BufferedReader reader = new BufferedReader(new FileReader(destPath));
 
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
 
		reader.close();
 
		String returnStr = fileData.toString();
		//System.out.println(returnStr);
		return returnStr;
		/*File file = new File(attachment);
  		 
        @SuppressWarnings("resource")
		FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[5000];
        try {
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                bos.write(buf, 0, readNum); //no doubt here is 0
            }
        } catch (IOException ex) {
            Logger.getLogger(FileTransfer.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte[] bytes = bos.toByteArray();
        String temp = bytes.toString();
		return temp;*/
	}
	
	public static void Receive(String destPath, String content) {
		/*byte[] fileBytes = fileArray.getBytes();
 
        try {
	    FileOutputStream fileOuputStream = 
                  new FileOutputStream(dest); 
	    fileOuputStream.write(fileBytes);
	    fileOuputStream.close();
        }catch(Exception e){
            e.printStackTrace();
        }
		//return true;
		*/ 
		FileOutputStream fop = null;
		File file;
		//String content = "This is the text content";
 
		try {
			file = new File(destPath);
			if (!file.exists()) {
				file.createNewFile();
			}
			else{
				while(file.exists()){
					destPath = destPath + k;
					file = new File(destPath);
					k++;
				}
			}
			fop = new FileOutputStream(file);
 
			// if file doesnt exists, then create it
			
				
 
			// get the content in bytes
			byte[] contentInBytes = content.getBytes();
 
			fop.write(contentInBytes);
			fop.flush();
			fop.close();
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
