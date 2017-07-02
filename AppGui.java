/*Steven Benjamin
 * CSIS 1410
 * 
 * This program creates allows you to create many RSA key pairs(representing senders and recievers) that are used to generate two QR codes, one containing an encrypted message of 245 characters or less and
 * another that displays the message senders public key. 
 * 
 * To actually be useful, this would need a mobile reader app and a way to distribute the keys to the parties involved.
 */
import java.awt.EventQueue;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.security.KeyPair;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import javax.swing.JTree;
import javax.swing.AbstractAction;
import javax.swing.Action;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Component;
import javax.swing.SwingConstants;



public class AppGui{

	protected static int count = 0;
	private JFrame frame;
	private JTextArea messageField = new JTextArea();
	private JTextField bannerField = new JTextField();
	private JTextField newUserField = new JTextField();
	
	final Recipients recipients = new Recipients();
	
	final JComboBox sendingToBox = new JComboBox();
	final JComboBox sendingFromBox = new JComboBox();
	
	final JLabel bannerLabel = new JLabel();
	
	final JPanel inputPanel = new JPanel();
	
	final QrGenerator qrG = new QrGenerator();
	
	final ImageIcon set = scaledIcon(new ImageIcon("speak.png"));
	       
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) 
	{
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AppGui window = new AppGui();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}
	

	private Collection<? extends String> deserializeRecipients() {
		 try
         {
	   
            FileInputStream fileIn2 =new FileInputStream("savedRecipients.ser");
            

            ObjectInputStream in2 = new ObjectInputStream(fileIn2);

            HashSet hs = (HashSet) in2.readObject();
            
            in2.close();
            fileIn2.close();
		     System.out.println(hs.toArray().toString());
		   	 return hs;
         }catch(IOException | ClassNotFoundException i)
         {
            i.printStackTrace();
             return null;
         }
	}
	
	private Map<? extends String, ? extends KeyPair> deserializeRecipientsKeys() {
		 try
	        {
		   
	           FileInputStream fileIn =new FileInputStream("savedRecipientsPairs.ser");

	           ObjectInputStream in = new ObjectInputStream(fileIn);

	           HashMap hs = (HashMap) in.readObject();
	           
	           in.close();
	           fileIn.close();

			   return hs;
	        }catch(IOException | ClassNotFoundException i)
	        {
	           i.printStackTrace();
	           return null;
	        }
	}
	
	public ImageIcon scaledIcon(ImageIcon move)
	{
		Image iconAsImage = move.getImage().getScaledInstance( 232,232,0 );  
		ImageIcon thisIcon = new ImageIcon(iconAsImage);
		return thisIcon;
	}



	/**
	 * Create the application.
	 */
	public AppGui()
	{
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	 void initialize() 
	 {
		 /*
		  * Deserialized the user list and key pair map.
		  */
		 try {
			recipients.recipientIdsList.addAll(deserializeRecipients());
			 recipients.recipientIdKeyPairs.putAll(deserializeRecipientsKeys());
		} catch (Exception e2) {

			e2.printStackTrace();
		}
		 
		 
		 for(String a : recipients.recipientIdsList)
		 { 
			 sendingToBox.addItem(a);
			 sendingFromBox.addItem(a);
		 }
		 
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setBounds(100, 100, 1000, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel outputPanel = new JPanel();
		outputPanel.setBackground(Color.BLACK);
		outputPanel.setBounds(527, 0, 455, 455);
		frame.getContentPane().add(outputPanel);
		outputPanel.setLayout(null);
		bannerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		bannerLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
		bannerLabel.setText("QR Generator");
		bannerLabel.setFont(new Font("Niagara Engraved", Font.PLAIN, 64));
		bannerLabel.setForeground(Color.WHITE);
		

		bannerLabel.setBounds(12, 297, 242, 113);
		outputPanel.add(bannerLabel);
		
		JLabel messageImage = new JLabel();
		messageImage.setLabelFor(outputPanel);

		messageImage.setBounds(12, 13, 242, 242);
		outputPanel.add(messageImage);
		
		JLabel sendersImage = new JLabel("");
		sendersImage.setLabelFor(outputPanel);

		sendersImage.setBounds(269, 268, 174, 174);
		outputPanel.add(sendersImage);
		inputPanel.setBackground(Color.BLACK);
		
		inputPanel.setBounds(0, 0, 455, 455);
		frame.getContentPane().add(inputPanel);
		inputPanel.setLayout(null);
		
        messageImage.setIcon(new ImageIcon("blank.png"));
		sendersImage.setIcon(new ImageIcon("blank.png"));
		
		JLabel panel = new JLabel();
		panel.setBackground(Color.BLACK);
		panel.setBounds(258, 13, 185, 242);
		outputPanel.add(panel);
		/*
		 * JComboBoxes listing recipients.
		 */
		sendingToBox.setForeground(Color.WHITE);
		sendingToBox.setBackground(Color.BLACK);

		sendingToBox.setBounds(156, 13, 112, 22);
		//sendingToBox.setModel(new DefaultComboBoxModel(recipients.recipientIdsList.toArray()));
		inputPanel.add(sendingToBox);
		sendingFromBox.setBackground(Color.BLACK);
		sendingFromBox.setForeground(Color.WHITE);
		

		sendingFromBox.setBounds(156, 286, 112, 22);
		//sendingFromBox.setModel(new DefaultComboBoxModel(recipients.recipientIdsList.toArray()));
		inputPanel.add(sendingFromBox);
		
		/*
		 * The message that is encrypted with the public key of the chosen recipient.
		 */
		messageField = new JTextArea();
		messageField.setToolTipText("Secret message limited to 245 characters.");
		messageField.setBounds(64, 45, 242, 235);
		inputPanel.add(messageField);
		messageField.setColumns(10);
		messageField.setLineWrap(true);
		messageField.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (messageField.getText().length() >= 245 ) // limit textfield to 245 characters
		            e.consume(); 
		    }  
		});
		
		JLabel bannerlabel = new JLabel("Banner");
		bannerlabel.setFont(new Font("SimSun-ExtB", Font.PLAIN, 13));
		bannerlabel.setForeground(Color.WHITE);
		bannerlabel.setBounds(12, 374, 104, 22);
		inputPanel.add(bannerlabel);

		
		JLabel messageLabel = new JLabel("Recipient:");
		messageLabel.setFont(new Font("SimSun", Font.PLAIN, 13));
		messageLabel.setForeground(Color.WHITE);
		messageLabel.setBounds(69, 10, 75, 28);
		inputPanel.add(messageLabel);
	
		
		JLabel lblSendingAs = new JLabel("   Create as:");
		lblSendingAs.setFont(new Font("SimSun", Font.PLAIN, 13));
		lblSendingAs.setForeground(Color.WHITE);
		lblSendingAs.setBounds(53, 283, 104, 28);
		inputPanel.add(lblSendingAs);
		
		JLabel label = new JLabel("Message");
		label.setFont(new Font("SimSun", Font.PLAIN, 13));
		label.setForeground(Color.WHITE);
		label.setBounds(12, 139, 104, 28);
		inputPanel.add(label);
		
		bannerField = new JTextField();
		bannerField.setToolTipText("Public banner limited to 15 characters.");
		bannerField.setHorizontalAlignment(SwingConstants.CENTER);
		bannerField.setBounds(64, 324, 242, 118);
		inputPanel.add(bannerField);
		bannerField.setColumns(10);
		bannerField.addKeyListener(new KeyAdapter() {
		    public void keyTyped(KeyEvent e) { 
		        if (bannerField.getText().length() >= 15 ) // limit textfield to 15 characters
		            e.consume(); 
		    }  
		});
		
		
		/*
		 * A plain text message that appears on the final image with the message and senders public key.
		 */
		
		newUserField = new JTextField();
		newUserField.setBounds(318, 142, 120, 22);
		inputPanel.add(newUserField);
		newUserField.setColumns(10);
		
		JButton AddUserButton = new JButton("Add Recipient");
		AddUserButton.setToolTipText("Generates a new keypair.");
		AddUserButton.setFont(new Font("Tw Cen MT", Font.PLAIN, 13));
		AddUserButton.setBackground(new Color(240, 240, 240));
		AddUserButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				
				/*
				 * Adds a new message recipient name and generates a corresponding keypair.
				 */

				recipients.createRecipients(newUserField.getText().toString());
				sendingToBox.addItem(newUserField.getText().toString());// = new JComboBox(recipients.recipientIdsListSerial.toArray());
				sendingFromBox.addItem(newUserField.getText().toString()); //new JComboBox(recipients.recipientIdsListSerial.toArray());
				newUserField.setText(null);
				sendingFromBox.setVisible(true);
				sendingToBox.setVisible(true);	
				frame.getContentPane().add(inputPanel);
				System.out.println(sendingToBox.getItemCount());
				System.out.println(sendingFromBox.getItemCount());
			}
		});


		AddUserButton.setBounds(318, 180, 120, 25);
		inputPanel.add(AddUserButton);
		
		JButton updateButton = new JButton("Set");
		updateButton.setToolTipText(" Recipient and Sender need to be set before creating. ");
		updateButton.setFont(new Font("Tw Cen MT", Font.PLAIN, 13));
		updateButton.setBackground(Color.WHITE);
		updateButton.setBounds(318, 350, 120, 28);
		inputPanel.add(updateButton); 
		
		updateButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				count++;
				/*
				 * Generate the QR codes and add them to the label.
				 */
					
					if ((sendingToBox.getSelectedItem().toString()!= null)&&(sendingFromBox.getSelectedItem()!= null))
					{	
						panel.setIcon(set);
						
						String sendingTo = sendingToBox.getSelectedItem().toString();
						//System.out.println(sendingTo);
						
						Key key = recipients.getrecipientIdKp(sendingTo).getPublic();
						qrG.makeQRMessageStamp(recipients.getrecipientIdKp(sendingTo).getPublic(),
								messageField.getText(), sendingToBox.getSelectedItem().toString());
						
						String sendingFrom = sendingFromBox.getSelectedItem().toString();
						
						qrG.makeQRPublicKeyStamp(recipients.getrecipientIdKp(sendingFrom).getPublic());
						bannerLabel.setText(bannerField.getText());
						
						File messagePng = new File("currentStamp.png");
						File sendersKeyPng = new File("currentStamp2.png");
						try {
							BufferedImage message = ImageIO.read(messagePng);
							BufferedImage sendersKey = ImageIO.read(sendersKeyPng);
							messageImage.setIcon(new ImageIcon(message));
							sendersImage.setIcon(new ImageIcon(sendersKey));
						} catch (IOException e1) {
							e1.printStackTrace();
						} 
					}
			}
		});

		JButton sendButton = new JButton("Create");
		sendButton.setToolTipText(" Creates message image in the project 'Messages' folder.\r\n");
		sendButton.setFont(new Font("Tw Cen MT", Font.PLAIN, 13));
		sendButton.setBackground(Color.WHITE);
		sendButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				/*
				 * When this button is selected, the outputPanel is output as a JPEG to the messages folder.
				 */
				if (count>=1) {
					try {
							String sendTo = sendingToBox.getSelectedItem().toString();
							String sendAs = sendingFromBox.getSelectedItem().toString();
						
	
							BufferedImage img = new BufferedImage(outputPanel.getWidth(), outputPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
						    outputPanel.print(img.getGraphics()); // or panel.printAll(...);
						    
						    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm.SSS");
						    Date date = new Date();
						    File newMessageJpg = new File("Messages/"+sendTo+"_from_"+sendAs+"_"+dateFormat.format(date).toString()+".jpg");
						    
						    try 
						    {
						        ImageIO.write(img, "jpg", newMessageJpg);
						    }
						    catch (IOException e1) {

						        e1.printStackTrace();
						}
						    
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				JOptionPane.showMessageDialog(frame,
					    "Created in Messages folder.");
				
			
		}});
		
		sendButton.setBounds(318, 391, 120, 40);
		inputPanel.add(sendButton);
	}
}
