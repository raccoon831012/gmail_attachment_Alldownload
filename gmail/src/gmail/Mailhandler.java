package gmail;

import java.io.IOException;
import java.util.Properties;

import javax.mail.FetchProfile;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.*;
import javax.swing.JTextArea;

import com.sun.mail.gimap.GmailFolder;
import com.sun.mail.gimap.GmailMessage;
import com.sun.mail.gimap.GmailSSLStore;

public class Mailhandler {
	public String username = "";
	public String password = "";
	public String path=null;
	public boolean isconnect = false;

	public GmailSSLStore store = null;
    private GmailFolder folder = null;
    private Message[] ms=null;
    
    String foldername=null;
    
    private JTextArea textArea;
	
    public Mailhandler(JTextArea textArea) {
        this.textArea = textArea;
    }
    
    private void printf(String str){
    	textArea.append(str+"\n");
    	
    	System.out.println(str);
    }
    
	public void SessionImap() throws MessagingException{
		Properties props = new Properties();
		props.put("mail.imap.host", "imap.gmail.com");
		props.put("mail.imap.port", "993");
		props.put("mail.imap.timeout", 4000);
		//props.put("mail.imap.auth.plain.disable", true);
		//props.put("mail.imap.ssl.trust", "*");
	    Session session = Session.getDefaultInstance(props, null);
        store = (GmailSSLStore) session.getStore("gimaps");
        System.out.println("Connecting...");
        store.connect(username, password);
        if(store.isConnected()){
        	isconnect = true;
        	printf(username+"connect successfully");
        	//System.out.println(username+"connect successfully");
        }
	}
	
	public void OpenFolder(String list) throws MessagingException{
		folder = (GmailFolder) store.getFolder(list);
        folder.open(Folder.READ_ONLY);
        ms = folder.getMessages();
        FetchProfile fp = new FetchProfile();
        folder.fetch(ms, fp);
	}
	
	public void DownloadMail(boolean issave) throws MessagingException, ParseException{
		try{
			GmailMessage gm;

	        for (int i=ms.length-1; i>-1; i--) {
	            gm = (GmailMessage) ms[i];
	            printf("title: "+gm.getSubject());
	            //System.out.println(gm.getSubject());
	            
	            Multipart multipart = (Multipart) gm.getContent();
	            for(int j=0;j<multipart.getCount();j++){
	            	 MimeBodyPart part = (MimeBodyPart) multipart.getBodyPart(j);
	            	    if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
	            	        // this part is attachment
	            	        // code to save attachment...
	            	    	String filename = part.getFileName();
	            	    	if(filename.contains("UTF"))
	            	    		filename = MimeUtility.decodeWord(part.getFileName());
	            	    	
	            	    	printf(filename);
	            	    	//System.out.println(filename);
	            	    	
	            	    	if(issave)
	            	    		part.saveFile(path+ "/" + filename);
	            	    }
	            }
	            printf("download done");
	        }
		}catch(IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
