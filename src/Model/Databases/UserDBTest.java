package Model.Databases;

import Model.Constants.TransactionType;
import Model.Transaction;

import java.util.Random;

public class UserDBTest
{
	public static void main(String[] args)
	{
		GeneralDatabase generalDatabase = new GeneralDatabase();
		UserDatabase userDatabase = new UserDatabase("boom123",generalDatabase);
//		userDatabase.getMonthlyStatement(5,2020);
		for(int i = 0; i < 10; i++) {
			int deposit = new Random().nextInt(10000);
			double currentBalance = generalDatabase.getCurrentBalance("boom123");
			System.out.println("Current Balance: $"+currentBalance);
			System.out.println("Depositing Amount: $"+deposit);
			userDatabase.logTransaction(new Transaction(deposit, TransactionType.DEPOSIT), (currentBalance + deposit));
			System.out.println("New Balance: $" + generalDatabase.getCurrentBalance("boom123")+"\n");
		}
	}
}
