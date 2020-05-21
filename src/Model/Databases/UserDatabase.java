package Model.Databases;

import Model.Constants.TransactionType;
import Model.Constants.UserDbColumns;
import Model.Date;
import Model.Transaction;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;

import java.util.*;

import static Model.Constants.UserDbColumns.*;

public class UserDatabase
{
	private final static DynamoDB dynamoDB;
	private final static AmazonDynamoDB client;
	private final String username;
	private static Table table;
	private static String databaseName;

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

	private void loadTable()
	{
		if(table == null){
			table = dynamoDB.getTable(databaseName);
		}
	}
	// We can generalize the for loop part by adding code that will traverse the table
	// in order to make the transaction table later on. Right now we can't do anything about this.
	// In mean time just grab each other by the foreskin.
	public void getTable()
	{
		ScanRequest scanRequest = new ScanRequest()
				.withTableName(databaseName);
		ScanResult result = client.scan(scanRequest);
		System.out.println(result.toString());
	}

	public void getMonthlyStatement(int monthNumber, int year)
	{
		String startDate = Date.getFirstDayOfMonth(monthNumber,year)+" "+Date.START_TIME;;
		String endDate = Date.getLastDayOfMonth(monthNumber,year)+" "+Date.END_TIME;

		loadTable();

		HashMap<String, String> nameMap = new HashMap<>();
		nameMap.put("#accID", accountID.name());

		HashMap<String, Object> valueMap = new HashMap<>();
		valueMap.put(":acc", generalDatabase.getAccountID(username));

		valueMap.put(":beg", startDate);
		valueMap.put(":end", endDate);

		QuerySpec querySpec = new QuerySpec().withProjectionExpression("#accID,  " + amount.name() + ", "+balance.name()
				+ ", " + transactionType.name() +", " + transactionID.name())
				.withKeyConditionExpression("#accID = :acc and transactionTime between :beg and :end")
				.withNameMap(nameMap)
				.withValueMap(valueMap);

		ItemCollection<QueryOutcome> items;
		Iterator<Item> iterator;

		try {
			items = table.query(querySpec);
			for (Item item: items) {
				System.out.println(item.toJSONPretty());
			}
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
		}
//		ScanSpec scanSpec = new ScanSpec().withProjectionExpression("#tt, " + amount.name() + ", " + balance.name()
//				+ ", " + transactionType.name() +", " + transactionID.name())
//				.withFilterExpression("#tt between :start_time and :end_time").withNameMap(new NameMap()
//						.with("#tt", "transactionTime"))
//				.withValueMap(new ValueMap().withString(":start_time", startDate)
//						.withString(":end_time", endDate));
//		try {
//			ItemCollection<ScanOutcome> items = table.scan(scanSpec);
//			for (Item item : items) {
//				System.out.println(item.toJSONPretty());
//			}
//		}
//		catch (Exception e) {
//			System.err.println("Unable to scan the table:");
//			System.err.println(e.getMessage());
//		}
	}

	public synchronized void logTransaction(Transaction transaction, double newBalance)
	{
		int lastTransID = generalDatabase.getLastTransactionID(username);

		loadTable();

		try {
			PutItemOutcome outcome = table
					.putItem(new Item().withPrimaryKey(UserDbColumns.accountID.name(),generalDatabase.getAccountID(this.username))
					.withString(transactionTime.name(),transaction.getTransactionTime())
					.withString(transactionType.name(),transaction.getType().name())
					.withNumber(amount.name(),transaction.getAmount())
					.withNumber(balance.name(),newBalance)
					.withNumber(transactionID.name(),(lastTransID+1)));
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
					Arrays.asList(new KeySchemaElement("accountID", KeyType.HASH), // Partition key
							new KeySchemaElement(transactionTime.name(), KeyType.RANGE)), // Sort key
					Arrays.asList(new AttributeDefinition(accountID.name(), ScalarAttributeType.N),
							new AttributeDefinition(transactionTime.name(), ScalarAttributeType.S)),
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
