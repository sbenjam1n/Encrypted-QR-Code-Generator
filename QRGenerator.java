import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Hashtable;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QrGenerator 
{
	
	private String messageStamp = "currentStamp.png";
	private String sendersPublicKey = "currentStamp2.png";
	private String charset = "UTF-8";
	private static Cipher rsaCipher;
	
	
	/*
	 *Takes the cipher text, generate a QR code and return it as an ImageIcon.
	 */
	public  void makeQRMessageStamp(Key publicKey, String message, String recipient)
	{		 
	String qrCodeData = encryptWithPublicKey(publicKey, message);
	
	String charset = "UTF-8"; // or "ISO-8859-1"
	
	Hashtable hintMap = new Hashtable();
	hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
	try
	{
		createQRCode(qrCodeData, messageStamp, charset, hintMap, 242, 242);

	System.out.println("QR Code image created successfully!");

	//System.out.println("Data read from QR Code: "+ readQRCode(filePath, charset, hintMap, keys, charset));
	
	} catch (WriterException | IOException e) {
		System.out.println("Whoops! There was a problem creating the QR code.");
		e.printStackTrace();
	}

	}
	
	/**
	 * This generates a QRCode with the sender generators public key.
	 */
	public void makeQRPublicKeyStamp(Key publicKey)
	{
		byte[] array = publicKey.getEncoded();
		String qrCodeData = Base64.getEncoder().encodeToString(array);
		
		Hashtable hintMap = new Hashtable();
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		
		try
		{
			createQRCode(qrCodeData, sendersPublicKey, charset, hintMap, 174, 174);

		System.out.println("QR Code image created successfully!");

		//System.out.println("Data read from QR Code: "+ readQRCode(filePath, charset, hintMap, publicKey, charset));
		
		} catch (WriterException | IOException e) {
			System.out.println("Whoops! There was a problem creating the QR code.");
			e.printStackTrace();
		}

		
		ImageIcon qrStamp = new ImageIcon(sendersPublicKey);

		
	}
   
	/*
	 *Encrypt the message from the sender/generator for a given recipient.
	 */
   public  String encryptWithPublicKey(Key publicKey, String message)
   {
	   
	   byte[] textEncrypted = null;

		try 
		{
		byte[] text = message.getBytes();
		
		
		rsaCipher = Cipher.getInstance("RSA");
		rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);

		textEncrypted  = rsaCipher.doFinal(text);
		
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
			System.out.println("Whoa!! Encryption Error!");
			e.printStackTrace();}
		
		return textEncrypted.toString();
		
   }

  
	/*
	 * The next two methods for creating and reading QRcodes using the ZXing library can be found at: http://javapapers.com/core-java/java-qr-code/
	 * 
	 * The readQRCode method has been modified to decrypt the QR code result with the receiving private key. It isn't used here, but would be useful in the mobile reader app.
	 */
	public void createQRCode(String qrCodeData, String filePath, String charset, Hashtable hintMap, int qrCodeheight, int qrCodewidth) throws WriterException, IOException 
	{	
		//245 character limit
		File file = new File(filePath);
		file.delete();
		BitMatrix matrix = new MultiFormatWriter().encode(
				new String(qrCodeData.getBytes(charset), charset),
				BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight, hintMap);
		MatrixToImageWriter.writeToFile(matrix, filePath.substring(filePath
				.lastIndexOf('.') + 1), new File(filePath));
	}

	
	public String readQRCode(String filePath, String charset, Hashtable hintMap, Key publicKey, String receiver)throws FileNotFoundException, IOException, NotFoundException 
	{
		BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
				new BufferedImageLuminanceSource(
						ImageIO.read(new FileInputStream(filePath)))));
		Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap,
				hintMap);
		String message =  qrCodeResult.getText();
		
		   byte[] textEncrypted = null;

			try 
			{
			byte[] text = message.getBytes();

			rsaCipher = Cipher.getInstance("RSA");
			rsaCipher.init(Cipher.DECRYPT_MODE, publicKey);

			textEncrypted  = rsaCipher.doFinal(text);
			
			} catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
				System.out.println("Whoa!! Encryption Error!");
				e.printStackTrace();}
			
			return textEncrypted.toString();
	}
	

	

}
