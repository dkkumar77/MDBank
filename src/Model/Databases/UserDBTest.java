package Model.Databases;

import Model.Constants.TransactionType;
import Model.Transaction;

public class UserDBTest
{
	public static void main(String[] args)
	{
		GeneralDatabase generalDatabase = new GeneralDatabase();
		double currentBalance =  generalDatabase.getCurrentBalance("rosycheeks");
		UserDatabase userDatabase = new UserDatabase("rosycheeks",generalDatabase);
		userDatabase.logTransaction(new Transaction(100.15, TransactionType.DEPOSIT),(currentBalance+100.15));
	}
}
