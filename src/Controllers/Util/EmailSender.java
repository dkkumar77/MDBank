package Controllers.Util;


import Model.Databases.AdminDatabase;
import Model.Email;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.List;
import java.util.Properties;

public class EmailSender
{
	private Email email;
	private static List<String> adminInfo = AdminDatabase.returnAdminInfo("admin1");

	//JUST TESTING RN CAUSE THIS ARGUMENT WILL CONTAIN THE CUSTOMERS EMAIL STUFF
	public EmailSender(Email email)
	{
		this.email = email;
	}

	private void send()
	{
		Runnable emailTask = () -> {
			try {
				Properties properties = new Properties();
				properties.put("mail.smtp.auth", "true");
				properties.put("mail.smtp.starttls.enable", "true");
				properties.put("mail.smtp.host", "smtp.gmail.com");
				properties.put("mail.smtp.port", "587");

				Session session = Session.getInstance(properties,
						new javax.mail.Authenticator()
						{
							protected PasswordAuthentication getPasswordAuthentication()
							{
								return new PasswordAuthentication(adminInfo.get(0), adminInfo.get(1));
							}
						});
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(adminInfo.get(0)));
				message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(email.getEmailAddress()));
				message.setSubject(email.getSubject());
				message.setText(email.getContent());
				System.out.println("Sending message...");
				long start = System.currentTimeMillis();
				Transport.send(message);
				System.out.println(System.currentTimeMillis() - start);
				System.out.println("Message sent to : " + email.getEmailAddress());
			}catch (MessagingException | RuntimeException runMsgEx){
				runMsgEx.printStackTrace();
			}
		};
		Thread mailThread = new Thread(emailTask,"EMAIL_THREAD");
		mailThread.start();
	}

	public static void main(String[] args)
	{
		// TESTING ONLY ALL OF THE CODE BELOW WILL BE DELETED
		EmailSender emailSender = new EmailSender(new Email("dk.kumar77@yahoo.com","Big Dick Horny Fucker wanting your steamy wet cum","big fat juicy ding dong horse meat, stuffed in my tight little bootyhole nigga slut"));
		emailSender.send();
		System.out.println("Sending Email");

	}
}
