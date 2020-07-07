package Model.Databases;

import Model.Objects.Date;
import Model.Objects.Transaction;
import Model.Constants.UserDbColumns;
import Model.Constants.TransactionType;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.HorizontalAlignment;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.io.FileNotFoundException;

import static Model.Constants.UserDbColumns.accountID;
import static Model.Constants.UserDbColumns.amount;
import static Model.Constants.UserDbColumns.balance;
import static Model.Constants.UserDbColumns.transactionID;
import static Model.Constants.UserDbColumns.transactionTime;
import static Model.Constants.UserDbColumns.transactionType;

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
		if(table != null){
			table = null;
		}
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

	public ScanResult getTable()
	{
		ScanRequest scanRequest = new ScanRequest()
				.withTableName(databaseName);
		return client.scan(scanRequest);
	}

	public void saveMonthlyStatementToPdf(File file, int month, int year) throws FileNotFoundException
	{
		ItemCollection<QueryOutcome> result = getMonthlyStatement(month,year);

		String date, type,amount,currBal,tID;

		PdfWriter writer = new PdfWriter(file.getAbsolutePath()); // Similar to file write
		PdfDocument pdfDocument = new PdfDocument(writer); // Creates the pdf template
		Document document = new Document(pdfDocument, PageSize.LETTER); // Defines the document that uses the empty template
		com.itextpdf.layout.element.Table table = new com.itextpdf.layout.element.Table(5); // Creates the table with 5 columns

		Cell cell; // individual cells of the table

		// All the headings
		document.add(new Paragraph("MD BANK").setTextAlignment(TextAlignment.CENTER).setBold());

		document.add(new Paragraph("Statement for " + Date.getMonth(month,year)+","+year)
				.setTextAlignment(TextAlignment.CENTER).setBold());

		document.add(new Paragraph("Transaction History For: " + generalDatabase.grabFullName(username))
				.setTextAlignment(TextAlignment.CENTER).setBold());

		document.add(new Paragraph("Current Balance: $" + generalDatabase.getCurrentBalance(username))
				.setTextAlignment(TextAlignment.CENTER).setBold());

		// Fills in the top row cells with the titles of the columns
		cell = new Cell().add(new Paragraph("Date")).setBold().setItalic();
		table.addCell(cell);
		cell = new Cell().add(new Paragraph("Type")).setBold().setItalic();
		table.addCell(cell);
		cell = new Cell().add(new Paragraph("Amount")).setBold().setItalic();
		table.addCell(cell);
		cell = new Cell().add(new Paragraph("New Balance")).setBold().setItalic();
		table.addCell(cell);
		cell = new Cell().add(new Paragraph("Transaction ID")).setBold().setItalic();
		table.addCell(cell);


		// For loop that goes through each transaction
		for(Item item : result){
			date = item.getString(transactionTime.name());
			cell = new Cell().add(new Paragraph(date)).setPadding(10).setHorizontalAlignment(HorizontalAlignment.CENTER); // adds the date to the cell
			table.addCell(cell);

			type = item.getString(transactionType.name());
			cell = new Cell().add(new Paragraph(type).setPadding(10)); // adds the type of transaction to cell
			table.addCell(cell);

			amount = item.getNumber(UserDbColumns.amount.name()).toString();
			cell = new Cell().add(new Paragraph("$"+amount)).setPadding(10); // adds the amount to cell
			table.addCell(cell);

			currBal = item.getNumber(balance.name()).toString();
			cell = new Cell().add(new Paragraph("$"+currBal)).setPadding(10); // adds the current balance to cell
			table.addCell(cell);

			tID = item.getNumber(transactionID.name()).toString();
			cell = new Cell().add(new Paragraph(tID).setPadding(10).setHorizontalAlignment(HorizontalAlignment.CENTER)); // adds ID to cell
			table.addCell(cell);

		}
		table.setHorizontalAlignment(HorizontalAlignment.CENTER); // Table is in the center of document
		document.add(table); // The table is added to the pdf document
		document.close(); // THe document is closed just like the way a file is closed
	}

	public ItemCollection<QueryOutcome> getMonthlyStatement(int monthNumber, int year)
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

		QuerySpec querySpec = new QuerySpec().withProjectionExpression("#accID,  "+transactionTime.name()+", " + amount.name() + ", "+balance.name()
				+ ", " + transactionType.name() +", " + transactionID.name())
				.withKeyConditionExpression("#accID = :acc and transactionTime between :beg and :end")
				.withNameMap(nameMap)
				.withValueMap(valueMap);

		ItemCollection<QueryOutcome> items;
		items = table.query(querySpec);
