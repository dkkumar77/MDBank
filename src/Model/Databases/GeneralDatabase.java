package Model.Databases;

/**
 * MDBank
 *
 * @author Deepak Kumar
 * Date: 5/11/20
 * Time: 11:46 AM
 * <p>
 * Brief Description:
 */
import Model.Constants.DatabaseType;

import Model.Definitions.Customer;
import com.amazonaws.regions.Regions;
import Model.Constants.GeneralDbColumns;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;

import java.util.Map;
import java.util.Random;


public class GeneralDatabase {
    private final static String DATABASE_TABLE = DatabaseType.BANK_SERVER_INFO.name();
    private static DynamoDB dynamoDB;
    private static AmazonDynamoDB client;
    private static Table table;

    /**
     * Creates a connection with the database and gets the table.
     * USERNAME IS THE PRIMARY KEY AND IS USED TO ACCESS DATA ABOUT A PARTICULAR USER
     */
    public GeneralDatabase() {
        client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
        dynamoDB = new DynamoDB(client);
        table = dynamoDB.getTable(DATABASE_TABLE);
    }


    public boolean verifyCredentials(String username, String password) {
        GetItemSpec spec = new GetItemSpec().withPrimaryKey(GeneralDbColumns.username.name(), username);
        try {
            Item outcome = table.getItem(spec);
            String user = outcome.getString(GeneralDbColumns.username.name());
            String pass = outcome.getString(GeneralDbColumns.hashedPassword.name());
            if (user.equalsIgnoreCase(username) && password.equalsIgnoreCase(pass)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //***************
     /*
     JUSTIFICATION OF CHANGES:
     WE CREATE CUSTOMER OBJECT WHICH DEFINES THE CHARACTERISTICS OF CUSTOMER LIKE EMAIL, NAME ETC.
     WE CAN JUST PASS THAT IN AND USE THAT INSTEAD OF MAKING 50 DIFF PARAMETERS FOR ONE METHOD

     I REMOVED ACCOUNT BALANCE AND LOGIN ATTEMPTS BECAUSE WHEN YOU ARE FIRST CREATING AN ACCOUNT BY DEFAULT YOU'LL
     HAVE $0 AND YOUR LOGIN ATTEMPTS WOULD BE 0.
      */
    public void addUser(Customer customer, String password, String dateCreated) {
        try {
            PutItemOutcome outcome = table
                    .putItem(new Item().withPrimaryKey(GeneralDbColumns.username.name(), customer.getUsername())
                            .withString(GeneralDbColumns.firstName.name(), customer.getFirstName())
                            .withString(GeneralDbColumns.lastName.name(), customer.getLastName())
                            .withString(GeneralDbColumns.hashedPassword.name(), password)
                            .withString(GeneralDbColumns.primaryEmail.name(), customer.getEmail())
                            .withString(GeneralDbColumns.dob.name(), customer.getDateOfBirth())
                            .withString(GeneralDbColumns.dateCreated.name(), dateCreated)
                            .withLong(GeneralDbColumns.accountID.name(), customer.getCustomerID())
                            .withInt(GeneralDbColumns.loginAttempts.name(), 0)
                            .withString(GeneralDbColumns.currentBalance.name(), "0.00")
                            .withBoolean(GeneralDbColumns.isLoggedOn.name(), false)
                            .withInt("lastTransactionID", 0));
            PutItemResult result = outcome.getPutItemResult();
            if (result != null) {
                new UserDatabase(customer.getUsername(), this).createUserDb();
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

    public int getLastTransactionID(String username) {
        GetItemSpec spec = new GetItemSpec().withPrimaryKey(GeneralDbColumns.username.name(), username);
        Item outcome = table.getItem(spec);
        return outcome.getInt("lastTransactionID");
    }

    // We can generalize the for loop part by adding code that will traverse the table
    // in order to make the transaction table later on. Right now we can't do anything about this.
    // In mean time just grab each other by the foreskin.
    public static void getTable() {
        ScanRequest scanRequest = new ScanRequest()
                .withTableName(DatabaseType.BANK_SERVER_INFO.name());

        ScanResult result = client.scan(scanRequest);
        for (Map<String, AttributeValue> item : result.getItems()) {
            System.out.println(item.get(GeneralDbColumns.primaryEmail.name()).getS());
        }
    }


    public long createUniqueAccountNumber() {
        char[] sequenceWriter = new char[12];
        Random random = new Random();
        sequenceWriter[0] = (char) (random.nextInt(9) + '1');
        for (int i = 1; i < sequenceWriter.length; i++) {
            sequenceWriter[i] = (char) (random.nextInt(10) + '0');
        }
        return Long.parseLong(new String(sequenceWriter));

    }

    public double getCurrentBalance(String username) {
        GetItemSpec spec = new GetItemSpec().withPrimaryKey(GeneralDbColumns.username.name(), username);
        Item outcome = table.getItem(spec);
        return Double.parseDouble(outcome.getString(GeneralDbColumns.currentBalance.name()));
    }

    public void updateLoggedInQuery(String username) {
        GetItemSpec spec = new GetItemSpec().withPrimaryKey(GeneralDbColumns.username.name(), username);
        Item outcome = table.getItem(spec);
        boolean loggedInQuery = outcome.getBoolean(GeneralDbColumns.isLoggedOn.name());
        UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey(GeneralDbColumns.username.name(), username)
                .withUpdateExpression("set isLoggedIn = :l")
                .withValueMap(new ValueMap().withBoolean(":l", !loggedInQuery))
                .withReturnValues(ReturnValue.UPDATED_NEW);
        table.updateItem(updateItemSpec);

    }

    public boolean avoidDuplicate(String username) {
        GetItemSpec spec = new GetItemSpec().withPrimaryKey(GeneralDbColumns.username.name(), username);
        Item outcome = table.getItem(spec);
        if (outcome == null) {
            return true;
        }
        return false;
    }


    @Deprecated
    @SuppressWarnings("unused")
    public void quickUpdateLoginLogoffQuery(String username) {
        UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey(GeneralDbColumns.username.name(), username)
                .withUpdateExpression("set isLoggedIn = :l")
                .withValueMap(new ValueMap().withBoolean(":l", false))
                .withReturnValues(ReturnValue.UPDATED_NEW);
        table.updateItem(updateItemSpec);

    }

    public void updateEmailQuery(String username, String newEmail) {
        UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey(GeneralDbColumns.username.name(), username)
                .withUpdateExpression("set primaryEmail = :l")
                .withValueMap(new ValueMap().withString(":l", newEmail))
                .withReturnValues(ReturnValue.UPDATED_NEW);
        table.updateItem(updateItemSpec);
    }


    public void updatePasswordQuery(String username, String newPass) {
        UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey(GeneralDbColumns.username.name(), username)
                .withUpdateExpression("set hashedPassword = :l")
                .withValueMap(new ValueMap().withString(":l", newPass))
                .withReturnValues(ReturnValue.UPDATED_NEW);
        table.updateItem(updateItemSpec);
    }

    public void updateBalance(String username, String amount) {
        UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey(GeneralDbColumns.username.name(), username)
                .withUpdateExpression("set currentBalance = :l")
                .withValueMap(new ValueMap().withString(":l", amount))
                .withReturnValues(ReturnValue.UPDATED_NEW);
        table.updateItem(updateItemSpec);
    }


    public void updateTransactionID(String username, int id) {
        UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey(GeneralDbColumns.username.name(), username)
                .withUpdateExpression("set lastTransactionID = :l")
                .withValueMap(new ValueMap().withInt(":l", id))
                .withReturnValues(ReturnValue.UPDATED_NEW);
        table.updateItem(updateItemSpec);
    }

    public String grabFullName(String username) {
        GetItemSpec spec = new GetItemSpec().withPrimaryKey(GeneralDbColumns.username.name(), username);
        Item outcome = table.getItem(spec);
        String f = outcome.getString(GeneralDbColumns.firstName.name());
        String l = outcome.getString(GeneralDbColumns.lastName.name());

        return f + " " + l;
    }

    public String grabFirstName(String username) {
        GetItemSpec spec = new GetItemSpec().withPrimaryKey(GeneralDbColumns.username.name(), username);
        Item outcome = table.getItem(spec);
        String fname = outcome.getString(GeneralDbColumns.firstName.name());

        return fname;
    }

    public String returnEmail(String username) {
        GetItemSpec spec = new GetItemSpec().withPrimaryKey(GeneralDbColumns.username.name(), username);
        Item outcome = table.getItem(spec);
        String email = outcome.getString(GeneralDbColumns.primaryEmail.name());

        return email;

    }

    public String returnHashedPass(String username) {
        GetItemSpec spec = new GetItemSpec().withPrimaryKey(GeneralDbColumns.username.name(), username);
        Item outcome = table.getItem(spec);
        String hashedPass = outcome.getString(GeneralDbColumns.hashedPassword.name());

        return hashedPass;
    }


    public Number getAccountID(String username) {
        GetItemSpec spec = new GetItemSpec().withPrimaryKey(GeneralDbColumns.username.name(), username);
        Item outcome = table.getItem(spec);
        return outcome.getNumber(GeneralDbColumns.accountID.name());
    }







}




