import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Recipients implements Serializable {

	protected  static  HashSet<String> recipientIdsList = new HashSet<String>();
	protected static Map<String, KeyPair> recipientIdKeyPairs = new HashMap<String, KeyPair>();
	
	protected  HashSet<String> recipientIdsListSerial = new HashSet<String>();
	protected  Map<String, KeyPair> recipientIdKeyPairsSerial = new HashMap<String, KeyPair>();
	
	public void print(){System.out.println(recipientIdKeyPairs.get("sfs"));}
	
	//public void visible(){System.out.println("In scope.");}
	
	/*
	 * Generates a public and private encryption key for every added user and stores them in the HashMap. 
	 * The users/recipients are added to an ArrayList to use with the JComboBoxes.
	 */
	public void createRecipients(String newName)
	{
		
		try
		{
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");	
		    kpg.initialize(2048);
			kp = kpg.genKeyPair();
			setKp(kp);


		}catch (NoSuchAlgorithmException noSuchAlgo)
		{
		System.out.println(" No Such Algorithm exists " + noSuchAlgo);
		}
	

        recipientIdsList.add(newName);
		//recipientIdsListSerial.addAll(recipientIdsList);
        
       
	    recipientIdKeyPairs.put(newName, kp);
	  //  recipientIdKeyPairsSerial.putAll(recipientIdKeyPairs);
	    
	    
	try {
			recipientIdsListSerial.addAll(recipientIdsList);
		    recipientIdKeyPairsSerial.putAll(recipientIdKeyPairs);
			
			System.out.println(recipientIdsListSerial.toString());
			System.out.println(recipientIdsListSerial.toString());	
			
			FileOutputStream f_out = new FileOutputStream("savedRecipientsPairs.ser");
    		FileOutputStream f_out2 = new FileOutputStream("savedRecipients.ser");
    	
    		ObjectOutputStream obj_out = new ObjectOutputStream (f_out);
    		ObjectOutputStream obj_out2 = new ObjectOutputStream (f_out2);
    			
    		obj_out.writeObject ( recipientIdKeyPairsSerial );
    			obj_out2.writeObject ( recipientIdsListSerial );
    			
    		obj_out.close();
    		f_out.close();
    		
   			obj_out2.close();
   			f_out2.close();

    	}catch(IOException i)
    	{
    	 i.printStackTrace();
        }
    	finally{System.out.println("Serialized file!");

    	}
	
		//printKeyPair(newName);
		

		
	}

	
	public KeyPair getrecipientIdKp(String rId)
	{
		return recipientIdKeyPairs.get(rId);
	}
	
	
	private KeyPair kp;

	
	public KeyPair getKp() 
	{
		return kp;
	}

	public void setKp(KeyPair kp) 
	{
		this.kp = kp;
	}


	public void printKeyPair(String rId) 
	{
		
		
		/*
		 * This code allows you to write out keys to a file.
		 * 
		 * 	    FileOutputStream fosPublicKey = null;
		try {
			fosPublicKey = new FileOutputStream(rId.toString()+"Public.DER");
		
	    byte[] publicKeyBytes = kp.getPublic().getEncoded();
	    fosPublicKey.write(publicKeyBytes);
	    fosPublicKey.close();
	    
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		try {
			fosPublicKey = new FileOutputStream(rId.toString()+"Private.DER");
		
	    byte[] privateKeyBytes = kp.getPrivate().getEncoded();
	    fosPublicKey.write(privateKeyBytes);
	    fosPublicKey.close();
	    
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 */
		
	}


}