//      Uncomment this stuff to print out details
//		try {
//			items = table.query(querySpec);
//			for (Item item: items) {
//				System.out.println(item.toJSONPretty());
//			}
//		}
//		catch (Exception e) {
//			System.err.println(e.getMessage());
//		}
		return items;
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
			if(transaction.getType() == TransactionType.DEPOSIT_CHECKING) {
				generalDatabase.updateBalance(this.username, Double.toString(newBalance));
			}
			if(transaction.getType() == TransactionType.DEPOSIT_SAVING){
				generalDatabase.updateSavingsBalance(this.username,Double.toString(newBalance));
			}
			if(transaction.getType() == TransactionType.WITHDRAWAL){
				generalDatabase.updateBalance(this.username,Double.toString(newBalance));
			}
			if(transaction.getType() == TransactionType.WITHDRAWAL_SAVINGS){
				generalDatabase.updateSavingsBalance(this.username, Double.toString(newBalance));


			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	public boolean deleteTable()
	{
		loadTable();
		table.delete();
		try {
			table.waitForDelete();
			return true;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void saveStatementToPdf(File file) throws FileNotFoundException
	{
		String date, type, amount,  currBal, tID;

		ScanResult result = getTable();

		PdfWriter writer = new PdfWriter(file.getAbsolutePath());
		PdfDocument pdfDocument = new PdfDocument(writer);
		Document document = new Document(pdfDocument, PageSize.LETTER);
		com.itextpdf.layout.element.Table table = new com.itextpdf.layout.element.Table(5);

		Cell cell;

		document.add(new Paragraph("MD BANK").setTextAlignment(TextAlignment.CENTER).setBold());
		document.add(new Paragraph("Transaction History For: " + generalDatabase.grabFullName(username))
				.setTextAlignment(TextAlignment.CENTER).setBold());
		document.add(new Paragraph("Current Balance: $" + generalDatabase.getCurrentBalance(username))
				.setTextAlignment(TextAlignment.CENTER).setBold());

		cell = new Cell().add(new Paragraph("Date")).setBold().setItalic();
		table.addCell(cell);
		cell = new Cell().add(new Paragraph("Type")).setBold().setItalic();
		table.addCell(cell);
		cell = new Cell().add(new Paragraph("Amount")).setBold().setItalic();
		table.addCell(cell);
		cell = new Cell().add(new Paragraph("New Balance")).setBold().setItalic();
		table.addCell(cell);
		cell = new Cell().add(new Paragraph("Transaction ID")).setBold().setItalic();
		table.addCell(cell);

		for(Map<String, AttributeValue> resultSet : result.getItems()){
			date = resultSet.get(transactionTime.name()).getS();
			cell = new Cell().add(new Paragraph(date)).setPadding(10).setHorizontalAlignment(HorizontalAlignment.CENTER);
			table.addCell(cell);

			type = resultSet.get(transactionType.name()).getS();
			cell = new Cell().add(new Paragraph(type).setPadding(10));
			table.addCell(cell);

			amount = resultSet.get(UserDbColumns.amount.name()).getN();
			cell = new Cell().add(new Paragraph("$"+amount)).setPadding(10);
			table.addCell(cell);

			currBal = resultSet.get(balance.name()).getN();
			cell = new Cell().add(new Paragraph("$"+currBal)).setPadding(10);
			table.addCell(cell);

			tID = resultSet.get(transactionID.name()).getN();
			cell = new Cell().add(new Paragraph(tID).setPadding(10).setHorizontalAlignment(HorizontalAlignment.CENTER));
			table.addCell(cell);

		}
		table.setHorizontalAlignment(HorizontalAlignment.CENTER);
		document.add(table);
		document.close();
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
		Transaction transaction = new Transaction(0,TransactionType.DEPOSIT_CHECKING);
		this.logTransaction(transaction,0.00);
	}
}
