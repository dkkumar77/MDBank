package Model.Databases;

import Model.Constants.TransactionType;
import Model.Transaction;

import java.util.Random;

public class UserDBTest
{
	public static void main(String[] args)
	{
		GeneralDatabase generalDatabase = new GeneralDatabase();
		UserDatabase userDatabase = new UserDatabase("user",generalDatabase);
		userDatabase.getMonthlyStatement(5,2020);
	//	userDatabase.getTable();

		// Uncomment the bottom to deposit some mullah

//		for(int i = 0; i < 10; i++) {
//			int deposit = new Random().nextInt(10000);
//			double currentBalance = generalDatabase.getCurrentBalance("user");
//			System.out.println("Current Balance: $"+currentBalance);
//			System.out.println("Depositing Amount: $"+deposit);
//			userDatabase.logTransaction(new Transaction(deposit, TransactionType.DEPOSIT), (currentBalance + deposit));
//			System.out.println("New Balance: $" + generalDatabase.getCurrentBalance("user")+"\n");
//			if(i == 9){
//				System.out.println("Sleeping for 10 seconds");
//				try {
//					Thread.sleep(10000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				System.out.println("Done sleeping, deposiing again");
//				i = 0;
//			}
//		}
	}
}
