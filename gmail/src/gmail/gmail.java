package gmail;

import java.io.File;
import java.io.FileWriter;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Properties;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
public class gmail {
	
	public static byte[] Encrypt(SecretKey secretKey, byte[] iv, String msg) throws Exception{
		  Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING"); 
		  cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));    
		  System.out.println("AES_CBC_PKCS5PADDING IV:"+cipher.getIV());
		  System.out.println("AES_CBC_PKCS5PADDING Algoritm:"+cipher.getAlgorithm());
		  byte[] byteCipherText = cipher.doFinal(msg.getBytes("UTF-8"));
		  System.out.println("加密結果的Base64編碼：" + Base64.getEncoder().encodeToString(byteCipherText));

		  return byteCipherText;
		 }
		 
		 public static void Decrypt(SecretKey secretKey, byte[] cipherText, byte[] iv) throws Exception{
		  Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING"); 
		  cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));    
		  byte[] decryptedText = cipher.doFinal(cipherText);
		  String strDecryptedText = new String(decryptedText);
		  System.out.println("解密結果：" + strDecryptedText);
		 }
	
	//final static String username = "raccoon831012@gmail.com";
	//final static String password = "xu3m06cj/6";
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Properties prop = new Properties();
		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
		keyGen.init(128,new SecureRandom( ) );
		SecretKey secretKey = keyGen.generateKey();
		byte[] iv = new byte[128 / 8]; 
		SecureRandom prng = new SecureRandom();
		prng.nextBytes(iv);
		
		byte[] cipher = Encrypt(secretKey, iv, "xu3m06cj/6");
		byte[] buffer = new byte[cipher.length];
		File auto = new File("auto.txt");
		FileWriter writer = new FileWriter(auto, false);
		Scanner scan = new Scanner(auto);
		
		char[] hex = Hex.encodeHex(cipher);
		writer.write(String.valueOf(Hex.encodeHex(secretKey.getEncoded()))+"\r\n");
		writer.write(String.valueOf(Hex.encodeHex(iv))+"\r\n");
		writer.write(String.valueOf(Hex.encodeHex(cipher))+"\r\n");
		writer.flush();
		//boolean is = scan.nextBoolean();
		//String mail = scan.next();
		String keydata = scan.next();
		String ivdata = scan.next();
		String data = scan.next();
		
		byte[] keycode = Hex.decodeHex(keydata.toCharArray());
		byte[] ivcode = Hex.decodeHex(ivdata.toCharArray());
		byte[] encoded = Hex.decodeHex(data.toCharArray());
		
		SecretKey key = new SecretKeySpec(keycode, "AES");
		System.out.println(Hex.encodeHex(keycode));
		System.out.println(Hex.encodeHex(secretKey.getEncoded()));
		Decrypt(key, encoded, ivcode);
		  
	}

}
