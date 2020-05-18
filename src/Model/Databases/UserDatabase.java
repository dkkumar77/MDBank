package Model.Databases;

import Model.Constants.GeneralDbColumns;
import Model.Constants.TransactionType;
import Model.Constants.UserDbColumns;
import Model.Date;
import Model.Transaction;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.model.*;
import com.amazonaws.services.mq.model.User;

import java.util.Arrays;

public class UserDatabase
{
	private static DynamoDB dynamoDB;
	private static AmazonDynamoDB client;
	private static Table table;
	private static String databaseName;
	private String username;

	private static GeneralDatabase generalDatabase;

	static{
		client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
		dynamoDB = new DynamoDB(client);
	}

	public UserDatabase(String username, GeneralDatabase genDatabase)
	{
		this.username = username;
		databaseName = username.toUpperCase()+"_TABLE";
		generalDatabase = genDatabase;
	}

	public void createUserDb()
	{
		createTable();

	}

	public void logTransaction(Transaction transaction, double newBalance)
	{
		if(table == null){
			table = dynamoDB.getTable(databaseName);
		}
		int lastTransID = generalDatabase.getLastTransactionID(username);
		try {
			PutItemOutcome outcome = table
					.putItem(new Item().withPrimaryKey(UserDbColumns.transactionID.name(), (lastTransID+1))
					.withString(UserDbColumns.monthYear.name(), transaction.getTransactionMonthYr())
					.withString(UserDbColumns.dateTime.name(),transaction.getTransactionDateTime())
					.withString(UserDbColumns.transactionType.name(),transaction.getType().name())
					.withNumber(UserDbColumns.amount.name(),transaction.getAmount())
					.withNumber(UserDbColumns.balance.name(),newBalance));
			outcome.getPutItemResult();
			generalDatabase.updateTransactionID(this.username,(lastTransID+1));
			generalDatabase.updateBalance(this.username,Double.toString(newBalance));
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	private void createTable()
	{
		try {
			System.out.println("Attempting to create table; please wait...");
			table = dynamoDB.createTable(databaseName,
					Arrays.asList(new KeySchemaElement("transactionID", KeyType.HASH)),
					Arrays.asList(new AttributeDefinition("transactionID", ScalarAttributeType.N)),
					new ProvisionedThroughput(5L, 5L));
			table.waitForActive();
			System.out.println("Success.  Table status: " + table.getDescription().getTableStatus());
		}
		catch (Exception e) {
			System.err.println("Unable to create table: ");
			System.err.println(e.getMessage());
		}
		Transaction transaction = new Transaction(0,TransactionType.DEPOSIT);
		this.logTransaction(transaction,0.00);
	}
}
