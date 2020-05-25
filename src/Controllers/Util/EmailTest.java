package Controllers.Util;


import Model.Definitions.Email;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmailTest
{
	private static Scanner scanner = new Scanner(System.in);

	public static void main(String[] args)
	{
		//choose who you wa
		// nt to send it to and change first arg on line 24
		String emailM = "mani.shah23@gmail.com";
		String emailD = "";

		System.out.print("Subject: ");
		String subject = scanner.nextLine();

		System.out.print("Content:");
		String content = scanner.nextLine();

		List<String> attachments = new ArrayList<>(2);
		//attachments.add("filepath");

		System.out.println("Creating email");
		Email email = new Email(emailM,subject,content,attachments);
		System.out.println("Sending email");
		EmailSender emailSender = new EmailSender(email);
		emailSender.send();
		System.out.println("Sent");
	}
}
