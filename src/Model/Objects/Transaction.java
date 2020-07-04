package Model.Objects;

import Model.Constants.TransactionType;

/**
 * THIS CLASS SHOULD DEFINE A TRANSACTION
 */
public class Transaction
{
	private double amount;
	private TransactionType transactionType;

	public Transaction(double amount, TransactionType transactionType)
	{
		this.amount = amount;
		this.transactionType = transactionType;
	}

	public double getAmount()
	{
		return amount;
	}

	public TransactionType getType()
	{
		return transactionType;
	}

	public String getTransactionTime()
	{
		return Date.getSQLTime().toString();
	}
}
