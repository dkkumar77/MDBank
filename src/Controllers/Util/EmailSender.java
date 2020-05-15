package Controllers.Util;


import Model.Databases.AdminDatabase;
import Model.Email;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class EmailSender
{
	private Email email;
	private static List<String> adminInfo = AdminDatabase.returnAdminInfo("admin1");

	public EmailSender(Email email)
	{
		this.email = email;
	}

	public boolean send()
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

				MimeBodyPart messageBodyPart = new MimeBodyPart();
				messageBodyPart.setText(email.getContent());

				Multipart multipart = new MimeMultipart();
				multipart.addBodyPart(messageBodyPart);

				if (email.getAttachments() != null) {
					for (int i = 0; i < email.getNumOfAttachments(); i++) {
						MimeBodyPart attachPart = new MimeBodyPart();
						try {
							attachPart.attachFile(email.getAttachments().get(i));
						} catch (IOException ex) {
							ex.printStackTrace();
						}
						multipart.addBodyPart(attachPart);
					}
				}
				message.setContent(multipart);
				Transport.send(message);
				System.out.println("Email Sent");
			}catch (MessagingException | RuntimeException runMsgEx){
				runMsgEx.printStackTrace();
			}
		};
		Thread mailThread = new Thread(emailTask,"EMAIL_THREAD");
		mailThread.start();
		return true;
	}
}
