package Controllers.Util;


import Model.Email;

import java.util.Scanner;

public class EmailTest
{
	private static Scanner scanner = new Scanner(System.in);

	public static void main(String[] args)
	{
		//choose who you want to send it to and change first arg on line 24
		String emailM = "mani.shah23@gmail.com";
		String emailD = "dk.kumar77@yahoo.com";

		System.out.print("Subject: ");
		String subject = scanner.nextLine();

		System.out.print("Content:");
		String content = scanner.nextLine();

		Email email = new Email(emailD,subject,content);
		EmailSender emailSender = new EmailSender(email);

		emailSender.send();
	}
}
