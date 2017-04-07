import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.apache.commons.codec.binary.Hex;

import gmail.Mailhandler;


@SuppressWarnings("serial")
public class GUI extends JFrame {
	private JPanel contentPane;
	private JTextField textField_email;
	//private JTextField textField_password;
	private JTextField textField_folder;
	private JPasswordField textField_password;
	
	Mailhandler mailhandler = new Mailhandler();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws NoSuchAlgorithmException 
	 * @throws IOException 
	 */
	public GUI() throws NoSuchAlgorithmException, IOException {
		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
		keyGen.init(128,new SecureRandom( ) );
		SecretKey secretKey = keyGen.generateKey();
		byte[] iv = new byte[128 / 8]; 
		SecureRandom prng = new SecureRandom();
		prng.nextBytes(iv);
		
		setResizable(false);
		setTitle("GmailDownload");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 558, 382);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        try {
		        	if(mailhandler.isconnect)mailhandler.store.close();
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		});
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(15, 15, 528, 172);
		contentPane.add(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 13));
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		panel_2.add(textArea, BorderLayout.CENTER);
		
		
		PrintStream printStream = new PrintStream(new ConsoleOutStream(textArea));
		System.setOut(printStream);
		System.setErr(printStream);
		
		
		JScrollPane scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);;
		panel_2.add(scrollPane);
		
		
		
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.setBounds(286, 200, 257, 129);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblNewLabel_2 = new JLabel("標籤");
		lblNewLabel_2.setBounds(11, 9, 36, 23);
		panel_1.add(lblNewLabel_2);
		
		JComboBox<String> comboBox_folder = new JComboBox<String>();
		comboBox_folder.setBounds(54, 6, 188, 29);
		panel_1.add(comboBox_folder);
		
		JButton button_view = new JButton("預覽");
		button_view.setEnabled(false);
		button_view.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					mailhandler.OpenFolder();
					mailhandler.DownloadMail(false);
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		button_view.setBounds(11, 83, 116, 31);
		panel_1.add(button_view);
		
		JButton button_download = new JButton("下載");
		button_download.setEnabled(false);
		button_download.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!mailhandler.path.isEmpty()){
					try {
						mailhandler.OpenFolder();
						mailhandler.DownloadMail(true);
					} catch (MessagingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					System.out.println("Error: Empty path");
				}
			}
		});
		button_download.setBounds(126, 83, 116, 31);
		panel_1.add(button_download);
		
		JButton button_folder = new JButton("瀏覽");
		button_folder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(null);
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if(chooser.showOpenDialog(button_folder)==JFileChooser.APPROVE_OPTION){
					String str = chooser.getSelectedFile().getAbsolutePath();
					textField_folder.setText(str);
					mailhandler.path = str;
				}
			}
		});
		
		textField_folder = new JTextField();
		textField_folder.setBounds(84, 48, 158, 29);
		panel_1.add(textField_folder);
		textField_folder.setColumns(10);
		button_folder.setBounds(11, 47, 69, 31);
		panel_1.add(button_folder);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(15, 200, 256, 129);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Email");
		lblNewLabel.setBounds(11, 18, 48, 23);
		panel.add(lblNewLabel);
		
		textField_email = new JTextField();
		textField_email.setBounds(54, 15, 187, 29);
		panel.add(textField_email);
		textField_email.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("密碼");
		lblNewLabel_1.setBounds(11, 55, 36, 23);
		panel.add(lblNewLabel_1);
		
		textField_password = new JPasswordField();
		textField_password.setBounds(54, 52, 187, 29);
		panel.add(textField_password);
		textField_password.setColumns(10);
		
		
		JCheckBox chckbx_auto = new JCheckBox("自動登入");
		chckbx_auto.setBounds(11, 89, 111, 31);
		panel.add(chckbx_auto);
		File auto = new File("auto.txt");
		if(!auto.exists()){
			auto.createNewFile();
		}
		try {
		
			@SuppressWarnings("resource")
			Scanner scan = new Scanner(auto);
			if(scan.hasNext() && scan.nextBoolean()){
				chckbx_auto.setSelected(true);
				mailhandler.username = scan.next();
				textField_email.setText(mailhandler.username);
				
				String keydata = scan.next();
				String ivdata = scan.next();
				String data = scan.next();
				
				byte[] keycode = Hex.decodeHex(keydata.toCharArray());
				byte[] ivcode = Hex.decodeHex(ivdata.toCharArray());
				byte[] encoded = Hex.decodeHex(data.toCharArray());
				
				SecretKey key = new SecretKeySpec(keycode, "AES");
				//System.out.println(Hex.encodeHex(ivcode));
				mailhandler.password = Decrypt(key, encoded, ivcode);
				textField_password.setText(mailhandler.password);
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		JButton btnNewButton_logout = new JButton("登出");
		JButton btnNewButton_login = new JButton("登入");
		btnNewButton_login.setBounds(133, 89, 111, 31);
		panel.add(btnNewButton_login);
		btnNewButton_login.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
					if(textField_email.getText().isEmpty()||
							textField_password.getPassword().length==0){
						System.out.println("please enter email or password!!!");}
					else{
						mailhandler.username = textField_email.getText();
						
						FileWriter writer = new FileWriter(auto, false);

						if(chckbx_auto.isSelected()){
							writer.write("true"+"\r\n");
							//System.out.println(mailhandler.username);
							writer.write(mailhandler.username+"\r\n");
							
							byte[] cipher = Encrypt(secretKey, iv, mailhandler.password);
							writer.write(String.valueOf(Hex.encodeHex(secretKey.getEncoded()))+"\r\n");
							writer.write(String.valueOf(Hex.encodeHex(iv))+"\r\n");
							writer.write(String.valueOf(Hex.encodeHex(cipher))+"\r\n");
						}
						else{
							writer.write("false");
							
						}
						writer.flush();
						writer.close();
						
						char[] input = textField_password.getPassword();
						mailhandler.password="";
						for(char c: input)
							mailhandler.password+=c;
						
						mailhandler.SessionImap();
						if(mailhandler.isconnect){
							Folder[] folder = mailhandler.store.getDefaultFolder().list();
							for(Folder fd : folder){
								comboBox_folder.addItem(fd.getName());
							}
						}else System.out.println("Connection Failed");
						
						btnNewButton_logout.setEnabled(true);
						btnNewButton_logout.setVisible(true);
						btnNewButton_login.setEnabled(false);
						btnNewButton_login.setVisible(false);
						button_view.setEnabled(true);
						button_download.setEnabled(true);
					}
				} catch ( Exception e) {
					// TODO Auto-generated catch block
					e.getMessage();
				}
			}
			});
		
		
		
		btnNewButton_logout.setEnabled(false);
		btnNewButton_logout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(mailhandler.isconnect) {try {
					mailhandler.store.close();
				} catch (MessagingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}}
				btnNewButton_logout.setEnabled(false);
				btnNewButton_logout.setVisible(false);
				btnNewButton_login.setEnabled(true);
				btnNewButton_login.setVisible(true);
				button_view.setEnabled(false);
				button_download.setEnabled(false);
				comboBox_folder.removeAllItems();
			}
		});
		btnNewButton_logout.setBounds(133, 89, 111, 31);
		panel.add(btnNewButton_logout);
		
		if(chckbx_auto.isSelected()){
			btnNewButton_login.doClick();
		}
	}
	
	public static byte[] Encrypt(SecretKey secretKey, byte[] iv, String msg) throws Exception{
		  Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING"); 
		  cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));    
		  //System.out.println("AES_CBC_PKCS5PADDING IV:"+cipher.getIV());
		  //System.out.println("AES_CBC_PKCS5PADDING Algoritm:"+cipher.getAlgorithm());
		  byte[] byteCipherText = cipher.doFinal(msg.getBytes("UTF-8"));
		  //System.out.println("加密結果的Base64編碼：" + Base64.getEncoder().encodeToString(byteCipherText));

		  return byteCipherText;
		 }
		 
	public static String Decrypt(SecretKey secretKey, byte[] cipherText, byte[] iv) throws Exception{
		//System.out.println("key: "+Hex.encodeHex(secretKey.getEncoded()));
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING"); 
		  cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));    
		  byte[] decryptedText = cipher.doFinal(cipherText);
		  String strDecryptedText = new String(decryptedText);
		  //System.out.println("解密結果：" + strDecryptedText);
		  return strDecryptedText;
	}
}

