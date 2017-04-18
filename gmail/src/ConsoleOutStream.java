import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

public class ConsoleOutStream implements DocumentListener {
	JTextArea textArea = new JTextArea();
	
	public ConsoleOutStream(JTextArea textArea) {
        this.textArea = textArea;
    }
	
	@Override
	public void changedUpdate(DocumentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertUpdate(DocumentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	private void displayEditInfo(DocumentEvent e) {
		Document document = (Document)e.getDocument();
	}
}
