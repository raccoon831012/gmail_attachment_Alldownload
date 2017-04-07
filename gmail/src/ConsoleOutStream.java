import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;

public class ConsoleOutStream extends OutputStream{
	private JTextArea textArea;
	
    public ConsoleOutStream(JTextArea textArea) {
        this.textArea = textArea;
    }

	@Override
	public void write(int b) throws IOException
	{
		byte[] bytes = {(byte)b};
	    write(bytes,0,bytes.length);
	}
	  
	public void write(byte[] bytes, int offset, int length) throws IOException
	{
	    String s = new String(bytes, offset, length);
	    textArea.append(s);
	}
    
    
    
}
